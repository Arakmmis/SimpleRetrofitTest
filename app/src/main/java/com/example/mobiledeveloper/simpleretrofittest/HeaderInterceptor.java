package com.example.mobiledeveloper.simpleretrofittest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private static final String CACHE_CONTROL = "Cache-Control";
    private Context context;

    HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain)
            throws IOException {
        CacheControl maxStaleCache = new CacheControl.Builder()
                .maxStale(30, TimeUnit.DAYS).build();

        Request request = chain.request();

        if (!isConnected()) {
            request = request.newBuilder()
                    .addHeader(CACHE_CONTROL, maxStaleCache.toString())
                    .build();

            Log.d("Interceptor", request.headers().toString());
        }

        CacheControl maxAgeCache = new CacheControl.Builder()
                .maxAge(1, TimeUnit.SECONDS).build();

        Response response = chain.proceed(request);
        response = response.newBuilder()
                .addHeader(CACHE_CONTROL, maxAgeCache.toString())
                .build();

        Log.d("Interceptor", response.headers().toString());

        return response;
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}