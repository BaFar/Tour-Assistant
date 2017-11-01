package com.example.dell.tourassistant.CombinedWeather.HourlyWeather;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;

import java.util.List;

/**
 * Created by DELL on 11/1/2017.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.HourlyDataViewHolder> {
    private Context context;
    private List<Datum> hourlyDataList;
    public HourlyForecastAdapter(@NonNull Context context,List<Datum> hourlyDataList) {
       this.context = context;
        this.hourlyDataList = hourlyDataList;
    }

    @Override
    public HourlyDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_hour_update,parent,false);
        HourlyDataViewHolder hourlyDataViewHolder =  new HourlyDataViewHolder(view);

        return hourlyDataViewHolder;
    }

    @Override
    public void onBindViewHolder(HourlyDataViewHolder holder, int position) {
       holder.tempTV.setText(String.valueOf(hourlyDataList.get(position).getTemp())+(char)0x00B0+"C");
        holder.timeTV.setText(hourlyDataList.get(position).getDatetime());
        holder.iconIV.setImageResource(ExtraHelper.getIconId(hourlyDataList.get(position).getWeather().getIcon()));
    }


    @Override
    public int getItemCount() {
        return hourlyDataList.size();
    }

    public class HourlyDataViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIV;
        TextView timeTV;
        TextView tempTV;
        public HourlyDataViewHolder(View itemView) {
            super(itemView);
            timeTV= (TextView) itemView.findViewById(R.id.show_time);
            iconIV = (ImageView) itemView.findViewById(R.id.show_weatherType);
            tempTV = (TextView) itemView.findViewById(R.id.show_temperature);

        }
    }


}
