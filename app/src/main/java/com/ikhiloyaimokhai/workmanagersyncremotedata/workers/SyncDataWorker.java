package com.ikhiloyaimokhai.workmanagersyncremotedata.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ikhiloyaimokhai.workmanagersyncremotedata.App;
import com.ikhiloyaimokhai.workmanagersyncremotedata.R;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.dao.BookDao;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.BookService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class SyncDataWorker extends Worker {
    private BookService bookService;
    private BookDao bookDao;

    private static final String TAG = SyncDataWorker.class.getSimpleName();

    public SyncDataWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        bookService = App.get().getBookService();
        bookDao = App.get().getBookDao();
    }

    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();
        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host");
        WorkerUtils.sleep();

        try {
            //create a call to network
            Call<List<Book>> call = bookService.fetchBooks();
            Response<List<Book>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty() && response.body().size() > 0) {

                String data = WorkerUtils.toJson(response.body());
                Log.i(TAG, "data fetched from network successfully");

                //delete existing book data
                bookDao.deleteBooks();

                bookDao.saveBooks(response.body());

                WorkerUtils.makeStatusNotification(applicationContext.getString(R.string.new_data_available), applicationContext);

                return Result.success();
            } else {
                return Result.retry();
            }


        } catch (Throwable e) {
            e.printStackTrace();
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error fetching data", e);
            return Result.failure();
        }
    }


    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}
