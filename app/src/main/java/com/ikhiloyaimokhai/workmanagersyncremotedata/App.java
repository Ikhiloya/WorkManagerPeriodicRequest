package com.ikhiloyaimokhai.workmanagersyncremotedata;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.BookDatabase;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.dao.BookDao;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;
import com.ikhiloyaimokhai.workmanagersyncremotedata.factory.LiveDataCallAdapterFactory;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.BookService;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static final String BASE_URL = "https://4124ce61-915b-4590-879e-21956799abf9.mock.pstmn.io/";
    private BookService mBookService;

    private static App INSTANCE;

    private static AppExecutors mAppExecutors;

    private static BookDao mBookDao;


    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        //Gson Builder
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Timber.plant(new Timber.DebugTree());

        // HttpLoggingInterceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();


        //Retrofit
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //BookService
        mBookService = mRetrofit.create(BookService.class);
        mAppExecutors = new AppExecutors();
        mBookDao = BookDatabase.getDatabase(getApplicationContext()).bookDao();


    }

    public BookService getBookService() {
        return mBookService;
    }


    public BookDao getBookDao() {
        return mBookDao;
    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }

}
