package com.czarnecki.myapplication.Models;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PhotoItem {
    public static List<PhotoItem> photoList = new ArrayList<>();
    private Uri imageUri;
    private Date date;

    public PhotoItem(Uri imageUri, Date date) {
        this.imageUri = imageUri;
        this.date = date;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Date getDate() {
        return date;
    }

    @NonNull
    @Override
    public String toString() {
        return date.toString();
    }

    public static void sortPhotoList() {
        PhotoItem.photoList.sort(new Comparator<PhotoItem>() {
            @Override
            public int compare(PhotoItem photo1, PhotoItem photo2) {
                return photo2.getDate().compareTo(photo1.getDate());
            }
        });
    }
}
