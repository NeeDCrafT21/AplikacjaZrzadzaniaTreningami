package com.czarnecki.myapplication.Service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.czarnecki.myapplication.Models.Exercise;
import com.czarnecki.myapplication.Models.PhotoItem;
import com.czarnecki.myapplication.Models.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Workouts.db";
    private static MyDatabase database;

    private static final String TABLE_WORKOUT = "workout";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EXERCISE_COUNT = "exercise_count";
    private static final String COLUMN_EXERCISES_LIST = "exercises_list";

    private static final String TABLE_PHOTO_ITEM = "photoitem";
    private static final String COLUMN_PHOTO_URI = "photo_uri";
    private static final String COLUMN_DATE = "date";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MyDatabase instanceOfDatabase(Context context)
    {
        //context.deleteDatabase(DATABASE_NAME);
        if(database == null)
            database = new MyDatabase(context);

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWorkoutTableQuery = "CREATE TABLE " + TABLE_WORKOUT + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EXERCISE_COUNT + " INTEGER,"
                + COLUMN_EXERCISES_LIST + " TEXT"
                + ")";
        db.execSQL(createWorkoutTableQuery);

        String createPhotoItemTableQuery = "CREATE TABLE " + TABLE_PHOTO_ITEM + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PHOTO_URI + " TEXT,"
                + COLUMN_DATE + " INTEGER"
                + ")";
        db.execSQL(createPhotoItemTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    public void addWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, workout.getId());
        values.put(COLUMN_NAME, workout.getName());
        values.put(COLUMN_EXERCISE_COUNT, workout.getExerciseCount());
        values.put(COLUMN_EXERCISES_LIST, convertExerciseListToString(workout.getExercisesList()));

        db.insert(TABLE_WORKOUT, null, values);
        db.close();
    }

    public void addPhotoItem(PhotoItem photoItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_URI, photoItem.getImageUri().toString());
        values.put(COLUMN_DATE, photoItem.getDate().getTime());

        db.insert(TABLE_PHOTO_ITEM, null, values);
        db.close();
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUT, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") int exerciseCount = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_COUNT));
                @SuppressLint("Range") String exercisesListString = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISES_LIST));
                ArrayList<Exercise> exercisesList = convertStringToExerciseList(exercisesListString);

                Workout workout = new Workout(id, name, exerciseCount, exercisesList);
                workouts.add(workout);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return workouts;
    }

    public List<PhotoItem> getAllPhotoItems() {
        List<PhotoItem> photoItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHOTO_ITEM, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String imageUriString = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO_URI));
                Uri imageUri = Uri.parse(imageUriString);
                @SuppressLint("Range") long dateMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
                Date date = new Date(dateMillis);

                PhotoItem photoItem = new PhotoItem(imageUri, date);
                photoItems.add(photoItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return photoItems;
    }

    private String convertExerciseListToString(ArrayList<Exercise> exercisesList) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (Exercise exercise : exercisesList) {
                JSONObject exerciseObject = new JSONObject();
                exerciseObject.put("name", exercise.getName());
                exerciseObject.put("type", exercise.getType());
                exerciseObject.put("muscle", exercise.getMuscle());
                exerciseObject.put("equipment", exercise.getEquipment());
                exerciseObject.put("difficulty", exercise.getDifficulty());
                exerciseObject.put("instructions", exercise.getInstructions());
                jsonArray.put(exerciseObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray.toString();
    }

    private ArrayList<Exercise> convertStringToExerciseList(String exercisesListString) {
        ArrayList<Exercise> exercisesList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(exercisesListString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject exerciseObject = jsonArray.getJSONObject(i);
                String name = exerciseObject.getString("name");
                String type = exerciseObject.getString("type");
                String muscle = exerciseObject.getString("muscle");
                String equipment = exerciseObject.getString("equipment");
                String difficulty = exerciseObject.getString("difficulty");
                String instructions = exerciseObject.getString("instructions");
                Exercise exercise = new Exercise(name, type, muscle, equipment, difficulty, instructions);
                exercisesList.add(exercise);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exercisesList;
    }
}
