package com.example.dell.tourassistant.PlacePackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment {


    ArrayList<SinglePlace> placeList;
    ListView placeLV;
    String searchCatagory;

    public PlaceFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_place, container, false);

        placeList= getArguments().getParcelableArrayList("placelist");
        searchCatagory= getArguments().getString("catagory",null);
        Toast.makeText(getActivity(), ""+searchCatagory, Toast.LENGTH_SHORT).show();

        int sz=placeList.size();
        Toast.makeText(getActivity(), ""+sz, Toast.LENGTH_SHORT).show();

        Log.d("placeListSize",String.valueOf(sz));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

        placeLV= (ListView) getActivity().findViewById(R.id.showPlaces);
        int  i = ExtraHelper.getCatagoryIcon(searchCatagory);
        PlaceAdapter placeAdapter= new PlaceAdapter(getActivity(),placeList, i);
        placeLV.setAdapter(placeAdapter);
    }
}
