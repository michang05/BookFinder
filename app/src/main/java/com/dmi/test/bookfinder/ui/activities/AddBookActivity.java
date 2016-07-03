package com.dmi.test.bookfinder.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dmi.test.bookfinder.R;
import com.dmi.test.bookfinder.model.Book;
import com.dmi.test.bookfinder.storage.database.DatabaseHandler;
import com.dmi.test.bookfinder.util.DataUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mikey on 7/2/2016.
 */
public class AddBookActivity extends AppCompatActivity {

    @Bind(R.id.etTitle)
    EditText etTitle;

    @Bind(R.id.etAuthor)
    EditText etAuthor;

    @Bind(R.id.etPrice)
    EditText etPrice;

    @Bind(R.id.etImageUrl)
    EditText etImageUrl;

    @Bind(R.id.btnSaveToLocal)
    Button btnSaveToLocal;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



      btnSaveToLocal.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              String title = etTitle.getText().toString();
              String author = etAuthor.getText().toString();
              String price = etPrice.getText().toString();

              String imgUrl = etImageUrl.getText().toString();


              Book book = new Book();

              book.setId(String.valueOf(DataUtil.randomInt()));
              book.setTitle(title);
              book.setAuthor(author);
              if(TextUtils.isEmpty(price)){
                  price="0.0";
              }
              book.setPrice(Double.parseDouble(price));
              book.setImageUrl(imgUrl);

              db = new DatabaseHandler(AddBookActivity.this);
              Log.d("----------","price: "+Double.parseDouble(price));
              db.addBook(book);

              if(!db.getAllBooks().isEmpty()) {
                  book = db.getBookById(Integer.parseInt(book.getId()));
              }

              Log.d("b4 going to list-----", "id: " + book.getId());
              Log.d("b4 going to list-----", "author: " + book.getAuthor());

              Intent bookList = new Intent(getBaseContext(),BookListActivity.class);
              bookList.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              bookList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(bookList);
              finish();

              //TODO: something isn't right about the parcel, the values are being changed
              // If I will do this, the parcel will have the id into author and vice versa.
              // Check the onActivityResult values from Parcel. the values on this LOg changed
              //(the Log above verifies I have passed the correct values)
//              Intent returnIntent = new Intent();
//              returnIntent.putExtra("BOOK",book);
//              setResult(Activity.RESULT_OK,returnIntent);
//              finish();
          }
      });
    }

}
