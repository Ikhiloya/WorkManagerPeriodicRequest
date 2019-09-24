package com.ikhiloyaimokhai.workmanagersyncremotedata.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import com.ikhiloyaimokhai.workmanagersyncremotedata.App;
import com.ikhiloyaimokhai.workmanagersyncremotedata.R;
import com.ikhiloyaimokhai.workmanagersyncremotedata.adapters.BookAdapter;
import com.ikhiloyaimokhai.workmanagersyncremotedata.factory.ViewModelFactory;
import com.ikhiloyaimokhai.workmanagersyncremotedata.repository.BookRepository;
import com.ikhiloyaimokhai.workmanagersyncremotedata.service.BookService;
import com.ikhiloyaimokhai.workmanagersyncremotedata.util.AppExecutors;
import com.ikhiloyaimokhai.workmanagersyncremotedata.viewmodels.RemoteSyncViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RemoteSyncViewModel mRemoteSyncViewModel;
    private ProgressBar mProgressBar;
    private BookAdapter mBookAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the ViewModel
        BookService bookService = App.get().getBookService();
        BookRepository mRepository = new BookRepository(getApplication(), MainActivity.this, bookService, new AppExecutors());
        ViewModelFactory factory = new ViewModelFactory(mRepository);
        mRemoteSyncViewModel = ViewModelProviders.of(this, factory).get(RemoteSyncViewModel.class);


        mRemoteSyncViewModel.fetchData();


        // Show work info, goes inside onCreate()
        mRemoteSyncViewModel.getOutputWorkInfo().observe(this, listOfWorkInfo -> {

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_SYNC_DATA
            WorkInfo workInfo = listOfWorkInfo.get(0);
            Log.i(TAG, "WorkState: " + workInfo.getState());
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                showWorkFinished();

                //observe Room db
                mRemoteSyncViewModel.getBooks().observe(this, books -> {
                    mBookAdapter = new BookAdapter(MainActivity.this, books);
                    recyclerView.setAdapter(mBookAdapter);

                });


            } else {
                showWorkInProgress();
            }
        });

    }

    private void showWorkInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showWorkFinished() {
        mProgressBar.setVisibility(View.GONE);
    }
}
