package com.czarnecki.myapplication.Activities.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.czarnecki.myapplication.Activities.AddNewWorkout.AddNewWorkoutActivity;
import com.czarnecki.myapplication.Activities.Progress.ProgressActivity;
import com.czarnecki.myapplication.Models.PhotoItem;
import com.czarnecki.myapplication.Models.Workout;
import com.czarnecki.myapplication.R;
import com.czarnecki.myapplication.Service.ExercisesAPIService;
import com.czarnecki.myapplication.Activities.Workout.WorkoutActivity;
import com.czarnecki.myapplication.Service.MyDatabase;

public class MainActivity extends AppCompatActivity {
    Button buttonAddWorkout;
    Button progressButton;
    ListView workoutsListView;
    WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My workouts");

        loadFromDatabase();
        workoutsListView = findViewById(R.id.list_workouts);
        setWorkoutAdapter();
        workoutsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout selectedWorkout = (Workout) workoutsListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                intent.putExtra("workoutID", selectedWorkout.getId());
                startActivity(intent);
            }
        });

        buttonAddWorkout = (Button) findViewById(R.id.button_add_workout);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewWorkoutActivity.class);
                startActivity(intent);
            }
        });

        progressButton = findViewById(R.id.button_progress);
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProgressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setWorkoutAdapter() {
        workoutAdapter = new WorkoutAdapter(getApplicationContext(), Workout.workoutsList);
        workoutsListView.setAdapter(workoutAdapter);
    }

    private void loadFromDatabase() {
        MyDatabase database = MyDatabase.instanceOfDatabase(this);
        Workout.workoutsList.addAll(database.getAllWorkouts());
        PhotoItem.photoList.addAll(database.getAllPhotoItems());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWorkoutAdapter();
    }


}