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
}
