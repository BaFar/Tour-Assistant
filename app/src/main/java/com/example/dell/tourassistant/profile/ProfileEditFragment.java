package com.example.dell.tourassistant.profile;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.ConnectivityReceiver;
import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.PermissionUtil;
import com.example.dell.tourassistant.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class
ProfileEditFragment extends Fragment implements View.OnClickListener {

    private ImageView profileIV;
    private TextView nameET, emailET;
    private Button saveBtn,cancelBtn,cameraBtn;

    private int IMAGE_CAPTURE_REQUEST_CODE = 301;
    private int GALLARY_REQUEST_CODE= 302;

    private Uri profilePicUrl=null;
    private Bitmap profileBitMap;
    private Uri profilePicRemoteUri;
    private DatabaseReference dr;
    private String dbLocalUri = null;
    private FirebaseUser user=null;
    private String userEmail = null;
    private String userID = null;
    public ProfileEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile_edit, container, false);

        dbLocalUri = getArguments().getString("photoLocalUri");

        profileIV = (ImageView) v.findViewById(R.id.profile_picture_edit);
        nameET = (EditText) v.findViewById(R.id.owner_name_edit);
        emailET = (EditText) v.findViewById(R.id.owner_email_edit);
        saveBtn = (Button) v.findViewById(R.id.profile_saveBtn);
        cancelBtn = (Button) v.findViewById(R.id.cancel_btn);
        cameraBtn = (Button) v.findViewById(R.id.cameraBtn);



        user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        profilePicUrl = user.getPhotoUrl();
        userID = user.getUid();

        dr = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID).child("profilePicLocalUri");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbLocalUri = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if (userName != null) nameET.setText(userName);
        if (dbLocalUri != null) {
            profileIV.setImageURI(Uri.parse(dbLocalUri));
        }
        else if (profilePicUrl != null){
            //profileIV.setImageURI(profilePicUrl)
            Picasso.with(getActivity()).load(profilePicUrl).into(profileIV);
        }
        else {
            Toast.makeText(getActivity(), "NO profile pic or loading failed", Toast.LENGTH_SHORT).show();
        }

        cameraBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        final String[] items = {"Choose from gallery","Take photo","Cancel"};

        switch (v.getId()){
            case  R.id.cameraBtn:
                profileBitMap = null;
                new AlertDialog.Builder(getActivity())
                        .setTitle("Take Profile picture")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which ==0){/*chosen first option: gallery*/
                                    choosePhotoFromGallery();
                                    Log.d("profile_picture","gellary choosen");

                                }
                                else if(which == 1){/*chosen secound option: take photo*/

                                    takePhoto();
                                    Log.d("profile_picture","camera choosen");
                                }
                                else if (which == 2){/*chosen last option: cancel*/
                                    dialog.cancel();
                                    Log.d("profile_picture","cancel choosen");
                                }
                                else {
                                    Log.d("profile_picture","Unknown  option");
                                }
                            }
                        }).show();
                break;
            case R.id.profile_saveBtn:
                if (!ConnectivityReceiver.isConnected())
                {
                    Toast.makeText(getActivity(), "Sorry, you are offline, to edit trun on internet connection", Toast.LENGTH_SHORT).show();
                    break;
                }
                String username = nameET.getText().toString();
                if (username == null) {
                    Toast.makeText(getActivity(), "Username can not be null", Toast.LENGTH_LONG).show();
                    break;
                }
                String localUri=null;
                localUri= savePhotoLocally();
                if (localUri!= null) {
                    profilePicRemoteUri = profilePicUrl;
                    savePhotoRemotly(localUri,username);
                }
                else {
                    Log.d("profile_picture","local uri null: failed");
                }
                break;
            case R.id.cancel_btn:
                /*goto profile view fragment*/
                break;

        }
    }
    private void saveProfile(String localUri,String username){


        final String ppluri= localUri;


            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(profilePicRemoteUri)
                    .build();

            user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("profile_update","profile updated successfully");
                    String userID = user.getUid();


                    dr.setValue(ppluri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("profile_picture","local uri database  backup failed");
                        }
                    });

                    profileIV.setImageResource(R.drawable.gallary);
                    nameET.setText(" ");
                    emailET.setText(" ");

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction ft= fm.beginTransaction();
                    ft.replace(R.id.profile_container,new ProfileViewFragment());
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("profile_update", "profile update failed");
                    Toast.makeText(getActivity(), "Profile update failed", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private String savePhotoLocally(){
        if (profileBitMap == null){
            Toast.makeText(getActivity(), "No Photo found,Try Again", Toast.LENGTH_SHORT).show();
            Log.d("profile_picture","no photo to locally backup");
            return null;
        }

        String photoName = ExtraHelper.getCurrentTime();
        File root = getActivity().getDir("ProfilePictures",Context.MODE_PRIVATE);
        String imageUri = root+photoName+".png";
        File profileFile = new File(imageUri);
        try {
            FileOutputStream fos = new FileOutputStream(profileFile);
            profileBitMap.compress(Bitmap.CompressFormat.PNG,100,fos);
            Log.d("profile_picture","photo local backup successfull");
            Uri uri = Uri.fromFile(profileFile);
            imageUri = uri.toString();

            return imageUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("profile_picture","profile picture locally save failed: "+e.getMessage());
            Toast.makeText(getActivity(), "Local Backup failed", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
    private void savePhotoRemotly(final String localUri, final String username){

        String photoName = Uri.parse(localUri).getLastPathSegment();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storeRef = FirebaseStorage.getInstance().getReference();
        StorageReference sr = storeRef.child("users").child(userID).child("profile").child(photoName);

        sr.putFile(Uri.parse(localUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("profile_picture","Online backUp successfull");
                 profilePicRemoteUri = taskSnapshot.getDownloadUrl();
                 saveProfile(localUri,username);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("profile_picture","Remote backup failed: "+e.getMessage());
                Toast.makeText(getActivity(), "Remote backup failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void choosePhotoFromGallery() {

        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {
            PermissionUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionUtil.PermissionAskListener() {

                @Override
                public void onNeedPermission() {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PermissionUtil.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }

                @Override
                public void onPermissionPreviouslyDenied() {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Need to access media and photos")
                            .setMessage("App must need to access gellary and photos to choose profile picture")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PermissionUtil.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                }

                @Override
                public void onPermissionDisabled() {

                    Toast.makeText(getActivity(), "Please allow media photos permission from app settings", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPermissionGranted() {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    // startActivityForResult(intent,GALLARY_REQUEST_CODE);
                    startActivityForResult(Intent.createChooser(intent,"choose photo"),GALLARY_REQUEST_CODE);
                    Log.d("profile_picture","gellary requested");
                }
            });
        }
        else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            // startActivityForResult(intent,GALLARY_REQUEST_CODE);
            startActivityForResult(Intent.createChooser(intent,"choose photo"),GALLARY_REQUEST_CODE);
            Log.d("profile_picture","gellary requested");
        }
    }
    private void takePhoto() {
     if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

         PermissionUtil.checkPermission(getActivity(), Manifest.permission.CAMERA, new PermissionUtil.PermissionAskListener() {
             @Override
             public void onNeedPermission() {
                 ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},PermissionUtil.CAMERA_REQUEST_CODE);
             }

             @Override
             public void onPermissionPreviouslyDenied() {

                 new AlertDialog.Builder(getActivity())
                         .setTitle("Need Camera Permission")
                         .setMessage("App must need camera permission to take photo..")
                         .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.cancel();
                                 ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},PermissionUtil.CAMERA_REQUEST_CODE);

                             }
                         }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.cancel();
                     }
                 });
             }

             @Override
             public void onPermissionDisabled() {
                 Toast.makeText(getActivity(), "Allow camera permission from app settings", Toast.LENGTH_LONG).show();
             }

             @Override
             public void onPermissionGranted() {

                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(intent,IMAGE_CAPTURE_REQUEST_CODE);
                 Log.d("take_picture","camera called after permission granted");
             }
         });

     }else {
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         startActivityForResult(intent,IMAGE_CAPTURE_REQUEST_CODE);
     }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == GALLARY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                try {
                    profileBitMap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),data.getData());
                    profileIV.setImageBitmap(profileBitMap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("profile_picture","Image selected but failed: "+e.getMessage());
                }
            }
            else if ( requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
                profileBitMap = (Bitmap) data.getExtras().get("data");
                profileIV.setImageBitmap(profileBitMap);
            }
            else {
                Log.d("profile_picture","onActivity result: other options");
            }
        }
    }
}
