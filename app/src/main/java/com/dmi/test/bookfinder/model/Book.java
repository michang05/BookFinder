package com.dmi.test.bookfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikey on 7/2/2016.
 */
public class Book implements Parcelable{

    @SerializedName("image")
    String imageUrl;

    @SerializedName("price")
    double price;

    @SerializedName("author")
    String author;

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("link")
    String linkPath;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }



    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(){

    }


    protected Book(Parcel in) {
        this.imageUrl = in.readString();
        this.price = in.readDouble();
        this.author = in.readString();
        this.id = in.readString();
        this.title = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeDouble(price);
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(title);
    }
}
