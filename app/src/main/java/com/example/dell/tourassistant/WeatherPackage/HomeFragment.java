package com.example.dell.tourassistant.WeatherPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dell.tourassistant.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener{


    private NearByListener nearByListener;

   private WeatherApi weatherApi;
    private TextView tempTV, placeNameTV, weatherTypeTV;
    private ImageView weatherIconIV;
    private Button foodBtn,resturantBtn,hotelBtn,hospitalBtn,atmBtn,policeBtn;
    private String currentLat=null;
    private String currentLon=null;
   // private double currentLon=0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String value= getArguments().getString("msg",null);
        Toast.makeText(getActivity(), ""+value, Toast.LENGTH_SHORT).show();


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getActivity().getSharedPreferences("coordinate",Context.MODE_PRIVATE);
        currentLat= preferences.getString("currentLat",null);
        currentLon = preferences.getString("currentLon",null);


        tempTV = (TextView) getActivity().findViewById(R.id.showTemparature);
        placeNameTV = (TextView) getActivity().findViewById(R.id.placeName);
        weatherTypeTV = (TextView) getActivity().findViewById(R.id.weatherType);
        weatherIconIV = (ImageView) getActivity().findViewById(R.id.weatherIcon);
        foodBtn = (Button) getActivity().findViewById(R.id.catagoryFood);
        resturantBtn = (Button) getActivity().findViewById(R.id.catagoryResturant);
        hotelBtn = (Button) getActivity().findViewById(R.id.catagoryHotel);
        hospitalBtn = (Button) getActivity().findViewById(R.id.catagoryHospital);
        atmBtn = (Button) getActivity().findViewById(R.id.catagoryATM);
        policeBtn = (Button) getActivity().findViewById(R.id.catagoryPolice);

        foodBtn.setOnClickListener(this);
        resturantBtn.setOnClickListener(this);
        hotelBtn.setOnClickListener(this);
        hospitalBtn.setOnClickListener(this);
        atmBtn.setOnClickListener(this);
        policeBtn.setOnClickListener(this);
        collectCurrentWeather();

    }


    @Override
    public void onPause() {

        super.onPause();
    }

    private void collectCurrentWeather() {

        String lat="23.4825";
        String lon = "90.8723";
        if (currentLat!=null && currentLon!=null){
            lat = String.valueOf(currentLat);
            lon = String.valueOf(currentLon);
        }

        String url = "current?&lat="+lat+"&lon="+lon+"&key=21580262673342e28e1c87639965a4e8";
        weatherApi = RetrofitWeatherClient.getRetrofitClient().create(WeatherApi.class);
        Call<Wether> weatherCall = weatherApi.getWeatherInfo(url);
        weatherCall.enqueue(new Callback<Wether>() {
            @Override
            public void onResponse(Call<Wether> call, Response<Wether> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Data receivec from weather", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "" + response.body().getData().get(0).getCityName(), Toast.LENGTH_SHORT).show();
                    Datum weatherData = response.body().getData().get(0);

                    String placeName = weatherData.getCityName();
                    double temp = weatherData.getTemp();
                    Weather weather = weatherData.getWeather();
                    String weatherTypeCode = weather.getCode();
                    String weatherType = weather.getDescription();

                    weatherIconIV.setImageResource(getIcon(weatherTypeCode));
                    placeNameTV.setText(placeName);
                    weatherTypeTV.setText(weatherType);
                    tempTV.setText(String.valueOf(temp) + (char) 0x00B0);


                } else {
                    Toast.makeText(getActivity(), "bed response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Wether> call, Throwable t) {
                Log.e("call", "" + t.getMessage());
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {


        String catagory=null;
        switch (v.getId()){

            case R.id.catagoryFood:
                catagory = "food";
                Log.d("click","clicked");
                break;
            case R.id.catagoryResturant:
                catagory = "resturant";
                Log.d("click","clicked");
                break;
            case R.id.catagoryHotel:
                catagory = "hotel";
                Log.d("click","clicked");
                break;
            case R.id.catagoryHospital:
                catagory = "hospital";
                Log.d("click","clicked");
                break;
            case R.id.catagoryATM:
                catagory = "atm";
                Log.d("click","clicked");
                break;
            case R.id.catagoryPolice:
                catagory = "police";
                Log.d("click","clicked");
                break;
            default:
                Toast.makeText(getActivity(), "UnKnown Event Clicked", Toast.LENGTH_SHORT).show();
        }

        nearByListener.showNearBy(catagory);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nearByListener = (NearByListener) context;

    }

    public interface NearByListener{
         void showNearBy(String catagory);
    }


    public int getIcon(String iconCode) {
        int code = 0;
        switch (iconCode) {
            case "200":
                code = R.drawable.t01d;
                break;
            case "201":
                code = R.drawable.t02d;
                break;
            case "202":
                code = R.drawable.t03d;
                break;
            case "230":
            case "231":
            case "232":
                code = R.drawable.t04d;
                break;
            case "233":
                code = R.drawable.t05d;
                break;
            case "300":
                code = R.drawable.d01d;
                break;
            case "301":
                code = R.drawable.d02d;
                break;
            case "302":
                code = R.drawable.d03d;
                break;
            case "500":
                code = R.drawable.r01d;
                break;
            case "501":
                code = R.drawable.r02d;
                break;
            case "502":
                code = R.drawable.r03d;
                break;
            case "511":
                code = R.drawable.f01d;
                break;
            default:
                code = R.drawable.c01n;
        }


        return code;

    }
}
