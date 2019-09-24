package com.ikhiloyaimokhai.workmanagersyncremotedata.util;

import android.os.Build;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;


import com.ikhiloyaimokhai.workmanagersyncremotedata.service.ApiResponse;

import java.util.Objects;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @MainThread
    protected NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue(Resource.<ResultType>loading(null));

        LiveData<ResultType> dbSource = loadFromDb();

        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType data) {
                result.removeSource(dbSource);
                if (NetworkBoundResource.this.shouldFetch(data)) {
                    NetworkBoundResource.this.fetchFromNetwork(dbSource);
                } else {

                    result.addSource(dbSource, (ResultType newData) -> {
                        NetworkBoundResource.this.setValue(Resource.success(newData));
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> setValue(Resource.loading(newData)));
        // adding a data source form the network, this takes time
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource); // removes the existing data sources since we've gotten a response from the network call
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    //saves the result/data from network to db, so the db has an updated data
                    //this is done in a background thread
                    NetworkBoundResource.this.saveCallResult(NetworkBoundResource.this.processResponse(response));
                    appExecutors.mainThread().execute(() ->
                                    // we specially request a new live data,
                                    // otherwise we will get immediately last cached value,
                                    // which may not be updated with latest results received from network.
                            {
                                result.addSource(NetworkBoundResource.this.loadFromDb(), // -> latest data from the network used to update any calling activity, single source of truth
                                        (ResultType newData) -> {
                                            NetworkBoundResource.this.setValue(Resource.success(newData));
                                        });
                            }
                    );
                });
            } else {
                NetworkBoundResource.this.onFetchFailed();
                result.addSource(dbSource,
                        newData -> NetworkBoundResource.this.setValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();


}