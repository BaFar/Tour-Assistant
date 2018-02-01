package com.example.dell.tourassistant.EventPackage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.tourassistant.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastEventFragment extends Fragment {

    private ArrayList<Event> eventList = new ArrayList<>();
    private ListView pastEventLV;
    private TextView headingTV;
    private  int noOfEvents=0;
    public PastEventFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_past_event, container, false);

        headingTV = (TextView) v.findViewById(R.id .past_events_heading);
        pastEventLV = (ListView) v.findViewById(R.id.past_event_LV);
        eventList = new ArrayList<Event>();

        eventList = getArguments().getParcelableArrayList("past_ events");
        noOfEvents = eventList.size();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (noOfEvents == 0){
            headingTV.setText(R.string.no_past_event);
            return;
        }

        EventAdapter eventAdapter = new EventAdapter(getActivity(),eventList);
        pastEventLV.setAdapter(eventAdapter);
        pastEventLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SingleEventFragment fragment = new SingleEventFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("single event", eventList.get(position));
                bundle.putInt("event position", position);
                bundle.putString("pastEventOrNOt","past");
                fragment.setArguments(bundle);
                ft.replace(R.id.eventFragmentContainer, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


    }
}
