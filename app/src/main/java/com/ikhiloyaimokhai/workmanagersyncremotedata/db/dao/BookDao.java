package com.ikhiloyaimokhai.workmanagersyncremotedata.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveUser(Book book);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveBooks(List<Book> books);

    @Transaction
    @Query("SELECT * FROM book WHERE id = :bookId")
    LiveData<Book> getBook(int bookId);


    @Transaction
    @Query("SELECT * from book")
    LiveData<List<Book>> getBooks();

    //delete
    @Transaction
    @Query("DELETE FROM book")
    void deleteBooks();
}