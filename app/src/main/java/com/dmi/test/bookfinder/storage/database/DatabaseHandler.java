package com.dmi.test.bookfinder.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dmi.test.bookfinder.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikey on 7/2/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dmiTest";

    // Books table name
    private static final String TABLE_BOOKS = "books";

    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_PRICE = "price";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_ID + " TEXT PRIMARY KEY NOT NULL,"
                + KEY_TITLE + " TEXT,"
                + KEY_AUTHOR + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);

        // Create tables again
        onCreate(db);
    }


    // Adding a new book
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, book.getId());
        values.put(KEY_TITLE, book.getTitle()); // Book Title
        values.put(KEY_AUTHOR, book.getAuthor()); // Book Author
        values.put(KEY_PRICE, book.getPrice()); // Book Price
        values.put(KEY_IMAGE_URL, book.getImageUrl()); // Book ImageUrl

        // Inserting Row
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close(); // Closing database connection

        return id;
    }

    // Getting a book
    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                KEY_ID,
                KEY_TITLE,
                KEY_AUTHOR,
                KEY_PRICE,
                KEY_IMAGE_URL
        };


        Cursor cursor = db.query(TABLE_BOOKS, projection, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Book book = new Book();
        book.setId(cursor.getString(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setPrice(cursor.getDouble(3));
        book.setImageUrl(cursor.getString(4));

        Log.d("getBookById---DB", "ID: " + book.getId() + " | author: " + book.getAuthor());

        return book;
    }

    public boolean isBookInDb(String bookId) {

        SQLiteDatabase db = this.getWritableDatabase();

        String Query = "Select * from " + TABLE_BOOKS + " where " + KEY_ID + " = " + bookId;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    //Get All Stored Books
    public List<Book> getAllBooks() {

        SQLiteDatabase db = this.getWritableDatabase();


        List<Book> bookList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getString(0));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setPrice(cursor.getDouble(3));
                book.setImageUrl(cursor.getString(4));

                bookList.add(book);
            } while (cursor.moveToNext());
        }

        return bookList;

    }


    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, book.getId());
        values.put(KEY_TITLE, book.getTitle()); // Book Title
        values.put(KEY_AUTHOR, book.getAuthor()); // Book Author
        values.put(KEY_PRICE, book.getPrice()); // Book Price
        values.put(KEY_IMAGE_URL, book.getImageUrl()); // Book ImageUrl

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
    }

    // Deleting a book
    public void deleteContact(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});
        db.close();
    }

   public void deleteAllData() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_BOOKS, null, null);
    }
}
