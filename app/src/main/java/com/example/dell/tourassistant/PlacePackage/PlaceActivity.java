package com.example.dell.tourassistant.PlacePackage;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dell.tourassistant.PermissionUtil;
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

public class PlaceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener,HomeFragment.NearByListener,PermissionUtil.PermissionAskListener{

    private Intent intent;
    private double deslat;
    private double deslon;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double currentLat;
    private double currentLon;
    private SharedPreferences preferences;
    private SharedPreferences userCurrentLocationPreference;
    private PlaceApi placeApi;

    private ArrayList<SinglePlace> placeList;

    private boolean isLocationGranted = false;

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

        userCurrentLocationPreference = getApplicationContext().getSharedPreferences("userCurrentLocationSharedPreference",MODE_PRIVATE);
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


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        Log.d("location","location api connected");
    }
    @Override
    protected void onPause() {
        googleApiClient.disconnect();
        Log.d("location","location api disconnected");
        super.onPause();
    }


    public void collectPlaceInfo(final String searchCatagory){

        String lat= String.valueOf(deslat);
        String lon = String.valueOf(deslon);;
        //Log.d("placeCallLatLon",""+lat+"\n" +lon);
        String radius="2000";

        String subUrl="json?location="+lat+","+lon+"&radius="+radius+"&type="+searchCatagory+"&key=AIzaSyAdt3XwQPIy5mKg3FrqyLaabVNd0gehToQ";
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
                    A.setLatitude(deslat);
                    A.setLongitude(deslat);
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
        ft.addToBackStack(null);
        ft.commit();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest().setInterval(15000).setFastestInterval(10000).setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        /*request location permission if missing*/
        PermissionUtil.checkPermission(this,Manifest.permission.ACCESS_FINE_LOCATION,this);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
       Log.d("location","current location requested");

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
        Toast.makeText(this, "Location received, lat: "+currentLat+" Lon: "+currentLon, Toast.LENGTH_SHORT).show();

        userCurrentLocationPreference.edit().putString("currentLat",String.valueOf(currentLat)).commit();
        userCurrentLocationPreference.edit().putString("currentLat",String.valueOf(currentLat)).commit();

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
        ft.addToBackStack(null);
        ft.commit();


    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public void onNeedPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PermissionUtil.ACCESS_FINE_LOCATION_REQUEST_CODE);
        Log.d("permission"," fine location requested: PlaceActivity");
    }

    @Override
    public void onPermissionPreviouslyDenied() {

        new AlertDialog.Builder(PlaceActivity.this)
                .setTitle("Need Location Permission")
                .setMessage("we must need location permission to find nearby places and measure distance.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(PlaceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PermissionUtil.ACCESS_FINE_LOCATION_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onPermissionDisabled() {
        new AlertDialog.Builder(this)
                .setTitle("Need Location Permission")
                .setMessage("We must need location permission to find nearby places and measure distance of them." +
                        "you can allow loaction from settings")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void onPermissionGranted() {
        isLocationGranted = true;

    }
}
/*
*  String subUrl = "json?location=-33.8670522,151.1957362&radius=500&type=food&key=AIzaSyAdt3XwQPIy5mKg3FrqyLaabVNd0gehToQ";
*
*
*
* */