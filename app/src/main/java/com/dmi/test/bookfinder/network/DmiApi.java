package com.dmi.test.bookfinder.network;


import com.dmi.test.bookfinder.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mikey on 7/2/2016.
 */
public interface DmiApi {

    // GET a list of books
    @GET("items")
    Call<List<Book>> getBookList();

    // GET a list of books by count
    @GET("items")
    Call<List<Book>> getBookListByCount(@Query("count") int count);

    // GET a specific book using its ID
    @GET("items/{id}")
    Call<Book> getBookById(@Path("id") String id);

}
