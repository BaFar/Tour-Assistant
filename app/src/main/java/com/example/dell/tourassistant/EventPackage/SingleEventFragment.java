package com.example.dell.tourassistant.EventPackage;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.tourassistant.ExtraHelper;
import com.example.dell.tourassistant.PlacePackage.PlaceActivity;
import com.example.dell.tourassistant.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleEventFragment extends Fragment implements View.OnClickListener {

    final int  PLACE_PICKER_REQUEST_CODE = 1;
    private EventInterface eventInterface;
    private Event event;
    private TextView eventNameTV,fromDateTV,toDateTV,budgetTV,currentExpenseTV;
    private Button expenseListBtn,momentListBtn,addExpenseBtn,addMomentBtn,editBtn,deleteBtn,weatherBtn,nearByBtn;
    private ListView expenseLV,momentLV;
    private ProgressBar expenseProgress;

    private AlertDialog expenseDialog;
    private AlertDialog momentDialog;
    private AlertDialog eventEditDialog;
    private AlertDialog.Builder expenseBuilder;
    private AlertDialog.Builder momentBuilder;
    private AlertDialog.Builder eventEditBuilder;
    private Calendar calendar;
    private int year,month,day;
    private double lat,lon;

    private String eventKey,fdate,tdate,placeName;
    private int eventPosition;

    static final int REQUEST_IMAGE_CAPTURE = 2;
    private String mCurrentPhotoPath=null;
    private ImageView mPhotoView = null;
    private Bitmap mBitmap;
    private EditText destinationET;

    public SingleEventFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_single_event, container, false);

        expenseBuilder = new AlertDialog.Builder(getActivity());
        momentBuilder = new AlertDialog.Builder(getActivity());
        eventEditBuilder = new AlertDialog.Builder(getActivity());


        event =  getArguments().getParcelable("single event");
        eventPosition = getArguments().getInt("event position");
        eventKey = event.getKey();


        eventNameTV = (TextView) v.findViewById(R.id.event_name);
        fromDateTV = (TextView) v.findViewById(R.id.from_date);
        toDateTV = (TextView) v.findViewById(R.id.to_date);
        budgetTV = (TextView) v.findViewById(R.id.event_est_budget);
        currentExpenseTV = (TextView) v.findViewById(R.id.current_expense);
        expenseListBtn = (Button) v.findViewById(R.id.expense_listBtn);
        momentListBtn = (Button) v.findViewById(R.id.moment_listBtn);
        addExpenseBtn = (Button) v.findViewById(R.id.add_expenseBtn);
        addMomentBtn = (Button) v.findViewById(R.id.add_momentBtn);
        editBtn = (Button) v.findViewById(R.id.event_edit_btn);
        deleteBtn = (Button) v.findViewById(R.id.event_delete_btn);
        nearByBtn = (Button) v.findViewById(R.id.near_by_place_Btn);
        weatherBtn = (Button) v.findViewById(R.id.weather_place_Btn);


        expenseLV = (ListView) v.findViewById(R.id.expense_listview);
        momentLV = (ListView) v.findViewById(R.id.moment_listview);
        expenseProgress = (ProgressBar) v.findViewById(R.id.expense_progressbar);

        calendar =  Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        expenseListBtn.setOnClickListener(this);
        momentListBtn.setOnClickListener(this);
        addMomentBtn.setOnClickListener(this);
        addExpenseBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        nearByBtn.setOnClickListener(this);
        weatherBtn.setOnClickListener(this);


        return v;
    }

    private void createAddMomentDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dview=inflater.inflate(R.layout.add_moment, null);
        final EditText noteTextET = (EditText) dview.findViewById(R.id.moment_note);
        final TextView imagePathTV = (TextView) dview.findViewById(R.id.image_path);
        mPhotoView = (ImageView) dview.findViewById(R.id.moment_photo);
        final Button photoCaptureBtn = (Button) dview.findViewById(R.id.photo_capture_Btn);
        noteTextET.setHint("Note Moment");
        photoCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent(v);
                imagePathTV.setText(mCurrentPhotoPath);
            }
        });

        momentBuilder.setView(dview)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "positive Button Clicked", Toast.LENGTH_SHORT).show();


                        String noteText= noteTextET.getText().toString();
                        String imagePath= mCurrentPhotoPath;
                        //   Toast.makeText(getContext(), ""+purpose+" "+amount, Toast.LENGTH_SHORT).show();
                        eventInterface.itemMomentAdd(eventKey,eventPosition,noteText,imagePath, ExtraHelper.getCurrentTime());        /*code 2 is moment*/

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  LoginDialogFragment.this.getDialog().cancel();
                        momentDialog.cancel();

                    }
                });
        momentDialog =  momentBuilder.create();
    }

    private void createAddExpenseDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dview=inflater.inflate(R.layout.add_expense, null);
        final EditText purposeET = (EditText) dview.findViewById(R.id.expense_purposeET);
        final EditText amountET = (EditText) dview.findViewById(R.id.expense_amountET);

        expenseBuilder.setView(dview)
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "positive Button Clicked", Toast.LENGTH_SHORT).show();

                        String purpose= purposeET.getText().toString();
                        String amount= amountET.getText().toString();
                        double d = Double.parseDouble(amount);
                        Toast.makeText(getContext(), ""+purpose+" "+amount, Toast.LENGTH_SHORT).show();
                        eventInterface.itemExpenseAdd(eventKey,eventPosition,purpose,d,ExtraHelper.getCurrentTime());/*code 1 is expense*/
                        purposeET.setText("");
                        amountET.setText("");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  LoginDialogFragment.this.getDialog().cancel();
                        expenseDialog.cancel();

                    }
                });
        expenseDialog =  expenseBuilder.create();
    }


    private void createEditEvent() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_basic_info,null);

        destinationET = (EditText) view.findViewById(R.id.change_destinationET);
        final EditText budgetET = (EditText) view.findViewById(R.id.change_budgetET);
        final Button   fromDateBtn = (Button) view.findViewById(R.id.change_fromDateET);
        final Button   toDateBtn = (Button) view.findViewById(R.id.change_toDateET);

        destinationET.setText(event.getDestination());

        budgetET.setText(String.valueOf(event.getBudget()));
        fromDateBtn.setText(event.getFromDate());
        toDateBtn.setText(event.getToDate());

        destinationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectPlace(v);
            }
        });
        destinationET.setText(placeName);


        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(),onFormDateSelectedListener,year,month,day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                pickerDialog.show();
            }
            private DatePickerDialog.OnDateSetListener onFormDateSelectedListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    calendar.set(year,month,dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    fdate = dateFormat.format(calendar.getTime());
                    fromDateBtn.setText(fdate);

                }
            };
        });
        toDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(),onToDateSelectedListener,year,month,day);
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                pickerDialog.show();
            }
            private DatePickerDialog.OnDateSetListener onToDateSelectedListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year,month,dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    tdate = dateFormat.format(calendar.getTime());
                    toDateBtn.setText(tdate);
                }
            };

        });
        eventEditBuilder.setView(view).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String des=destinationET.getText().toString();
                int budget = Integer.parseInt(budgetET.getText().toString());
                eventInterface.eventEdit(eventKey,eventPosition,des,fdate,tdate,budget,lat,lon);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        eventEditDialog = eventEditBuilder.create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createAddExpenseDialog();
        createAddMomentDialog();
        createEditEvent();




        eventNameTV.setText(event.getDestination());
        fromDateTV.setText(event.getFromDate());
        toDateTV.setText(event.getToDate());
        budgetTV.setText(String.valueOf(event.getBudget()));
        currentExpenseTV.setText(String.valueOf(event.getTotalExpense()));

        //double f = event.getTotalExpense();
        //int  d = (int) f;
        expenseProgress.setMax(event.getBudget());
        expenseProgress.setProgress((int)event.getTotalExpense());

        ArrayList<Expense> expenseList= new ArrayList<>();
        ArrayList<Moment> momentList= new ArrayList<>();
        expenseList = event.getExpenseList();
        momentList = event.getMomentList();

        ExpenseAdapter expenseAdapter = new ExpenseAdapter(getActivity(),expenseList);
        expenseLV.setAdapter(expenseAdapter);
        MomentAdapter momentAdapter = new MomentAdapter(getActivity(),momentList);
        momentLV.setAdapter(momentAdapter);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.expense_listBtn:

                if (expenseLV.getVisibility()==View.VISIBLE){
                    expenseLV.setVisibility(View.GONE);
                    expenseListBtn.setText("See Expenses");
                }
                else {
                    expenseLV.setVisibility(View.VISIBLE);
                    expenseListBtn.setText("Hide Expenses");
                }

                break;
            case R.id.moment_listBtn:
                if (momentLV.getVisibility()==View.VISIBLE){
                    momentLV.setVisibility(View.GONE);
                    momentListBtn.setText("See Moments");
                }
                else {
                    momentLV.setVisibility(View.VISIBLE);
                    momentListBtn.setText("Hide Moments");
                }
                break;
            case R.id.add_expenseBtn:
                expenseDialog.show();
                break;
            case R.id.add_momentBtn:
                momentDialog.show();
                break;
            case R.id.event_edit_btn:
                eventEditDialog.show();
                break;
            case R.id.event_delete_btn:
                eventInterface.eventDelete(eventKey,eventPosition);
                break;
            case R.id.near_by_place_Btn:
                Intent nearIntent = new Intent(getActivity(), PlaceActivity.class);
                nearIntent.putExtra("des lattitude",event.getLattitude());
                nearIntent.putExtra("des longitude",event.getLongitude());
                startActivity(nearIntent);
                break;
            case R.id.weather_place_Btn:
                break;
            default:
                Toast.makeText(getActivity(), "Other place Clicked", Toast.LENGTH_SHORT).show();
                Log.d("singleEventFrag click","other place clickd");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventInterface = (EventInterface) context;

    }

    public interface EventInterface {
        void itemExpenseAdd(String eventKey, int position, String noteText,double amount,String curentTime);
        void itemMomentAdd(String eventKey, int position, String noteText, String imagePath,String currentTime);
        void eventEdit(String eventKey, int eventPosition, String Destination, String fromDate, String toDate, int budget,double lat,double lon);
        void eventDelete(String eventKey, int eventPosition);


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
        else  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            setPic();
        }
    }

    public void dispatchTakePictureIntent(View view) {
        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("photo","file create failed");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
        else {
            Toast.makeText(getActivity(), "this device has no camera app", Toast.LENGTH_SHORT).show();
        }


    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String folder_main = "eventPictures";


        // File f = new File(getExternalStorageDirectory(Environment.DIRECTORY_PICTURES),folder_main);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //  File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),folder_main);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = image.getAbsolutePath();
        Toast.makeText(getActivity(), ""+mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
        return image;
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mPhotoView.getWidth();
        int targetH = mPhotoView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mPhotoView.setImageBitmap(mBitmap);
    }

}
