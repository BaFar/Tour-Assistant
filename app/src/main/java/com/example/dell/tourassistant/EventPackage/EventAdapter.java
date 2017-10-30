package com.example.dell.tourassistant.EventPackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dell.tourassistant.R;

import java.util.ArrayList;

/**
 * Created by DELL on 10/26/2017.
 */

public class EventAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> eventList;


    public EventAdapter(@NonNull Context context, ArrayList<Event> eventList) {
        super(context, R.layout.single_event_row, eventList);
        this.context = context;
        this.eventList = eventList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.single_event_row,parent,false);

        TextView destinationTV,fromDateTV,todateTV,budgetTV;

        destinationTV = (TextView) convertView.findViewById(R.id.event_destination_name);
        fromDateTV = (TextView) convertView.findViewById(R.id.event_from_date);
        todateTV = (TextView) convertView.findViewById(R.id.event_to_date);
        budgetTV = (TextView) convertView.findViewById(R.id.event_budget);

        destinationTV.setText(eventList.get(position).getDestination());
        fromDateTV.setText(eventList.get(position).getFromDate());
        todateTV.setText(eventList.get(position).getToDate());
        budgetTV.setText(String.valueOf(eventList.get(position).getBudget()));
        return convertView;
    }
}
