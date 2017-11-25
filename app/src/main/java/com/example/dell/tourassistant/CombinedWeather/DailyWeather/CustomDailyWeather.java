package com.example.dell.tourassistant.CombinedWeather.DailyWeather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 11/22/2017.
 */

public class CustomDailyWeather implements Parcelable {
    private double maxTemp;
    private double minTemp;
    private String date;
    private String iconCode;

    public CustomDailyWeather(double maxTemp, double minTemp, String date, String iconCode) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.date = date;
        this.iconCode = iconCode;
    }

    protected CustomDailyWeather(Parcel in) {
        maxTemp = in.readDouble();
        minTemp = in.readDouble();
        date = in.readString();
        iconCode = in.readString();
    }


    public CustomDailyWeather() {
    }

    public static final Creator<CustomDailyWeather> CREATOR = new Creator<CustomDailyWeather>() {
        @Override
        public CustomDailyWeather createFromParcel(Parcel in) {
            return new CustomDailyWeather(in);
        }

        @Override
        public CustomDailyWeather[] newArray(int size) {
            return new CustomDailyWeather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(maxTemp);
        dest.writeDouble(minTemp);
        dest.writeString(date);
        dest.writeString(iconCode);
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getDate() {
        return date;
    }

    public String getIconCode() {
        return iconCode;
    }
}
