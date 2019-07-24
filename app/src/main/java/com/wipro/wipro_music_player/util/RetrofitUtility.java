package com.wipro.wipro_music_player.util;

import android.app.DownloadManager;

import com.wipro.wipro_music_player.RetrofitInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtility {
    // Create a Retrofit Builder
    private static Retrofit getRetrofitClient() {
        return new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                // GsonConverterFactory converts directly JSON data to an Object
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Create a Service Call
    public static RetrofitInterface getRetrofitServiceCall() {
        return getRetrofitClient().create(RetrofitInterface.class);
    }
}
