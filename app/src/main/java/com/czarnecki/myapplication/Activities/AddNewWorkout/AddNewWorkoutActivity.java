package com.czarnecki.myapplication.Activities.AddNewWorkout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.czarnecki.myapplication.Models.Exercise;
import com.czarnecki.myapplication.Models.ExerciseResponse;
import com.czarnecki.myapplication.Models.Workout;
import com.czarnecki.myapplication.R;
import com.czarnecki.myapplication.Service.ExercisesAPIService;
import com.czarnecki.myapplication.Service.MyDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddNewWorkoutActivity extends AppCompatActivity {

    private Button buttonAdd;
    private Button buttonCancel;
    private EditText editTextName;
    private Button addNewExerciseButton;
    private ListView exercisesListView;
    private ArrayAdapter<String> workoutExercisesArrayAdapter;
    private ArrayList<Exercise> exercisesList;
    private ArrayList<String> exerciseNamesList;

    private Spinner exerciseSpinner;
    private ArrayAdapter<String> exerciseArrayAdapter;
    private final ExercisesAPIService exercisesAPIService = new ExercisesAPIService();

    boolean difficultyIsStart = true;
    private String chosenDifficulty;
    boolean typeIsStart = true;
    private String chosenType;
    boolean muscleIsStart = true;
    private String chosenMuscle;
    private List<ExerciseResponse> apiExerciseList = new ArrayList<>();

    boolean temp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);

        editTextName = findViewById(R.id.editText_name);

        exercisesList = new ArrayList<>();
        exerciseNamesList = new ArrayList<>();
        workoutExercisesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNamesList);
        setExerciseAdapter();

        addNewExerciseButton = (Button) findViewById(R.id.button_add_new_exercise);
        addNewExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExercisePopupWindow();
            }
        });

        buttonAdd = (Button) findViewById(R.id.button_add_new_workout);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Workout.workoutsList.size();
                String name = editTextName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Set workout name", Toast.LENGTH_SHORT).show();
                    return;
                }
                Workout newWorkout = new Workout(id, name, exercisesList.size(), exercisesList);
                Workout.workoutsList.add(newWorkout);
                MyDatabase database = MyDatabase.instanceOfDatabase(getApplicationContext());
                database.addWorkout(newWorkout);
                finish();
            }
        });

        buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setExerciseAdapter() {
        exercisesListView = findViewById(R.id.list_exercises);
        exercisesListView.setAdapter(workoutExercisesArrayAdapter);
    }

    private void showAddExercisePopupWindow() {
        View view = View.inflate(getApplicationContext(), R.layout.add_exercise_popup, null);

        final ViewGroup rootView = (ViewGroup) getWindow().getDecorView().getRootView();
        final View overlayView = new View(getApplicationContext());
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setBackgroundColor(Color.parseColor("#80000000"));
        rootView.addView(overlayView);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        PopupWindow popupWindow = new PopupWindow(view, width, height, false);
        popupWindow.showAtLocation(findViewById(R.id.layout_new_workout), Gravity.CENTER, 0, 0);

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

        chosenDifficulty = "beginner";
        chosenType = "strongman";
        chosenMuscle = "quadriceps";

        Map<String, String> exerciseDifficultyMap = new HashMap<String, String>() {{
            put("Beginner", "beginner");
            put("Intermediate", "intermediate");
            put("Expert", "expert");
        }};

        Map<String, String> exerciseTypeMap = new HashMap<String, String>() {{
            put("Cardio", "cardio");
            put("Olympic weightlifting", "olympic_weightlifting");
            put("Plyometrics", "plyometrics");
            put("Powerlifting", "powerlifting");
            put("Strength", "strength");
            put("Stretching", "stretching");
            put("Strongman", "strongman");
        }};

        Map<String, String> exerciseMuscleMap = new HashMap<String, String>() {{
            put("Abdominals", "abdominals");
            put("Abductors", "abductors");
            put("Adductors", "adductors");
            put("Biceps", "biceps");
            put("Calves", "calves");
            put("Chest", "chest");
            put("Forearms", "forearms");
            put("Glutes", "glutes");
            put("Hamstrings", "hamstrings");
            put("Lats", "lats");
            put("Lower back", "lower_back");
            put("Middle back", "middle_back");
            put("Neck", "neck");
            put("Quadriceps", "quadriceps");
            put("Traps", "traps");
            put("Triceps", "triceps");
        }};

        Spinner difficultySpinner = view.findViewById(R.id.spinner_difficulty);
        ArrayAdapter<String> difficultyArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(exerciseDifficultyMap.keySet()));
        difficultyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyArrayAdapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenDifficulty = exerciseDifficultyMap.get(difficultySpinner.getSelectedItem().toString());
                if (difficultyIsStart) {
                    difficultyIsStart = false;
                } else {
                    addExercisesList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner typeSpinner = view.findViewById(R.id.spinner_type);
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(exerciseTypeMap.keySet()));
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeArrayAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenType = exerciseTypeMap.get(typeSpinner.getSelectedItem().toString());
                if (typeIsStart) {
                    typeIsStart = false;
                } else {
                    addExercisesList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner muscleSpinner = view.findViewById(R.id.spinner_muscle);
        ArrayAdapter<String> muscleArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(exerciseMuscleMap.keySet()));
        muscleArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(muscleArrayAdapter);
        muscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenMuscle = exerciseMuscleMap.get(muscleSpinner.getSelectedItem().toString());
                if (muscleIsStart) {
                    muscleIsStart = false;
                } else {
                    addExercisesList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        exerciseSpinner = view.findViewById(R.id.spinner_exercise);
        addExercisesList();

        Button exerciseAddButton = view.findViewById(R.id.button_exercise_add);
        exerciseAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = exerciseSpinner.getSelectedItemPosition();
                ExerciseResponse tempExercise = apiExerciseList.get(pos);
                Toast.makeText(getApplicationContext(),tempExercise.getName(), Toast.LENGTH_SHORT).show();
                //Exercise newExercise = new Exercise(exerciseSpinner.getSelectedItem().toString());
                Exercise newExercise = new Exercise(tempExercise.getName(), tempExercise.getType(), tempExercise.getMuscle(), tempExercise.getEquipment(), tempExercise.getDifficulty(), tempExercise.getInstructions());
                exerciseNamesList.add(newExercise.getName());
                exercisesList.add(newExercise);
                setExerciseAdapter();
                popupWindow.dismiss();
            }
        });
    }

    private void addExercisesList() {
        if (temp) {
            return;
        }
        temp = true;
        apiExerciseList.clear();
        //Toast.makeText(getApplicationContext(),chosenDifficulty + chosenType + chosenMuscle, Toast.LENGTH_SHORT).show();
        exercisesAPIService.getExerciseListByTypeMuscleDifficulty(chosenType, chosenMuscle, chosenDifficulty, new ExercisesAPIService.ExerciseCallback() {
            @Override
            public void onSuccess(List<ExerciseResponse> exerciseList) {
                apiExerciseList.addAll(exerciseList);
                updateExerciseSpinner();
                temp = false;
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(),"No exercises with given parameters", Toast.LENGTH_SHORT).show();
                updateExerciseSpinner();
                temp = false;
            }
        });
    }

    private void updateExerciseSpinner() {
        List<String> exerciseList = apiExerciseList.stream()
                .map(ExerciseResponse::getName)
                .collect(Collectors.toList());

        //Toast.makeText(getApplicationContext(),exerciseList.toString(), Toast.LENGTH_SHORT).show();

        exerciseArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exerciseList);
        exerciseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseArrayAdapter);
        workoutExercisesArrayAdapter.notifyDataSetChanged();
    }
}