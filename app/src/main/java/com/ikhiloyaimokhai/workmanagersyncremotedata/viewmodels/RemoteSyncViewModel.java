package com.ikhiloyaimokhai.workmanagersyncremotedata.viewmodels;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;
import com.ikhiloyaimokhai.workmanagersyncremotedata.repository.BookRepository;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.Resource;
import com.ikhiloyaimokhai.workmanagersyncremotedata.workers.SyncDataWorker;
import com.ikhiloyaimokhai.workmanagersyncremotedata.workers.WorkerUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ikhiloyaimokhai.workmanagersyncremotedata.util.Constants.SYNC_DATA_WORK_NAME;
import static com.ikhiloyaimokhai.workmanagersyncremotedata.util.Constants.TAG_SYNC_DATA;


public class RemoteSyncViewModel extends AndroidViewModel {
    private BookRepository mRepository;
    private WorkManager mWorkManager;
    // New instance variable for the WorkInfo
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private static List<Book> books;

    public RemoteSyncViewModel(BookRepository mRepository) {
        super(mRepository.getApplication());
        this.mRepository = mRepository;
        mWorkManager = WorkManager.getInstance(mRepository.getApplication());
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_SYNC_DATA);
    }

    public LiveData<Resource<List<Book>>> loadBooks() {
        return mRepository.loadBooks();
    }

    public void fetchData() {

        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(SyncDataWorker.class, 15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        mWorkManager.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );

    }

    public LiveData<List<Book>> getBooks() {
        return mRepository.getBooks();
    }

    public void setOutputData(String outputData) {
        books = WorkerUtils.fromJson(outputData);
    }

    public List<Book> getOutputData() {
        return books;
    }


    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }

    /**
     * Cancel work using the work's unique name
     */
    public void cancelWork() {
        Log.i("VIEWMODEL", "Cancelling work");
        mWorkManager.cancelUniqueWork(SYNC_DATA_WORK_NAME);
    }
}
