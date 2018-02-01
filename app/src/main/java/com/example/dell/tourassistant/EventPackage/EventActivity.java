package com.example.dell.tourassistant.EventPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.LoginActivity;
import com.example.dell.tourassistant.R;
import com.example.dell.tourassistant.SignupActivity;
import com.example.dell.tourassistant.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventActivity extends AppCompatActivity implements
        AddEventFragment.CreateEvent,
        SingleEventFragment.EventInterface{

    private ArrayList<Event> cEventList;
    private ArrayList<Event> pEventList;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft;
    private DatabaseReference databaseReference;
    private String userId;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private StorageReference mstorageRef;

    private BottomNavigationView mBottomNV;
    private boolean isAllDeleted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mBottomNV = (BottomNavigationView) findViewById(R.id.navigation);
        cEventList = new ArrayList<Event>();
        pEventList = new ArrayList<Event>();

         user = auth.getCurrentUser();
        userId = user.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("EventList");
        mstorageRef = FirebaseStorage.getInstance().getReference().child("users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cEventList = new ArrayList<Event>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Event event = new Event();
                    event = ds.getValue(Event.class);
                    String endDate = event.getToDate();
                    boolean ispassed = ExtraHelper.compareTwoDate(endDate);
                    if (ispassed) pEventList.add(event);
                    else cEventList.add(event);
                }

                onDataLoaded(cEventList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventActivity.this, "Database Error Occured", Toast.LENGTH_SHORT).show();
                Log.d("datasnapshot","Database Error Occured");

            }
        });




        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment=null;
                Bundle bundle = new Bundle();
                switch (item.getItemId()){
                    case R.id.comming_event_menu:
                        fragment = new CommingEventFragment();
                        bundle.putParcelableArrayList("comming_events",cEventList);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.past_event_menu:
                        fragment = new PastEventFragment();
                        bundle.putParcelableArrayList("past_ events",pEventList);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.add_event_menu:
                        fragment = new AddEventFragment();
                        break;
                }

                transaction.replace(R.id.eventFragmentContainer,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_manu, menu);
        super.onCreateOptionsMenu(menu);
        //menu.add((CharSequence) getResources().getDrawable(R.drawable.logout_icon));
        //menu.add((CharSequence) getResources().getDrawable(R.drawable.ic_account_box_black_24dp));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            case R.id.delete_acc:
                new AlertDialog.Builder(this)
                        .setTitle("Delete Account?")
                        .setMessage("Deleting your account delete all of your content like picture and other data. Continue?")
                        .setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                deleteAccount();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

                return true;
            case R.id.logout:
                auth.signOut();
                FirebaseUser user = auth.getCurrentUser();
                if (user == null){
                    Intent intent = new Intent(EventActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    static public boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
    private void deleteAccount(){
        changeFlag(true);

        StorageReference storageRef = null;
        FirebaseStorage fstorage = FirebaseStorage.getInstance();

        String eventName = null;
        for (Event event: cEventList) {

            eventName = event.getDestination();
           ArrayList<Moment> momentList = new ArrayList<>();
           momentList = event.getMomentList();
           if (momentList != null){
               int sz = momentList.size();
               for (int i=0; i<sz; i++){
                    storageRef = fstorage.getReferenceFromUrl(momentList.get(i).getDownloadUri());
                    storageRef.delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            changeFlag(false);
                        }
                    });
               }
           }
           /*delete local storage*/
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

            File folder = new File(root+"/"+eventName);
            boolean isLocalDeleted=   deleteDirectory(folder);

            if (isLocalDeleted)
            {
                Log.d("delete","Local data deleted");

            }
            else {
                Log.d("delete","can't delete Local data.");
                Toast.makeText(this, "can't delete local data. Delete it manually", Toast.LENGTH_LONG).show();

            }


        }


        /*before removing user delete all data*/
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Delete","Firebase data deleted successfully");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Delete","Firebase data deletion failed: "+e.getMessage());
               changeFlag(false);
            }
        });

        /*check all data deleted or not*/
        if (!isAllDeleted){
            Toast.makeText(this, "Sorry,data deletion failed. account can't be deleted now.", Toast.LENGTH_LONG).show();
            return;
        }

        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EventActivity.this, "User Account deleted sucessfully", Toast.LENGTH_SHORT).show();
                /*clear all backstack here*/
                startActivity(new Intent(EventActivity.this, LoginActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(EventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Delete","User delete failed. "+e.getMessage());
            }
        });
    }

    void  changeFlag(boolean b){
        isAllDeleted = b;
    }
    @Override
    public void createEventClicked(String destination, int budget, String fromDate, String toDate, double lattitude, double longitude) {

        Toast.makeText(this, "Created: "+destination, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+fromDate, Toast.LENGTH_SHORT).show();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("EventList");
        String keyValue=databaseReference.push().getKey();

        Event event = new Event(destination,fromDate,toDate, budget,lattitude,longitude);
        event.setKey(keyValue);
        databaseReference.child(keyValue).setValue(event);

    }

    public void logOut(View view) {

        auth.signOut();
        FirebaseUser user =auth.getCurrentUser();
        if (user==null){
            startActivity(new Intent(this,SignupActivity.class));
        }
        else {
            Toast.makeText(this, "Log out failed", Toast.LENGTH_SHORT).show();
            Log.d("logout","failed");
        }
    }
    @Override
    public void itemExpenseAdd(String eventKey, int position, String noteText,double amount,String curentTime) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .child("EventList").child(eventKey);

        Event event = cEventList.get(position);
        event.addExpense(new Expense(noteText,amount,curentTime));
        databaseReference.setValue(event);
        Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void itemMomentAdd(String eventKey, int position, String noteText, Bitmap bm) {
        Uri photoPathUri = null;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("EventList").child(eventKey);
        Event event = cEventList.get(position);
        String eventName = event.getDestination();
        if (bm!=null){
            Log.d("bitmap","bitmap is not null");
            photoPathUri =  saveImage(eventName,bm);
        }
        Moment moment = new Moment(noteText,photoPathUri.toString(), ExtraHelper.getCurrentTime());
        event.addMoment(moment);
        databaseReference.setValue(event);
        Toast.makeText(this, "Moment Added", Toast.LENGTH_SHORT).show();
        if (photoPathUri !=null ){
            backUpPhoto(eventName,photoPathUri, databaseReference,position, moment);
        }


    }

    private Uri saveImage(String eventName, Bitmap bitmap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hh_mm_ss");
        String photoName = sdf.format(new Date());
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        File folder = new File(root+"/"+eventName);
        folder.mkdirs();



        File my_file = new File(folder,photoName+".png");

        try {
            FileOutputStream stream = new FileOutputStream(my_file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            stream.flush();
            stream.close();
            Log.d("saveImage","Image saved successfully");
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("saveImage",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("saveImage",e.getMessage());
        }

        Uri uri = Uri.fromFile(new File(root+"/"+eventName+"/"+photoName+".png"));
        return uri;


    }

    private void backUpPhoto(final String eventName, Uri photoPathUri, DatabaseReference dbRef, final int position, final Moment moment){
        String photoName = photoPathUri.getLastPathSegment();
        StorageReference photoRef = mstorageRef.child(eventName+"/photos/"+photoName);

        photoRef.putFile(photoPathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUri = taskSnapshot.getDownloadUrl();
                Log.d("backup","online backup successfull, updating to firebase");
                moment.setDownloadUri(downloadUri.toString());
                Event event = cEventList.get(position);
                int numberOfEvents = event.getMomentList().size();
                if (numberOfEvents > 0 ){
                    String time =event.getMomentList().get(numberOfEvents-1).getMomentTime();
                    if (time.equals(moment.getMomentTime())){
                        event.getMomentList().remove(numberOfEvents-1);
                    }
                }/*hlloe*/
                event.addMoment(moment);
                databaseReference.setValue(event);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("backUpPhoto","photo backup failed: "+e.getMessage());
            }
        });

    }


    @Override
    public void eventEdit(String eventKey, int eventPosition, String Destination, String fromDate, String toDate, int budget,double lat,double lon) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .child("EventList").child(eventKey);
        Event event= cEventList.get(eventPosition);
        event.setDestination(Destination);
        event.setFromDate(fromDate);
        event.setToDate(toDate);
        event.setBudget(budget);
        databaseReference.setValue(event);
        Toast.makeText(this, "Event Edited", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void eventDelete(String eventKey, int eventPosition) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .child("EventList").child(eventKey);
        databaseReference.removeValue();
        mBottomNV.setSelectedItemId(R.id.comming_event_menu);
        Toast.makeText(this, "event Deleted", Toast.LENGTH_SHORT).show();

    }

    public void onDataLoaded(ArrayList<Event> events) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("comming_events",events);
        CommingEventFragment commingEventFragment = new CommingEventFragment();
        commingEventFragment.setArguments(bundle);
        ft = fm.beginTransaction();
        ft.add(R.id.eventFragmentContainer,commingEventFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }
}
