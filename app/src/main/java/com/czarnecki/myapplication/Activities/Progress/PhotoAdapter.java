package com.czarnecki.myapplication.Activities.Progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.czarnecki.myapplication.Models.PhotoItem;
import com.czarnecki.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PhotoAdapter extends ArrayAdapter<PhotoItem> {
    private Context context;
    private List<PhotoItem> photoList;

    public PhotoAdapter(Context context, List<PhotoItem> photoList) {
        super(context, 0, photoList);
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.photo_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.imageView);
            viewHolder.dateTextView = view.findViewById(R.id.dateTextView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        PhotoItem photoItem = photoList.get(position);

        // Set the image bitmap to the ImageView
        Bitmap imageBitmap = getBitmapFromUri(photoItem.getImageUri());
        viewHolder.imageView.setImageBitmap(imageBitmap);

        // Set the date to the TextView
        viewHolder.dateTextView.setText(photoItem.getDate().toString());

        return view;
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView dateTextView;
    }
}
