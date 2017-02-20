package com.example.mobiledeveloper.simpleretrofittest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zone {

    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("zoneName")
    @Expose
    private String zoneName;
    @SerializedName("gmtOffset")
    @Expose
    private Integer gmtOffset;

    @Override
    public String toString() {
        return countryName + " - " + zoneName + "\n" + (gmtOffset / (60 * 60)) + " hr(s)";
    }
}
