package com.example.dell.tourassistant.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileViewFragment extends Fragment implements View.OnClickListener{

    private ImageView profileIV;
    private TextView nameTV,emailTV;
    private Button updateBtn;
    private UpdateListener updateListener;

    private String profilePhotoLocalUri=null;
    private String profilePhotoRemoteUri = null;
    private String userID = null;
    private String userName = null;
    private String userEmail = null;



    public ProfileViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_profile_view, container, false);

       Bundle bundle = getArguments();
       userID = bundle.getString("userID");
       userName = bundle.getString("userName");
       profilePhotoLocalUri = bundle.getString("photoLocalUri");
       profilePhotoRemoteUri = bundle.getString("photoRemoteUri");
       userEmail = bundle.getString("userEmail");

       profileIV = (ImageView) v.findViewById(R.id.profile_picture);
       nameTV = (TextView) v.findViewById(R.id.owner_name);
       emailTV = (TextView) v.findViewById(R.id.owner_email);
       updateBtn = (Button) v.findViewById(R.id.profile_updateBtn);

       nameTV.setText(userName);
       emailTV.setText(userEmail);

       if (profilePhotoLocalUri!=null){
           profileIV.setImageURI(Uri.parse(profilePhotoLocalUri));
       }
       else if (profilePhotoRemoteUri != null){
           Picasso.with(getActivity()).load(Uri.parse(profilePhotoRemoteUri)).into(profileIV);
       }
       else {
           Toast.makeText(getActivity(), "Unable to load profile photo", Toast.LENGTH_LONG).show();

        }

        updateBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateListener = (UpdateListener) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_updateBtn:
                updateListener.readyToUpdate();
                break;
            default:
                Toast.makeText(getActivity(), "View clicked", Toast.LENGTH_SHORT).show();
        }
    }
    public interface UpdateListener{

        void readyToUpdate();
    }
}
