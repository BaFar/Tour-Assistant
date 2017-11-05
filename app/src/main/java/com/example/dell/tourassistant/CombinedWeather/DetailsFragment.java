package com.example.dell.tourassistant.CombinedWeather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dell.tourassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }

    private String cityName,dateTime;
    private double temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_details, container, false);
        Toast.makeText(getActivity(), "In deatils fragment", Toast.LENGTH_LONG).show();
        Bundle rBundle = getArguments();
        cityName = rBundle.getString("cityname");
        dateTime = rBundle.getString("datetime");
        temp = rBundle.getDouble("temperature");
        Toast.makeText(getActivity(), ""+cityName+"\n"+dateTime+"\n"+temp, Toast.LENGTH_SHORT).show();

        return v;
    }

}
