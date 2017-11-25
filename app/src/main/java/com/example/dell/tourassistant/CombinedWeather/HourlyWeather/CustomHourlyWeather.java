package com.example.dell.tourassistant.CombinedWeather.HourlyWeather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 11/22/2017.
 */

public class CustomHourlyWeather implements Parcelable {
    private String time;
    private double temp;
    private String iconCode;


    public CustomHourlyWeather(String time, double temp, String iconCode) {
        this.time = time;
        this.temp = temp;
        this.iconCode = iconCode;
    }

    public CustomHourlyWeather() {
    }

    protected CustomHourlyWeather(Parcel in) {
        time = in.readString();
        temp = in.readDouble();
        iconCode = in.readString();
    }

    public static final Creator<CustomHourlyWeather> CREATOR = new Creator<CustomHourlyWeather>() {
        @Override
        public CustomHourlyWeather createFromParcel(Parcel in) {
            return new CustomHourlyWeather(in);
        }

        @Override
        public CustomHourlyWeather[] newArray(int size) {
            return new CustomHourlyWeather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeDouble(temp);
        dest.writeString(iconCode);
    }

    public String getTime() {
        return time;
    }

    public double getTemp() {
        return temp;
    }

    public String getIconCode() {
        return iconCode;
    }
}
