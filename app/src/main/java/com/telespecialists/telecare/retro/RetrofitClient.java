package com.telespecialists.telecare.retro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.telespecialists.telecare.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit retro = null;
    private static final int TIME_OUT = 3000;


    public static Retrofit getClientRetro() {
        if (retro == null) {
            OkHttpClient.Builder c = new OkHttpClient.Builder();
            c.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
            c.readTimeout(TIME_OUT, TimeUnit.SECONDS);
            c.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

            HttpLoggingInterceptor i = new HttpLoggingInterceptor();
            i.setLevel(HttpLoggingInterceptor.Level.BODY);
            c.addInterceptor(i);
            c.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder builder1 = original.newBuilder()
                        .addHeader("Authorization", "bearer " + Prefs.getString(Constants.SAVED_TOKEN, ""));
                Request request = builder1.build();
                return chain.proceed(request);
            });

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retro = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(c.build())
                    .build();
        }
        return retro;
    }


}