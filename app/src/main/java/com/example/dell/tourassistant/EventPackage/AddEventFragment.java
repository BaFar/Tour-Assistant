package com.example.dell.tourassistant.EventPackage;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.ConnectivityReceiver;
import com.example.dell.tourassistant.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment implements View.OnClickListener{




    private CreateEvent createEvent;

    private TextView destinationTV,budgetTV,fromDateTV,toDateTV;
    private EditText destinationET,budgetET;
    private Button createBtn,fromDateBtn,toDateBtn;
    private String destination,fdate,tdate,placeName;
    private int budget;
    private  double lat,lon;
    private Calendar calendar;
    private int year,month,day;
    final int PLACE_PICKER_REQUEST_CODE= 1;
    long millis;


    public AddEventFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);


        destinationET = (EditText) view.findViewById(R.id.destinationValue);
        budgetET = (EditText) view.findViewById(R.id.budgetValue);
        fromDateBtn = (Button) view.findViewById(R.id.fromDateValue);
        toDateBtn = (Button) view.findViewById(R.id.toDateValue);
        createBtn = (Button) view.findViewById(R.id.eventCreateBtn);

        fromDateBtn.setOnClickListener(this);
        toDateBtn.setOnClickListener(this);
        createBtn.setOnClickListener(this);

        calendar =  Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        destinationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectivityReceiver.isConnected()){
                    Toast.makeText(getActivity(), "Opps! No internet Connection. First check internet connection, please!", Toast.LENGTH_LONG).show();
                    return;
                }

                selectPlace(v);
               // destinationET.setText(placeName);
            }
        });
        destinationET.setText("Click Here");
        toDateBtn.setEnabled(false);

        return view;

    }
    public void selectPlace(View view) {


        try {
            new PlacePicker.IntentBuilder().build(getActivity());

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable");
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            Log.d("exception","repairable");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String TAG ="on result back";
        if (requestCode == PLACE_PICKER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                placeName = String.valueOf(place.getName());
                destinationET.setText(placeName);
                LatLng loc= place.getLatLng();
                lat = loc.latitude;
                lon = loc.longitude;


            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Status status = PlacePicker.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onClick(View v) {

        DatePickerDialog pickerDialog;
        switch (v.getId()){

            case R.id.fromDateValue:
                pickerDialog = new DatePickerDialog(getActivity(),onFormDateSelectedListener,year,month,day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

                pickerDialog.show();

                break;
            case R.id.toDateValue:
                pickerDialog = new DatePickerDialog(getActivity(),onToDateSelectedListener,year,month,day);
              //  pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                pickerDialog.getDatePicker().setMinDate(millis);
                pickerDialog.show();
                break;
            case R.id.eventCreateBtn:
                if (!ConnectivityReceiver.isConnected()){
                    Toast.makeText(getActivity(), "Opps! No internet Connection. First check internet connection, please!", Toast.LENGTH_LONG).show();
                    return;
                }

                destination = destinationET.getText().toString();
                //destination = placeName;
               //budget = Integer.parseInt(budgetET.getText().toString()); /*imply condition*/
                String budgetStr = budgetET.getText().toString();
                boolean isOK=true;
                budget = -5;
                try{
                budget = Integer.parseInt(budgetStr);
            }
                catch (Exception e){
                    isOK = false;
                }
                Toast.makeText(getActivity(), "D: "+destination+"\nB:"+budgetStr+"\nf:"+fdate+"\nt:"+tdate, Toast.LENGTH_SHORT).show();
                if (destination==null || destination=="Click Here" || fdate ==null || tdate == null){

                    Toast.makeText(getActivity(), "Fill All field with valid information", Toast.LENGTH_LONG).show();
                }
                else if(!isOK || budget <= 0){
                    Toast.makeText(getActivity(), "Set valid Budget", Toast.LENGTH_LONG).show();
                }
                else {

                    createEvent.createEventClicked(destination,budget,fdate,tdate,lat,lon);
                    toDateBtn.setEnabled(false);
                    destinationET.setText("Click Here");
                    fromDateBtn.setText("");
                    toDateBtn.setText("");
                }


                break;

        }
    }

    private DatePickerDialog.OnDateSetListener onFormDateSelectedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            calendar.set(year,month,dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
             Date dt = calendar.getTime();
            fdate = dateFormat.format(calendar.getTime());
             millis = dt.getTime();
            fromDateBtn.setText(fdate);
            toDateBtn.setEnabled(true);

        }
    };
    private DatePickerDialog.OnDateSetListener onToDateSelectedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year,month,dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tdate = dateFormat.format(calendar.getTime());
            toDateBtn.setText(tdate);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        createEvent = (CreateEvent) context;
    }

    public interface CreateEvent{
        void createEventClicked(String destination, int budget, String fromDate, String toDate,double lattitude,double logitude);
    }
}














/*
* destinationTV = (TextView) view.findViewById(R.id.destinationText);
        budgetTV = (TextView) view.findViewById(R.id.budgetText);
        fromDateTV = (TextView) view.findViewById(R.id.fromDateTV);
        toDateTV = (TextView) view.findViewById(R.id.toDateText);
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
* */