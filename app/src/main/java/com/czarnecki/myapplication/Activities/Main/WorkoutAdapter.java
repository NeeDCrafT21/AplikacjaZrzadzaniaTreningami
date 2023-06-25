package com.czarnecki.myapplication.Activities.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.czarnecki.myapplication.Models.Workout;
import com.czarnecki.myapplication.R;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    private List<Workout> workoutsList;
    private LayoutInflater layoutInflater;

    public WorkoutAdapter(Context context, List<Workout> workoutsList) {
        super(context, 0, workoutsList);
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

        name.setText(workout.getName());
        exercisesNumber.setText(workout.getExerciseCount() + " Exercises");

        return convertView;
    }
}
