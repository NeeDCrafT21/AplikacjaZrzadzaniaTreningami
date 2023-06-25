package com.czarnecki.myapplication.Service;

import com.czarnecki.myapplication.Models.ExerciseResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExercisesAPIService {
    private String BASE_URL = "https://api.api-ninjas.com/v1/";
    private String API_KEY = "b4QRDvef4B2s4Dn2xD2ChA==7TQuqJiKxfUvwbDF";
    private ExercisesAPI exercisesAPI;

    public ExercisesAPIService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        exercisesAPI = retrofit.create(ExercisesAPI.class);
    }

    public void getExerciseListByTypeMuscleDifficulty(String type, String muscle, String difficulty, final ExerciseCallback callback) {
        Call<List<ExerciseResponse>> call = exercisesAPI.getExerciseListByTypeMuscleDifficulty(API_KEY, type, muscle, difficulty);
        call.enqueue(new Callback<List<ExerciseResponse>>() {
            @Override
            public void onResponse(Call<List<ExerciseResponse>> call, Response<List<ExerciseResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ExerciseResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public interface ExerciseCallback {
        void onSuccess(List<ExerciseResponse> exerciseList);
        void onFailure(Throwable t);
    }
}
