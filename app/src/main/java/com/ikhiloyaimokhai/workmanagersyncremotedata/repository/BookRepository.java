package com.ikhiloyaimokhai.workmanagersyncremotedata.repository;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.ikhiloyaimokhai.workmanagersyncremotedata.db.BookDatabase;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.dao.BookDao;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.ApiResponse;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.BookService;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.AppExecutors;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.NetworkBoundResource;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.Resource;

import java.util.List;

import timber.log.Timber;

public class BookRepository {
    private static BookDao mBookDao;
    private static BookService bookService;
    private final AppExecutors appExecutors;
    private final Activity activity;
    private final android.app.Application application;
    private static String LOG_TAG = BookRepository.class.getSimpleName();


    public BookRepository(android.app.Application application, Activity activity, BookService bookService,
                          AppExecutors appExecutors) {
        this.application = application;
        this.activity = activity;
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        BookRepository.bookService = bookService;
        this.appExecutors = appExecutors;
    }


    public LiveData<List<Book>> getBooks(){
        return mBookDao.getBooks();
    }

    public LiveData<Resource<List<Book>>> loadBooks() {
        /**
         * List<Book> is the [ResultType]
         * List<Book></Book> is the [RequestType]
         */
        return new NetworkBoundResource<List<Book>, List<Book>>(appExecutors) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void saveCallResult(@NonNull List<Book> books) {
                Timber.d("call to delete book in db");
                mBookDao.deleteBooks();
                Timber.d("call to insert results to db");
                mBookDao.saveBooks(books);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected boolean shouldFetch(@Nullable List<Book> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Book>> loadFromDb() {
                Timber.d(" call to load from db");
                return mBookDao.getBooks();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Book>>> createCall() {
                Timber.d("creating a call to network");
                return bookService.getBooks();
            }

            @Override
            protected List<Book> processResponse(ApiResponse<List<Book>> response) {
                return super.processResponse(response);
            }
        }.asLiveData();
    }

    public android.app.Application getApplication() {
        return application;
    }


}
