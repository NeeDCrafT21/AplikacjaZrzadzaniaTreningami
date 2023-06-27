package com.czarnecki.myapplication.Activities.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.czarnecki.myapplication.Models.Workout;
import com.czarnecki.myapplication.R;
import com.czarnecki.myapplication.Service.MyDatabase;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    private List<Workout> workoutsList;
    private LayoutInflater layoutInflater;
    private Context context;

    public WorkoutAdapter(Context context, List<Workout> workoutsList) {
        super(context, 0, workoutsList);
        this.context = context;
        this.workoutsList = workoutsList;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return workoutsList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Workout workout = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.workout_row, parent, false);
        }

        TextView name = convertView.findViewById(R.id.textView_workout_name);
        TextView exercisesNumber = convertView.findViewById(R.id.textView_exercises_number);
        Button removeButton = convertView.findViewById(R.id.button_remove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabase database = MyDatabase.instanceOfDatabase(context);
                Workout.workoutsList.remove(workout);
                database.removeWorkout(workout);
                WorkoutAdapter.this.notifyDataSetChanged();
            }
        });

        name.setText(workout.getName());
        exercisesNumber.setText(workout.getExerciseCount() + " Exercises");

        return convertView;
    }
}
