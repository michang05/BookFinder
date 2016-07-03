package com.dmi.test.bookfinder.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.storage.database.DatabaseHandler;
import com.dmi.test.bookfinder.ui.activities.BookDetailActivity;
import com.dmi.test.bookfinder.ui.activities.BookListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a {@link BookListActivity}
 * in two-pane mode (on tablets) or a {@link BookDetailActivity}
 * on handsets.
 */
public class BookDetailFragment extends Fragment {


    private static final String TAG = BookDetailFragment.class.getSimpleName();


    /**
     * The fragment argument representing the book ID that this fragment
     * represents.
     */
    public static final String BOOK_ID = "book_id";

    public static final String BOOK = "book";

    @Bind(R.id.imgBook)
    ImageView imgBook;

    @Bind(R.id.tvTitle)
    TextView tvTitle;

    @Bind(R.id.tvAuthor)
    TextView tvAuthor;

    @Bind(R.id.tvPrice)
    TextView tvPrice;
    private Book mBook;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(BOOK)) {

            mBook = getArguments().getParcelable(BOOK);

            Log.d(TAG, "title: " + mBook.getTitle());
            Log.d(TAG, "author: " + mBook.getAuthor());
            Log.d(TAG, "Price: " + mBook.getPrice());
            Log.d(TAG, "Id: " + mBook.getId());
            Log.d(TAG, "ImageUrl: " + mBook.getImageUrl());

        } else if (getArguments().containsKey("local_book_id")) {

            DatabaseHandler db = new DatabaseHandler(getActivity());
            mBook = db.getBookById(getArguments().getInt("local_book_id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (mBook != null) {

            if (mBook.getImageUrl() != null) {
                Glide.with(getActivity()).load(mBook.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(android.R.drawable.screen_background_light)
                        .crossFade()
                        .into(imgBook);
            }

            tvTitle.setText(mBook.getTitle());
            tvAuthor.setText(mBook.getAuthor());
            tvPrice.setText("$ " + String.valueOf(mBook.getPrice()));

        }

        return rootView;
    }
}
