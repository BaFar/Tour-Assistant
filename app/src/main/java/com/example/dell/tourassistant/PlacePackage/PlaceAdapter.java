package com.example.dell.tourassistant.PlacePackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dell.tourassistant.R;

import java.util.ArrayList;

/**
 * Created by DELL on 10/13/2017.
 */

public class PlaceAdapter extends ArrayAdapter<SinglePlace>{

    private Context context;
    private ArrayList<SinglePlace>placeList;
    private int catagoryIconId;


    public PlaceAdapter(@NonNull Context context, ArrayList<SinglePlace> placeList,int catagoryIconId ){
        super(context, R.layout.single_place, placeList);
        this.context=context;
        this.placeList=placeList;
        this.catagoryIconId = catagoryIconId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.single_place,parent,false);
        TextView nameTV= (TextView) convertView.findViewById(R.id.show_name);
        TextView placeLocationTV= (TextView) convertView.findViewById(R.id.show_place_location);
        TextView distanceTV= (TextView) convertView.findViewById(R.id.show_distance);
        ImageView iconIV = (ImageView) convertView.findViewById(R.id.show_catagory_icon);
        nameTV.setText(placeList.get(position).getName());
        placeLocationTV.setText(placeList.get(position).getPlaceLocation());
        distanceTV.setText(String.valueOf(placeList.get(position).getDistance())+"m");

        iconIV.setImageResource(catagoryIconId);

        return convertView;
    }
}
