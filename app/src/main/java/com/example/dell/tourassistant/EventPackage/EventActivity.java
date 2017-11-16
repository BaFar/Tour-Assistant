package com.example.dell.tourassistant.EventPackage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dell.tourassistant.LoginActivity;
import com.example.dell.tourassistant.R;
import com.example.dell.tourassistant.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/*implements AddEventFragment.CreateEvent,SingleEventFragment.EventInterface */
public class EventActivity extends AppCompatActivity implements AddEventFragment.CreateEvent,SingleEventFragment.EventInterface {

      private ArrayList<Event> cEventList;
      private ArrayList<Event> pEventList;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction ft;
    private DatabaseReference databaseReference;
    private String userId;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    private BottomNavigationView mBottomNV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        mBottomNV = (BottomNavigationView) findViewById(R.id.navigation);
        userId = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();




           cEventList = new ArrayList<Event>();
//         FirebaseDatabase.getInstance().setPersistenceEnabled(true);
         databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("EventList");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cEventList = new ArrayList<Event>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Event event = new Event();
                    event = ds.getValue(Event.class);
                    cEventList.add(event);

                }

               Log.d("commingEvents:",""+cEventList.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Bundle bundle = new Bundle();
       // bundle.putParcelableArrayList("comming events",cEventList);
        AddEventFragment addEventFragment = new AddEventFragment();
      //  addEventFragment.setArguments(bundle);
        ft = fm.beginTransaction();
        ft.add(R.id.eventFragmentContainer,addEventFragment);
        ft.commit();

        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment=null;

                switch (item.getItemId()){
                    case R.id.comming_event_menu:
                        fragment = new CommingEventFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("comming events",cEventList);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.past_event_menu:
                        fragment = new PastEventFragment();
                        break;
                    case R.id.add_event_menu:
                        fragment = new AddEventFragment();
                        break;
                }


                transaction.replace(R.id.eventFragmentContainer,fragment);
                transaction.commit();
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.add_event_menu);



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
    @Override
    public void createEventClicked(String destination, int budget, String fromDate, String toDate, double lattitude, double longitude) {

        Toast.makeText(this, "Created: "+destination, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, ""+fromDate, Toast.LENGTH_SHORT).show();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("EventList");
        String keyValue=databaseReference.push().getKey();

        Event event = new Event(destination,fromDate,toDate, budget,lattitude,longitude);
        event.setKey(keyValue);
        event.addExpense(new Expense("Breakfast",60,"27/12/17 2:30pm"));

        event.addMoment(new Moment("Dinner with local food","simple path","27/12/17 2:30pm"));
        databaseReference.child(keyValue).setValue(event);

        event.addExpense(new Expense("Dinner",90,"27/12/17 2:30pm"));
        event.addMoment(new Moment("break fast with local food","simple path","27/12/17 2:30pm"));

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

        Event event =cEventList.get(position);
        event.addExpense(new Expense(noteText,amount,curentTime));
        databaseReference.setValue(event);
        Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void itemMomentAdd(String eventKey, int position, String noteText, String imagePath, String currentTime) {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                .child("EventList").child(eventKey);
        Event event = cEventList.get(position);
        event.addMoment(new Moment(noteText,imagePath,currentTime));
        databaseReference.setValue(event);
        Toast.makeText(this, "Moment Added", Toast.LENGTH_SHORT).show();

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
}

/*
*
        DatabaseReference dbRefE =  FirebaseDatabase.getInstance().getReference().child("EventList").child(keyValue).child("ExpenseList");
        DatabaseReference dbRefM =  FirebaseDatabase.getInstance().getReference().child("EventList").child(keyValue).child("MomentList");
        Expense  expense = new Expense("Breakfast",60);
        Moment moment = new Moment("Breakfast with local food",3);

        dbRefE.setValue(expense);
        dbRefM.setValue(moment);

         GenericTypeIndicator<ArrayList<Event>> indicator = new GenericTypeIndicator<ArrayList<Event>>() { };
            cEventList = new ArrayList<Event>();
                  cEventList = dataSnapshot.getValue(indicator);
            int i = cEventList.get(0).getBudget();
           */