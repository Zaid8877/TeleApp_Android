package com.telespecialists.telecare.retro;

import android.util.Log;

import com.telespecialists.telecare.utils.Constants;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.telespecialists.telecare.utils.Constants.BASE_URL;

public class RetrofitClient {

    private static Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()));
    private static OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static <S> S createServiceToken(Class<S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.addInterceptor(logging);
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceWithToken(Class<S> serviceClass) {
        httpClient.interceptors().clear();
        httpClient.addInterceptor( chain -> {
            Request original = chain.request();
            Request.Builder builder1 = original.newBuilder()
                    .addHeader("Authorization", "bearer "+Prefs.getString(Constants.SAVED_TOKEN, ""));
            Request request = builder1.build();
            return chain.proceed(request);
        });
        httpClient.addInterceptor(logging);
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }


}