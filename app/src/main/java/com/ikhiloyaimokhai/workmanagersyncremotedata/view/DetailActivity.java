package com.ikhiloyaimokhai.workmanagersyncremotedata.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ikhiloyaimokhai.workmanagersyncremotedata.App;
import com.ikhiloyaimokhai.workmanagersyncremotedata.R;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;
import com.ikhiloyaimokhai.workmanagersyncremotedata.factory.ViewModelFactory;
import com.ikhiloyaimokhai.workmanagersyncremotedata.repository.BookRepository;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.BookService;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.AppExecutors;
import com.ikhiloyaimokhai.workmanagersyncremotedata.viewmodels.RemoteSyncViewModel;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        TextView tv = findViewById(R.id.tv);
        StringBuilder sb = new StringBuilder();

        BookService bookService = App.get().getBookService();
        BookRepository mRepository = new BookRepository(getApplication(), DetailActivity.this, bookService, new AppExecutors());
        ViewModelFactory factory = new ViewModelFactory(mRepository);
        RemoteSyncViewModel mRemoteSyncViewModel = ViewModelProviders.of(this, factory).get(RemoteSyncViewModel.class);


        mRemoteSyncViewModel.getBooks().observe(this, books -> {
            Log.i(TAG, books.toString());
        });


        List<Book> books = mRemoteSyncViewModel.getOutputData();
        Log.i(TAG, "Books: " + books.toString());

        books.forEach((book) ->
                sb.append(" Title- ")
                        .append(book.getTitle())
                        .append(" Genre- ")
                        .append(book.getGenre())
                        .append(" Author- ")
                        .append(book.getAuthor())
                        .append("\n"));

        tv.setText(sb.toString());


    }
}
