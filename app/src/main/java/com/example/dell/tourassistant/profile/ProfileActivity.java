package com.example.dell.tourassistant.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.tourassistant.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileActivity extends AppCompatActivity implements ProfileViewFragment.UpdateListener {

    private DatabaseReference dr=null;
    private String profilePhotoLocalUri=null;
    private String profilePhotoRemoteUri = null;
    private String userID = null;
    private String userName = null;
    private String userEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        userName = user.getDisplayName();
        if (user.getPhotoUrl()!=null){

            profilePhotoRemoteUri = user.getPhotoUrl().toString();
        }

        userEmail = user.getEmail();

        dr = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID).child("profilePicLocalUri");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profilePhotoLocalUri = dataSnapshot.getValue(String.class);
                Log.d("profile",profilePhotoLocalUri);
                viewProfile();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("profile","local uri load failed "+databaseError.getMessage());
                Toast.makeText(ProfileActivity.this, "Currently unable to show , Try again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void viewProfile() {
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("photoLocalUri",profilePhotoLocalUri);
        bundle.putString("photoRemoteUri",profilePhotoRemoteUri);
        bundle.putString("userID",userID);
        bundle.putString("userEmail",userEmail);

        ProfileViewFragment pvf = new ProfileViewFragment();
        pvf.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.profile_container,pvf);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void readyToUpdate() {
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("photoLocalUri",profilePhotoLocalUri);
        bundle.putString("photoRemoteUri",profilePhotoRemoteUri);
        bundle.putString("userID",userID);
        bundle.putString("userEmail",userEmail);

        ProfileEditFragment pef = new ProfileEditFragment();
        pef.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.profile_container,pef);
        ft.addToBackStack(null);
        ft.commit();
    }

}
