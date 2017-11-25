package com.example.dell.tourassistant.CombinedWeather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dell.tourassistant.CombinedWeather.DailyWeather.CustomDailyWeather;
import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyForecastAdapter;
import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyWeather;
import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyWeatherClient;
import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.CustomHourlyWeather;
import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.Datum;
import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.HourlyForecastAdapter;
import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.HourlyWeather;
import com.example.dell.tourassistant.CombinedWeather.HourlyWeather.HourlyWeatherClient;
import com.example.dell.tourassistant.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {


    public ForecastFragment() {
        // Required empty public constructor
    }


    private double lat,lon;
    //private List<Datum> hourlyDataList;
   // private List<com.example.dell.tourassistant.CombinedWeather.DailyWeather.Datum> dailyDataList;
    private RecyclerView hourlyRV;
    private ListView dailyLV;
    private ArrayList<CustomDailyWeather> dailyDatalist;
    private ArrayList<CustomHourlyWeather> hourlyDataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forecast, container, false);
        hourlyRV = (RecyclerView) view.findViewById(R.id.show_forecast_per_hour);
        dailyLV = (ListView) view.findViewById(R.id.show_forecast_per_day);

        Bundle bundle = getArguments();
        dailyDatalist = bundle.getParcelableArrayList("dailydatalist");
        hourlyDataList = bundle.getParcelableArrayList("hourlydatalist");


//        Log.d("ForecastDaily","daily data list size is: "+dailyDatalist.size());

        DailyForecastAdapter dailyAdapter = new DailyForecastAdapter(getActivity(),dailyDatalist);
        dailyLV.setAdapter(dailyAdapter);

        HourlyForecastAdapter hourlyForecastAdapter = new HourlyForecastAdapter(getActivity(),hourlyDataList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyRV.setLayoutManager(llm);
        hourlyRV.setAdapter(hourlyForecastAdapter);

        return view;
    }

}
