package com.example.dell.tourassistant.PlacePackage;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dell.tourassistant.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener,HomeFragment.NearByListener{

    private Intent intent;
    private double deslat;
    private double deslon;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double currentLat=-33.8670522;
    private double currentLon=151.1957362;
    private SharedPreferences preferences;
    private PlaceApi placeApi;

    private ArrayList<SinglePlace> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        try{
            intent = getIntent();
            deslat = intent.getDoubleExtra("des lattitude",0);
            deslon = intent.getDoubleExtra("des longitude",0);

        }
        catch (Exception e){
            Log.d("place intent","Did not find intent argument");
        }


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        preferences = getSharedPreferences("coordinate",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("desLat", (float) deslat);
        editor.putFloat("desLon", (float) deslon);
        editor.apply();
        editor.commit();
        //editor.putString("currentLat",String.valueOf(currentLat)).apply();
        //preferences.edit().putString("currentLat",String.valueOf(currentLat)).apply();

        HomeFragment fragment = new HomeFragment();
        Bundle bundle= new Bundle();
        bundle.putString("msg","Welcome message");

        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.place_fragment_container, fragment);
        ft.commit();
        ft.addToBackStack("HomeFragment");


        placeList=new ArrayList<SinglePlace>();

    }
    public void collectPlaceInfo(final String searchCatagory){

        String lat= String.valueOf(preferences.getFloat("desLat",0));
        String lon = String.valueOf(preferences.getFloat("desLon",0));;
        Log.d("placeCallLatLon",""+lat+"\n" +lon);
        String radius="2000";

        String subUrl="json?location="+lat+","+lon+"&radius="+radius+"&type="+searchCatagory+"&key=AIzaSyAdt3XwQPIy5mKg3FrqyLaabVNd0gehToQ";
        // String subUrl = "json?location=-33.8670522,151.1957362&radius=500&type=food&key=AIzaSyAdt3XwQPIy5mKg3FrqyLaabVNd0gehToQ";
        placeApi = RetrofitPlaceClient.getRetrofitPlaceClient().create(PlaceApi.class);
        Call<Places> placesCall=placeApi.getPlaceInfo(subUrl);
        placesCall.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                if (response.code()==200){

                    int sz= response.body().getResults().size();
                    Toast.makeText(PlaceActivity.this, "size: "+sz, Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(MainActivity.this, ""+result.getName(), Toast.LENGTH_SHORT).show();
                    String name=null,placeLocatin=null,id=null;
                    double distance= 0,placeLat=0,placeLon=0,rating=0;
                    boolean isOpen=false;
                    int nearByType=1;
                    placeList.clear();

                    Location A = new Location("cureent place");
                    A.setLatitude(currentLat);
                    A.setLongitude(currentLon);
                    Location B= new Location("Destination place");

                    for(int i=0; i<sz; i++){
                        Result result = response.body().getResults().get(i);
                        name=result.getName();
                        placeLocatin = result.getVicinity();
                        id = result.getId();
                        // isOpen=result.getOpeningHours().getOpenNow();
                        placeLat=result.getGeometry().getLocation().getLat();
                        placeLon=result.getGeometry().getLocation().getLng();
                        B.setLatitude(placeLat);
                        B.setLongitude(placeLon);
                        distance = A.distanceTo(B);
                        try {
                            isOpen=result.getOpeningHours().getOpenNow();

                        }catch (Exception e){
                            isOpen=false;

                        }
                        try {
                            rating=result.getRating();
                        }
                        catch (Exception e){
                            rating=0;
                        }

                        SinglePlace place=new SinglePlace(name,placeLocatin,id,distance,isOpen,rating,placeLat,placeLon);


                        placeList.add(place);
                    }
                    gotoPlaceFragment(searchCatagory);

                }
                else {
                    Toast.makeText(PlaceActivity.this, "Bad result", Toast.LENGTH_SHORT).show();
                    Log.d("response","bad response");


                }
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {

                Log.e("response","call failed");
                Log.d("response",""+t.getMessage());
            }
        });

    }

    private void gotoPlaceFragment(String searchCatagory) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("placelist",placeList);

        bundle.putString("catagory",searchCatagory);
        PlaceFragment placeFragment= new PlaceFragment();
        placeFragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.place_fragment_container,placeFragment);
        ft.commit();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest().setInterval(10000).setFastestInterval(5000).setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Toast.makeText(this, " Location permission missing", Toast.LENGTH_SHORT).show();
            Log.d("permission","No Location permission");
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "connection suspended", Toast.LENGTH_SHORT).show();
        Log.d("connection","connection suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
        Log.d("connection","connection failed");
    }

    @Override
    public void onLocationChanged(Location location) {

        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
        Toast.makeText(this, "Location Changed and received", Toast.LENGTH_SHORT).show();

        preferences.edit().putString("currentLat",String.valueOf(currentLat)).commit();
        preferences.edit().putString("currentLat",String.valueOf(currentLat)).commit();
        //   Latpreferences.edit().putString("currentLat",String.valueOf(currentLat)).commit();
        //  SharedPreferences  LonPreference= getSharedPreferences("LonCoordinate",MODE_PRIVATE);
        //  LonPreference.edit().putString("currentLon",String.valueOf(currentLon)).commit();
    }

    @Override
    public void showNearBy(String catagory) {
        collectPlaceInfo(catagory);
    }


    public void gotoMap(View view) {


        Bundle bundle= new Bundle();
        bundle.putParcelableArrayList("placeListToMap",placeList);

        MapFragment mapFragment=new MapFragment();

        mapFragment.setArguments(bundle);
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.place_fragment_container,mapFragment);
        ft.addToBackStack("map");
        ft.commit();


    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }


}
