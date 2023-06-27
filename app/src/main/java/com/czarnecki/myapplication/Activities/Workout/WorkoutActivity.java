package com.czarnecki.myapplication.Activities.Workout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.czarnecki.myapplication.Models.Exercise;
import com.czarnecki.myapplication.Models.Workout;
import com.czarnecki.myapplication.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class WorkoutActivity extends AppCompatActivity {

    Workout selectedWorkout;
    TextView textViewWorkoutName;
    Button backButton;
    ListView exercisesListView;
    private ArrayAdapter<String> workoutExercisesArrayAdapter;
    private ArrayList<String> workoutExerciseNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent intent = getIntent();
        int workoutID = intent.getIntExtra("workoutID", -1);
        for (Workout workout: Workout.workoutsList) {
            if (workout.getId() == workoutID) {
                selectedWorkout = workout;
                break;
            }
        }
        setWorkoutExerciseNamesList();

        workoutExercisesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workoutExerciseNamesList);
        setExerciseAdapter();

        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAddExercisePopupWindow(selectedWorkout.getExercisesList().get(position));
            }
        });

        textViewWorkoutName = (TextView) findViewById(R.id.textView_workout_name);
        textViewWorkoutName.setText(selectedWorkout.getName());

        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setWorkoutExerciseNamesList() {
        workoutExerciseNamesList = selectedWorkout.getExercisesList().stream()
                .map(Exercise::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void setExerciseAdapter() {
        exercisesListView = findViewById(R.id.list_workout_exercises);
        exercisesListView.setAdapter(workoutExercisesArrayAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void showAddExercisePopupWindow(Exercise exercise) {
        View view = View.inflate(getApplicationContext(), R.layout.exercise_popup, null);

        final ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        final View overlayView = new View(getApplicationContext());
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setBackgroundColor(Color.parseColor("#80000000"));
        rootView.addView(overlayView);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(view, width, height, false);
        popupWindow.showAtLocation(findViewById(R.id.layout_workout), Gravity.CENTER, 0, 0);
        popupWindow.setOutsideTouchable(true);

        overlayView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss(); // Dismiss the popup window
                    return true;
                }
                return false;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rootView.removeView(overlayView);
            }
        });

        TextView exerciseName = view.findViewById(R.id.textView_workout_exercise_name);
        exerciseName.setText("Type: " + exercise.getName());

        TextView exerciseType = view.findViewById(R.id.textView_workout_exercise_type);
        exerciseType.setText("Muscle group: " + exercise.getType());

        TextView exerciseMuscle = view.findViewById(R.id.textView_workout_exercise_muscle);
        exerciseMuscle.setText("Equipment: " + exercise.getMuscle());

        TextView exerciseEquipment = view.findViewById(R.id.textView_workout_exercise_equipment);
        exerciseEquipment.setText(exercise.getEquipment());

        TextView exerciseInstructions = view.findViewById(R.id.textView_workout_exercise_instructions);
        exerciseInstructions.setText(exercise.getInstructions());
    }
}