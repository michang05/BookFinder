package com.dmi.test.bookfinder.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.ui.adapters.viewholder.BookListViewHolder;

import java.util.List;

/**
 * Created by Mikey on 7/2/2016.
 */
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Book> mBookList;

    public BookListAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        RecyclerView.ViewHolder vh;

        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
        vh = new BookListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Book bookItem = mBookList.get(position);

        BookListViewHolder bookListViewHolder = (BookListViewHolder) holder;
        bookListViewHolder.bindBookListItem(bookItem);

    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
