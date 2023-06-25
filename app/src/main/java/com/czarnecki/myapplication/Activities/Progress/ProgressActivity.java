package com.czarnecki.myapplication.Activities.Progress;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.czarnecki.myapplication.Models.PhotoItem;
import com.czarnecki.myapplication.R;
import com.czarnecki.myapplication.Service.MyDatabase;

import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private PhotoAdapter photoAdapter;
    private ListView photoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Button btnCapture = findViewById(R.id.btnCapture);
        photoListView = findViewById(R.id.photoListView);

        //photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, PhotoItem.photoList);
        photoListView.setAdapter(photoAdapter);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureLauncher.launch(createTakePictureIntent());
            }
        });

        photoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click if needed
            }
        });

        // Initialize the activity result launcher
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");

                            Uri imageUri = saveImageToGallery(imageBitmap); // Save the image to gallery and get its Uri

                            PhotoItem photoItem = new PhotoItem(imageUri, new Date());
                            PhotoItem.photoList.add(photoItem);
                            MyDatabase database = MyDatabase.instanceOfDatabase(getApplicationContext());
                            database.addPhotoItem(photoItem);

                            photoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private Intent createTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return takePictureIntent;
    }

    private Uri saveImageToGallery(Bitmap imageBitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (imageUri != null) {
            try {
                OutputStream outputStream = resolver.openOutputStream(imageUri);
                if (outputStream != null) {
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                    return imageUri;
                }
            } catch (Exception e) {
                Log.e("Save Image", "Failed to save image to gallery: " + e.getMessage());
            }
        }

        return null;
    }
}