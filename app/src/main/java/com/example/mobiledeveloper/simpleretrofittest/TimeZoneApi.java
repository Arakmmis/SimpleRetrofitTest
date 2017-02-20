package com.example.mobiledeveloper.simpleretrofittest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TimeZoneApi {

    @GET("list-time-zone?format=json")
    Call<Zones> loadZones(@Query("key") String apiKey);
}
