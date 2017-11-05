package com.example.dell.tourassistant.CombinedWeather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.CurrentWeather;
import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.CurrentWeatherClient;
import com.example.dell.tourassistant.CombinedWeather.CurrentWeatherPackage.Datum;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherHomeFragment extends Fragment {


    public WeatherHomeFragment() {
        // Required empty public constructor
    }
    private TextView currentTempTV,weatherTypeTV,sunsetTV,sunriseTV,cityNameTV,dateTimeTV;
    private ImageView minTempIV,maxTempIV,weatherTypeIV;

    String lowTemp;
    String highTemp;
    String weatherCode;
    String cityName;
    String chill;
    String speed;
    String humidity;
    String rising;
    String pressure;

    String sunrise;
    String sunset;
    String weatherText;
    String pubdate;
    String weatherType;
    String iconeCode;
    String dateTime;
    private CurrentWeather weather;
    private Double windspeed,visibility,temp,lat,lon;
    private DetailsInterface dInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_weather_home, container, false);

         dInterface = (DetailsInterface) getActivity();
        lat = 90.4786;
        lon = 23.81435;
        try{

            lat=getArguments().getDouble("lattitude");
            lon = getArguments().getDouble("longitude");
        }
        catch (Exception e){
            Log.e("whf","no values");
        }


        currentTempTV = (TextView)view.findViewById(R.id.show_current_temp);
        sunsetTV = (TextView) view.findViewById(R.id.show_sunset);
        sunriseTV = (TextView) view.findViewById(R.id.show_sunrise);
        cityNameTV = (TextView) view.findViewById(R.id.show_place_name);
        dateTimeTV = (TextView) view.findViewById(R.id.show_date_time);
        weatherTypeTV = (TextView) view.findViewById(R.id.show_current_weather_type_text);
        weatherTypeIV = (ImageView) view.findViewById(R.id.show_current_weather_type_icon);


        weather = new CurrentWeather();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        collectCurrentWeather(lat,lon);

    }

    private void collectCurrentWeather(double lat, double lon) {
        String subUrl = "currently?key=21580262673342e28e1c87639965a4e8&lat="+lat+"&lon="+lon;
        CurrentWeatherClient client = CurrentWeatherClient.currentRetrofitClient.create(CurrentWeatherClient.class);
        Call<CurrentWeather> cwCall = client.getCurrentWeather(subUrl);
        cwCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {

                if(response.code()==200){
                    Toast.makeText(getActivity(), "200 OK", Toast.LENGTH_SHORT).show();
                    weather = response.body();
                    setDatatoView(weather);

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
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("CurrentWeather",t.getMessage());
            }
        });
    }

    private void setDatatoView(CurrentWeather weather) {

        temp = 8.0;
        Datum datum = weather.getData().get(0);
        cityName = datum.getCityName();
        dateTime = datum.getDatetime();
        sunrise = datum.getSunrise();
        sunset = datum.getSunset();
        temp = datum.getTemp();
        windspeed = datum.getWindSpd();
        visibility= datum.getVis();
        weatherCode = datum.getWeather().getCode();
        iconeCode = datum.getWeather().getIcon();
        weatherType = datum.getWeather().getDescription();
        int weatherIconCode = ExtraHelper.getIconId(iconeCode);

        cityNameTV.setText(cityName);
        dateTimeTV.setText(dateTime);
        sunriseTV.setText(sunrise);
        sunsetTV.setText(sunset);
        currentTempTV.setText(String.valueOf(temp)+(char)0x00B0);
        weatherTypeTV.setText(weatherType);
        weatherTypeIV.setImageResource(weatherIconCode);



        dInterface.detailsContainer(cityName,temp,dateTime);

    }



    public interface DetailsInterface{
         void detailsContainer(String cityName, double temp, String dateTime);
    }
}