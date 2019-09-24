package com.ikhiloyaimokhai.workmanagersyncremotedata.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ikhiloyaimokhai.workmanagersyncremotedata.R;
import com.ikhiloyaimokhai.workmanagersyncremotedata.db.entity.Book;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context mContext;
    private List<Book> books;


    public BookAdapter(Context mContext, List<Book> books) {
        this.mContext = mContext;
        this.books = books;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        private TextView title;

        @Nullable
        private TextView genre;

        @Nullable
        private TextView author;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            genre = itemView.findViewById(R.id.genre);
            author = itemView.findViewById(R.id.author);
        }


        public void bind(Book book) {
            if (title != null) title.setText(book.getTitle());
            if (genre != null) genre.setText(book.getGenre());
            if (author != null) author.setText(book.getAuthor());
        }
    }


}
