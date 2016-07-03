package com.dmi.test.bookfinder.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.network.ApiManager;
import com.dmi.test.bookfinder.storage.database.DatabaseHandler;
import com.dmi.test.bookfinder.storage.preferences.SessionPreferences;
import com.dmi.test.bookfinder.ui.adapters.BookListAdapter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * An activity representing a list of Books. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BookDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BookListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private static final int ADD_BOOK = 0;

    private BookListAdapter mBookListAdapter;
    private View recyclerView;
    private List<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBook = new Intent(BookListActivity.this, AddBookActivity.class);
                startActivityForResult(addBook, ADD_BOOK);
            }
        });


        new BookListAsyncTask().execute();


        recyclerView = findViewById(R.id.book_list);
        assert recyclerView != null;


        if (findViewById(R.id.book_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ADD_BOOK) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();

                Book book = b.getParcelable("BOOK");
                Log.d("after -----", "id: " + book.getId());
                Log.d("after -----", "author: " + book.getAuthor());
                mBookList.add(0, book);
                mBookListAdapter.notifyItemInserted(0);
            }
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(mBookListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search_home: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Reload Book List");

                View viewInflated = getLayoutInflater().inflate(R.layout.dialog_pagination, null);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.etNumberOfBooks);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (TextUtils.isEmpty(input.getText().toString())) {
                            dialog.dismiss();
                        } else {
                            String numOfBooks = input.getText().toString();
                            new BookListCountAsyncTask().execute(Integer.parseInt(numOfBooks));
                        }

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            }
            case R.id.action_logout: {
                if (SessionPreferences.isUserLoggedIn()) {
                    SessionPreferences.clearUserSession();

                    DatabaseHandler db = new DatabaseHandler(this);
                    db.deleteAllData();

                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used for calling BookList by Count
     */
    private class BookListCountAsyncTask extends AsyncTask<Integer, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(Integer... ints) {

            Call<List<Book>> call = ApiManager.getApi().getBookListByCount(ints[0]);

            List<Book> bookList = null;
            try {
                bookList = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("------------", "Author: " + bookList.get(0).getAuthor());
            Log.d("------------", "ImageUrl: " + bookList.get(0).getImageUrl());
            Log.d("------------", "ID: " + bookList.get(0).getId());
            Log.d("------------", "Title: " + bookList.get(0).getTitle());
            Log.d("------------", "Price: " + bookList.get(0).getPrice());
            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);


            mBookList = books;

            mBookListAdapter = new BookListAdapter(books);
            mBookListAdapter.notifyDataSetChanged();
            setupRecyclerView((RecyclerView) recyclerView);

            Toast.makeText(getBaseContext(), "List updated to: " + books.size(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Default for calling BookList (default 10)
     */
    private class BookListAsyncTask extends AsyncTask<Void, Void, List<Book>> {


        @Override
        protected List<Book> doInBackground(Void... ints) {


            // Fetch and print a list of Books.
            Call<List<Book>> call = ApiManager.getApi().getBookList();

            List<Book> bookList = null;
            try {
                bookList = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            mBookList = books;

            DatabaseHandler handler = new DatabaseHandler(getBaseContext());
            if (!handler.getAllBooks().isEmpty()) {
                books.addAll(handler.getAllBooks());
            }

            mBookListAdapter = new BookListAdapter(books);
            mBookListAdapter.notifyDataSetChanged();
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }
}


