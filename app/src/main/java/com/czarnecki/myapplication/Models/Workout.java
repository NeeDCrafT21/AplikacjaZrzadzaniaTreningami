package com.czarnecki.myapplication.Models;

import java.util.ArrayList;

public class Workout {
    public static ArrayList<Workout> workoutsList = new ArrayList<>();

    private int id;
    private String name;
    private int exerciseCount = 0;
    private ArrayList<Exercise> exercisesList;

    public Workout(int id, String name, int exerciseCount, ArrayList<Exercise> exercisesList) {
        this.id = id;
        this.name = name;
        this.exerciseCount = exerciseCount;
        this.exercisesList = exercisesList;
    }

    public Workout getWorkoutByID(int givenID) {
        for (Workout workout: workoutsList) {
            if (workout.getId() == givenID) {
                return workout;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public int getExerciseCount() {
        return exerciseCount;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Exercise> getExercisesList() {
        return exercisesList;
    }

    public void setExercisesList(ArrayList<Exercise> exercisesList) {
        this.exercisesList = exercisesList;
    }

    public ArrayList<Workout> getWorkoutsList() {
        return workoutsList;
    }

    public void setWorkoutsList(ArrayList<Workout> workoutsList) {
        Workout.workoutsList = workoutsList;
    }
}
