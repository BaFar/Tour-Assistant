package com.example.dell.tourassistant;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DELL on 10/26/2017.
 */

public class ExtraHelper {


    public static String getCurrentTime(){
        String currentTime=null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = df.format(c.getTime());
// Now formattedDate have current date/time

        return currentTime;
    }
    public static int getIconId(String code){
        switch (code) {

            case "t01d": return R.drawable.t01d;
            case "t01n": return R.drawable.t01n;
            case "t02d": return R.drawable.t02d;
            case "t02n": return R.drawable.t02n;
            case "t03d": return R.drawable.t03d;
            case "t03n": return R.drawable.t03n;
            case "t04d": return R.drawable.t04d;
            case "t04n": return R.drawable.t04n;
            case "t05d": return R.drawable.t05d;
            case "t05n": return R.drawable.t05n;
            case "d01d": return R.drawable.d01d;
            case "d01n": return R.drawable.d01n;
            case "d02d": return R.drawable.d02d;
            case "d02n": return R.drawable.d02n;
            case "d03d": return R.drawable.d03d;
            case "d03n": return R.drawable.d03n;
            case "r01d": return R.drawable.r01d;
            case "r01n": return R.drawable.r01n;
            case "r02d": return R.drawable.r02d;
            case "r02n": return R.drawable.r02n;
            case "r03d": return R.drawable.r03d;
            case "r03n": return R.drawable.r03n;
            case "f01d": return R.drawable.f01d;
            case "f01n": return R.drawable.f01n;
            case "r04d": return R.drawable.r04d;
            case "r04n": return R.drawable.r04n;
            case "r05d": return R.drawable.r05d;
            case "r05n": return R.drawable.r05n;
            case "r06d": return R.drawable.r06d;
            case "r06n": return R.drawable.r06n;
            case "s01d": return R.drawable.s01d;
            case "s01n": return R.drawable.s01n;
            case "c02d": return R.drawable.c02d;
            case "c02n": return R.drawable.c02n;
        default:
            return R.drawable.wind_icon;

    }
    }
}
