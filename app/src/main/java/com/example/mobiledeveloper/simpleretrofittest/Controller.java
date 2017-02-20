package com.example.mobiledeveloper.simpleretrofittest;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Controller implements Callback<Zones> {

    private static final String BASE_URL = "http://api.timezonedb.com/v2/";

    private static final String API_KEY = "FC1A2CGOBYHH";

    private Call<Zones> call;
    private Context context;
    private OkHttpClient.Builder client = new OkHttpClient.Builder();

    Controller(Context context) {
        this.context = context;
    }

    void start() {
        long SIZE_OF_CACHE = 20 * 1024 * 1024;
        final Cache cache = new Cache(new File(context.getCacheDir(), "retrofit_cache"), SIZE_OF_CACHE);

        client.cache(cache);
        client.addInterceptor(new HeaderInterceptor(context));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TimeZoneApi timeZoneApi = retrofit.create(TimeZoneApi.class);

        call = timeZoneApi.loadZones(API_KEY);
        call.enqueue(this);
    }

    void stop() {
        if (call != null) {
            call.cancel();
        }
    }

    void restart() {
        this.stop();
        Call<Zones> c = call.clone();
        c.enqueue(this);
    }

    @Override
    public void onResponse(Call<Zones> call, Response<Zones> response) {
        if (response.isSuccessful()) {
            ((MainActivity) context).success(response.body().zones);
        } else {
            try {
                ((MainActivity) context).fail(response.errorBody().string());
                Log.e("MainActivity", response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<Zones> call, Throwable t) {
        ((MainActivity) context).fail(t.getLocalizedMessage());
        Log.e("MainActivity", t.getLocalizedMessage());
    }
}
