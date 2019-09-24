package com.ikhiloyaimokhai.workmanagersyncremotedata.service;

import androidx.lifecycle.LiveData;

import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BookService {

    @GET("getBooks")
    LiveData<ApiResponse<List<Book>>> getBooks();


    @GET("getBooks")
    Call<List<Book>> fetchBooks();
}
