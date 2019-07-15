package com.example.assistant;

import com.google.gson.annotations.SerializedName;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Chucknorris {

    public static class ApiResult {
        @SerializedName("value")
        public String value;
    }

    public interface ChucknorrisService{
//        https://api.chucknorris.io/jokes/random
        @GET("jokes/random")
        Call<ApiResult> getResult();
    }

    public static void get(final Consumer<String> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.chucknorris.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call<ApiResult> call = retrofit
                .create(ChucknorrisService.class)
                .getResult();

        call.enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(Call<ApiResult> call, Response<ApiResult> response) {
                ApiResult apiResult = response.body();
                String result = apiResult.value;
                System.out.println("println : " + result);
                callback.accept(result);
            }

            @Override
            public void onFailure(Call<ApiResult> call, Throwable t) {

            }
        });
    }
}
