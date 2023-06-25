package com.czarnecki.myapplication.Service;

import com.czarnecki.myapplication.Models.ExerciseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ExercisesAPI {
    @GET("exercises")
    Call<List<ExerciseResponse>> getExerciseListByTypeMuscleDifficulty(
            @Header("X-Api-Key") String apiKey,
            @Query("type") String type,
            @Query("muscle") String muscle,
            @Query("difficulty") String difficulty
    );
}
