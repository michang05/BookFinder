package com.dmi.test.bookfinder.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.network.ApiManager;
import com.dmi.test.bookfinder.network.DmiApi;
import com.dmi.test.bookfinder.storage.preferences.SessionPreferences;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Mikey on 7/2/2016.
 */
public class LoginActivity extends Activity {

    @Bind(R.id.etUsername)
    EditText etUsername;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.btnLogin)
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);


        if (SessionPreferences.isUserLoggedIn()) {
            Intent bookListIntent = new Intent(LoginActivity.this, BookListActivity.class);
            startActivity(bookListIntent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                new LoginAsyncTask().execute(username, password);

            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, List<Book>> {

        private Call<List<Book>> call;

        private String mUsername;
        private String mPassword;

        @Override
        protected List<Book> doInBackground(String... string) {

            mUsername = string[0];
            mPassword = string[1];

            DmiApi client = ApiManager.createService(DmiApi.class,
                    mUsername
                    , mPassword);

            // Fetch and print a list of Books.
            call = client.getBookList();
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


            if (books == null) {
                Toast.makeText(LoginActivity.this, "Login Credentials are Incorrect!", Toast.LENGTH_SHORT).show();
            } else {
                SessionPreferences.saveUsername(mUsername);
                SessionPreferences.savePassword(mPassword);
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                Intent bookListIntent = new Intent(LoginActivity.this, BookListActivity.class);
                startActivity(bookListIntent);
                finish();
            }
        }
    }


}
