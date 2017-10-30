package com.example.dell.tourassistant.EventPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.tourassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastEventFragment extends Fragment {


    public PastEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_event, container, false);
    }

}
