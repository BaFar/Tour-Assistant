package com.example.dell.tourassistant;

import android.util.Log;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by DELL on 10/26/2017.
 */

public class ExtraHelper {

   public static Calendar c = Calendar.getInstance();

    public static String getCurrentTime() {
        String currentTime = null;

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = df.format(c.getTime());// Now formattedDate have current date/time
        return currentTime;
    }
    public static boolean compareTwoDate(String endDate){

        boolean ispassed= false;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String cdate= df.format(c.getTime());
        Log.d("checkCurrentDate",cdate);
        Log.d("checkCurrentDate",endDate);
        Date currentDate = null;
        Date eDate = null;

        try {
             currentDate = df.parse(cdate);
             eDate = df.parse(endDate);
            if (eDate.compareTo(currentDate) < 0)    ispassed = true;

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("DateCompare","date compare failed: "+e.getMessage());
        }

        return ispassed;

    }

    public static int getIconId(String code) {

        switch (code) {
            case "t01d":
                return R.drawable.t01d;
            case "t01n":
                return R.drawable.t01n;
            case "t02d":
                return R.drawable.t02d;
            case "t02n":
                return R.drawable.t02n;
            case "t03d":
                return R.drawable.t03d;
            case "t03n":
                return R.drawable.t03n;
            case "t04d":
                return R.drawable.t04d;
            case "t04n":
                return R.drawable.t04n;
            case "t05d":
                return R.drawable.t05d;
            case "t05n":
                return R.drawable.t05n;
            case "d01d":
                return R.drawable.d01d;
            case "d01n":
                return R.drawable.d01n;
            case "d02d":
                return R.drawable.d02d;
            case "d02n":
                return R.drawable.d02n;
            case "d03d":
                return R.drawable.d03d;
            case "d03n":
                return R.drawable.d03n;
            case "r01d":
                return R.drawable.r01d;
            case "r01n":
                return R.drawable.r01n;
            case "r02d":
                return R.drawable.r02d;
            case "r02n":
                return R.drawable.r02n;
            case "r03d":
                return R.drawable.r03d;
            case "r03n":
                return R.drawable.r03n;
            case "f01d":
                return R.drawable.f01d;
            case "f01n":
                return R.drawable.f01n;
            case "r04d":
                return R.drawable.r04d;
            case "r04n":
                return R.drawable.r04n;
            case "r05d":
                return R.drawable.r05d;
            case "r05n":
                return R.drawable.r05n;
            case "r06d":
                return R.drawable.r06d;
            case "r06n":
                return R.drawable.r06n;
            case "s01d":
                return R.drawable.s01d;
            case "s01n":
                return R.drawable.s01n;
            case "s02d":
                return R.drawable.s02d;
            case "s02n":
                return R.drawable.s02n;
            case "s03d":
                return R.drawable.s03d;
            case "s03n":
                return R.drawable.s03n;
            case "s04d":
                return R.drawable.s04d;
            case "s04n":
                return R.drawable.s04n;
            case "s05d":
                return R.drawable.s05d;
            case "s05n":
                return R.drawable.s05n;
            case "s06d":
                return R.drawable.s06d;
            case "s06n":
                return R.drawable.s06n;
            case "a01d":
                return R.drawable.a01d;
            case "a01n":
                return R.drawable.a01n;
            case "a02d":
                return R.drawable.a02d;
            case "a02n":
                return R.drawable.a02n;
            case "a03d":
                return R.drawable.a03d;
            case "a03n":
                return R.drawable.a03n;
            case "a04d":
                return R.drawable.a04d;
            case "a04n":
                return R.drawable.a04n;
            case "a05d":
                return R.drawable.a05d;
            case "a05n":
                return R.drawable.a05n;
            case "a06d":
                return R.drawable.a06d;
            case "a06n":
                return R.drawable.a06n;
            case "c01d":
                return R.drawable.c01d;
            case "c01n":
                return R.drawable.c01n;
            case "c02d":
                return R.drawable.c02d;
            case "c02n":
                return R.drawable.c02n;
            case "c03d":
                return R.drawable.c03d;
            case "c03n":
                return R.drawable.c03n;
            case "c04d":
                return R.drawable.c04d;
            case "c04n":
                return R.drawable.c04n;
            case "u00d":
                return R.drawable.u00d;
            case "u00n":
                return R.drawable.u00n;
        }
        return R.mipmap.ic_launcher_round;
    }

    public static String getHour(String time) {
        time = "" + time.charAt(11) + time.charAt(12);
        int i = Integer.parseInt(time);
        i++;
        String timeSuf = "";
        if (i < 12) {
            timeSuf = "AM";
        } else if (i == 24) {
            timeSuf = "AM";
            i = 12;
        } else if (i == 12) {
            timeSuf = "PM";
        } else {
            timeSuf = "PM";
            i = i % 12;
        }


        time = String.valueOf(i) + timeSuf;
        return time;
    }

    public static int getCatagoryIcon(String catagory) {
        int icon;
        switch (catagory) {
            case "food":      icon   = R.drawable.icons8_hamburger_40; break;
            case "resturant": icon = R.drawable.icons8_restaurant_40; break;
            case "hotel":     icon = R.drawable.icons8_hotel_40; break;
            case "hospital":  icon = R.drawable.icons8_hospital_40; break;
            case "atm":       icon = R.drawable.icons8_atm_40; break;
            case "police":    icon = R.drawable.icons8_police_station_40; break;

            default: icon = R.mipmap.ic_launcher;
        }
        return icon;
    }
}
