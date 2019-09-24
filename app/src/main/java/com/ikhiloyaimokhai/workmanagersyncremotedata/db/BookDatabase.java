package com.ikhiloyaimokhai.workmanagersyncremotedata.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ikhiloyaimokhai.workmanagersyncremotedata.db.dao.BookDao;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;


@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static volatile BookDatabase INSTANCE;

    public abstract BookDao bookDao();

    public static BookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class, "book_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}