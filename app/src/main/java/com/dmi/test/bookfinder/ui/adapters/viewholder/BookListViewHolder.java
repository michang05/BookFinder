package com.dmi.test.bookfinder.ui.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.storage.database.DatabaseHandler;
import com.dmi.test.bookfinder.ui.activities.BookDetailActivity;
import com.dmi.test.bookfinder.ui.fragments.BookDetailFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mikey on 7/2/2016.
 */
public class BookListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = BookListViewHolder.class.getSimpleName();

    private Book mBook;

    @Bind(R.id.tvBookId)
    TextView tvBookId;

    @Bind(R.id.tvBookListTitle)
    TextView tvBookListTitle;


    public BookListViewHolder(View parent) {

        super(parent);

        ButterKnife.bind(this, parent);

        parent.setOnClickListener(this);
    }

    //called from onBindViewHolder
    public void bindBookListItem(Book book) {

        mBook = book;

        tvBookId.setText(String.valueOf(mBook.getId()));
        tvBookListTitle.setText(mBook.getTitle());
    }

    @Override
    public void onClick(View view) {

        Context c = view.getContext();


        DatabaseHandler handler = new DatabaseHandler(c);
        Intent bookDetailIntent = new Intent(c, BookDetailActivity.class);
        Book book = null;

        Log.d(TAG, "book id >>" + mBook.getId() + "<<");
        Log.d(TAG, "book title: " + mBook.getTitle());
        Log.d(TAG, "book author: " + mBook.getAuthor());
        Log.d(TAG, "book price: " + mBook.getPrice());
        Log.d(TAG, "book imgUrl: " + mBook.getImageUrl());

        if (handler.isBookInDb(mBook.getId())) {
            if (!handler.getAllBooks().isEmpty()) {
                book = handler.getBookById(Integer.parseInt(mBook.getId()));
            }
            if (book != null) {
                bookDetailIntent.putExtra("service", false);
                bookDetailIntent.putExtra("local_book_id", Integer.parseInt(mBook.getId()));
            }

        } else {
            bookDetailIntent.putExtra("service", true);
            bookDetailIntent.putExtra(BookDetailFragment.BOOK_ID, mBook.getId());
        }


        c.startActivity(bookDetailIntent);

    }
}
