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

import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyForecastAdapter;
import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyWeather;
import com.example.dell.tourassistant.CombinedWeather.DailyWeather.DailyWeatherClient;
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
    private List<Datum> hourlyDataList;
    private List<com.example.dell.tourassistant.CombinedWeather.DailyWeather.Datum> dailyDataList;
    private RecyclerView hourlyRV;
    private ListView dailyLV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forecast, container, false);
        hourlyRV = (RecyclerView) view.findViewById(R.id.show_forecast_per_hour);
        dailyLV = (ListView) view.findViewById(R.id.show_forecast_per_day);

        hourlyDataList = new ArrayList<>();
        dailyDataList = new ArrayList<>();

        lat = getArguments().getDouble("lattitude");
        lon = getArguments().getDouble("longitude");
        Toast.makeText(getActivity(), ""+lat+"\n"+lon, Toast.LENGTH_SHORT).show();

        collectHourlyWeather(lat,lon);
        collectDailyWeather(lat,lon);
        return view;
    }

    private void collectDailyWeather(double lat, double lon) {
        /*https://api.weatherbit.io/v2.0/forecast/daily?key=21580262673342e28e1c87639965a4e8&lat=38.123&lon=-78.543*/
        /*https://api.weatherbit.io/v2.0/forecast/daily?key=21580262673342e28e1c87639965a4e8&lat=22.5726&lon=88.3639*/
        String subUrl = "daily?key=21580262673342e28e1c87639965a4e8&lat="+lat+"&lon="+lon;
        DailyWeatherClient dailyWeatherClient = DailyWeatherClient.dailyRetrofitClient.create(DailyWeatherClient.class);
        Call<DailyWeather> dwCall = dailyWeatherClient.getDailyWeather(subUrl);
        dwCall.enqueue(new Callback<DailyWeather>() {
            @Override
            public void onResponse(Call<DailyWeather> call, Response<DailyWeather> response) {
                if(response.code()==200){
                    Toast.makeText(getActivity(), "200 OK", Toast.LENGTH_SHORT).show();
                    dailyDataList =  response.body().getData();
                  //  Toast.makeText(getActivity(), "Daily size: "+dailyDataList.size(), Toast.LENGTH_SHORT).show();
                   // Toast.makeText(getActivity(), ""+response.body().getCityName(), Toast.LENGTH_SHORT).show();
                    DailyForecastAdapter dailyAdapter = new DailyForecastAdapter(getActivity(),dailyDataList);
                    dailyLV.setAdapter(dailyAdapter);


                }
                else if(response.code()==304){
                    Toast.makeText(getActivity(), "304 Not Modified", Toast.LENGTH_SHORT).show();

                }else if(response.code()==400){
                    Toast.makeText(getActivity(), "400 Bed Request", Toast.LENGTH_SHORT).show();

                }
                else if(response.code()==401){
                    Toast.makeText(getActivity(), "401 Unauthorised", Toast.LENGTH_SHORT).show();

                }else if(response.code()==403){
                    Toast.makeText(getActivity(), "403 Forbidden", Toast.LENGTH_SHORT).show();

                }else if(response.code()==404){
                    Toast.makeText(getActivity(), "404 Not Found", Toast.LENGTH_SHORT).show();

                }else if(response.code()==409){
                    Toast.makeText(getActivity(), "409 Conflict", Toast.LENGTH_SHORT).show();

                }else if(response.code()==500){
                    Toast.makeText(getActivity(), "500 Internal Servar Error", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DailyWeather> call, Throwable t) {

                Log.d("forecast","DailyCallFailed");
                Log.d("forecast D",""+t.getMessage());
            }
        });
    }

    private void collectHourlyWeather(double lat, double lon) {

        String subUrl = "hourly?key=21580262673342e28e1c87639965a4e8&lat="+lat+"&lon="+lon;
        HourlyWeatherClient hourlyWeatherClient= HourlyWeatherClient.hourlyRetrofitClient.create(HourlyWeatherClient.class);
        Call<HourlyWeather> hwCall = hourlyWeatherClient.getHourlyWeather(subUrl);
        hwCall.enqueue(new Callback<HourlyWeather>() {
            @Override
            public void onResponse(Call<HourlyWeather> call, Response<HourlyWeather> response) {

                if(response.code()==200){
                    Toast.makeText(getActivity(), "200 OK", Toast.LENGTH_SHORT).show();
                    hourlyDataList = response.body().getData();

                  //  Toast.makeText(getActivity(), ""+response.body().getCityName(), Toast.LENGTH_SHORT).show();

                    HourlyForecastAdapter hourlyForecastAdapter = new HourlyForecastAdapter(getActivity(),hourlyDataList);

                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    hourlyRV.setLayoutManager(llm);
                    hourlyRV.setAdapter(hourlyForecastAdapter);
                }
                else if(response.code()==304){
                    Toast.makeText(getActivity(), "304 Not Modified", Toast.LENGTH_SHORT).show();

                }else if(response.code()==400){
                    Toast.makeText(getActivity(), "400 Bed Request", Toast.LENGTH_SHORT).show();

                }
                else if(response.code()==401){
                    Toast.makeText(getActivity(), "401 Unauthorised", Toast.LENGTH_SHORT).show();

                }else if(response.code()==403){
                    Toast.makeText(getActivity(), "403 Forbidden", Toast.LENGTH_SHORT).show();

                }else if(response.code()==404){
                    Toast.makeText(getActivity(), "404 Not Found", Toast.LENGTH_SHORT).show();

                }else if(response.code()==409){
                    Toast.makeText(getActivity(), "409 Conflict", Toast.LENGTH_SHORT).show();

                }else if(response.code()==500){
                    Toast.makeText(getActivity(), "500 Internal Servar Error", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<HourlyWeather> call, Throwable t) {

                Log.d("forecast","HourlyCallFailed");
                Log.d("forecast H",""+t.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Toast.makeText(getActivity(), "Adapter called", Toast.LENGTH_SHORT).show();
        Log.d("forecast","Adapter called");



    }
}
