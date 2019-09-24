package com.ikhiloyaimokhai.workmanagersyncremotedata.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ikhiloyaimokhai.workmanagersyncremotedata.repository.BookRepository;
import com.ikhiloyaimokhai.workmanagersyncremotedata.viewmodels.RemoteSyncViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final BookRepository mRepository;

    public ViewModelFactory(BookRepository mRepository) {
        this.mRepository = mRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(RemoteSyncViewModel.class))
            return (T) new RemoteSyncViewModel(mRepository);
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}

