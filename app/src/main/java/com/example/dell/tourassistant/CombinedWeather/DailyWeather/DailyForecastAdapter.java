package com.example.dell.tourassistant.CombinedWeather.DailyWeather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.CustomHourlyWeather;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 11/1/2017.
 */

public class DailyForecastAdapter extends ArrayAdapter<CustomDailyWeather>{
    private Context context;
    private List<CustomDailyWeather> dailyDataList;


    public DailyForecastAdapter(@NonNull Context context, List<CustomDailyWeather> dailyDataList) {
        super(context, R.layout.single_day_update, dailyDataList);

        this.context = context;
        this.dailyDataList = dailyDataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.single_day_update,parent,false);

        TextView dayTV,highTempTV,lowTempTV;
        ImageView weatherIconIV;
        String maxtemp,mintemp;
         dayTV = (TextView) convertView.findViewById(R.id.show_day);
         highTempTV = (TextView) convertView.findViewById(R.id.show_high_temp);
         lowTempTV = (TextView) convertView.findViewById(R.id.show_low_temp);
        weatherIconIV = (ImageView) convertView.findViewById(R.id.show_weather_icon);

        String input = dailyDataList.get(position).getDate();
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");//dd-MM-yyyy
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //CustomDailyWeather dweather = new CustomDailyWeather();


        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String goal = outFormat.format(date);

        maxtemp = String.valueOf(dailyDataList.get(position).getMaxTemp());
        mintemp = String.valueOf(dailyDataList.get(position).getMinTemp());

        dayTV.setText(goal+"\n"+dailyDataList.get(position).getDate());
        highTempTV.setText(maxtemp+(char)0x00B0);               /*add unit later*/
        lowTempTV.setText(mintemp+(char)0x00B0);
        int iconCode = ExtraHelper.getIconId(dailyDataList.get(position).getIconCode());
        weatherIconIV.setImageResource(iconCode);
        return convertView;


    }


}
