package com.dmi.test.bookfinder.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.network.ApiManager;
import com.dmi.test.bookfinder.network.DmiApi;
import com.dmi.test.bookfinder.storage.preferences.SessionPreferences;
import com.dmi.test.bookfinder.ui.fragments.BookDetailFragment;

import java.io.IOException;

import retrofit2.Call;

/**
 * An activity representing a single Book detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 */
public class BookDetailActivity extends AppCompatActivity {

    private static final String TAG = BookDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

            boolean isService = getIntent().getExtras().getBoolean("service");
            if (isService) {
                String bookId = (String) getIntent().getExtras().get(BookDetailFragment.BOOK_ID);
                new BookDetailAsyncTask().execute(bookId);
            } else {
                Bundle b = getIntent().getExtras();
//                Book book =  b.getParcelable("local_book");
                int id = b.getInt("local_book_id");

                Bundle arguments = new Bundle();
                arguments.putInt("local_book_id", id);

                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.book_detail_container, fragment)
                        .commit();
            }


        }
    }

    private class BookDetailAsyncTask extends AsyncTask<String, Void, Book> {

        private Call<Book> call;

        @Override
        protected Book doInBackground(String... strs) {
            // Create a very simple REST adapter which points the DMI API endpoint.

            DmiApi client = ApiManager.createService(DmiApi.class,
                    SessionPreferences.getLoginUsername()
                    , SessionPreferences.getLoginPassword());


            Log.d(TAG, "Selected ID: " + strs[0]);
            call = client.getBookById(strs[0]);

            Book book = null;
            try {

                book = call.execute().body();

                Log.d(TAG, "title: " + book.getTitle());
                Log.d(TAG, "author: " + book.getAuthor());
                Log.d(TAG, "Price: " + book.getPrice());
                Log.d(TAG, "Id: " + book.getId());
                Log.d(TAG, "ImageUrl: " + book.getImageUrl());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            super.onPostExecute(book);

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putParcelable(BookDetailFragment.BOOK, book);

            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
