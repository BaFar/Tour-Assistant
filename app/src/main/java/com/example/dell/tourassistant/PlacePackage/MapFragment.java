package com.example.dell.tourassistant.PlacePackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.tourassistant.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener {

    private MapView mapView;
    private Button gotoListBtn;

    private GoogleMap mGoogleMap;
    private ArrayList<SinglePlace>placelist;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        gotoListBtn = (Button) view.findViewById(R.id.listBtn);
        gotoListBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placelist=getArguments().getParcelableArrayList("placeListToMap");
        Toast.makeText(getActivity(), "received place list size "+placelist.size(), Toast.LENGTH_SHORT).show();
        mapView= (MapView) getActivity().findViewById(R.id.placeMap);

        if (mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap=googleMap;

        for (int i=0; i<placelist.size(); i++){

            LatLng latLng=new LatLng(placelist.get(i).getLat(),placelist.get(i).getLon());

            mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(placelist.get(i).getName()));
        }
        LatLng latLng=new LatLng(placelist.get(1).getLat(),placelist.get(1).getLon());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.listBtn:
                getFragmentManager().popBackStack();
                break;

        }
    }
}
/*        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng dhaka = new LatLng(23.8103, 90.4125);
        mMap.addMarker(new MarkerOptions().position(dhaka).title("Marker in Dhaka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
*/