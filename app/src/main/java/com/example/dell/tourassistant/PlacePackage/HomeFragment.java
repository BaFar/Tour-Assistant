        package com.example.dell.tourassistant.PlacePackage;

        import android.content.Context;
        import android.content.SharedPreferences;
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


        import com.example.dell.tourassistant.ConnectivityReceiver;
        import com.example.dell.tourassistant.R;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener{


    private NearByListener nearByListener;


    private TextView tempTV, placeNameTV, weatherTypeTV;
    private ImageView weatherIconIV;
    private Button foodBtn,resturantBtn,hotelBtn,hospitalBtn,atmBtn,policeBtn;
    private String currentLat=null;
    private String currentLon=null;
    // private double currentLon=0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        String value= getArguments().getString("msg",null);
        Toast.makeText(getActivity(), ""+value, Toast.LENGTH_SHORT).show();

        foodBtn = (Button) view.findViewById(R.id.catagoryFood);
        resturantBtn = (Button) view.findViewById(R.id.catagoryResturant);
        hotelBtn = (Button) view.findViewById(R.id.catagoryHotel);
        hospitalBtn = (Button) view.findViewById(R.id.catagoryHospital);
        atmBtn = (Button) view.findViewById(R.id.catagoryATM);
        policeBtn = (Button) view.findViewById(R.id.catagoryPolice);

        foodBtn.setOnClickListener(this);
        resturantBtn.setOnClickListener(this);
        hotelBtn.setOnClickListener(this);
        hospitalBtn.setOnClickListener(this);
        atmBtn.setOnClickListener(this);
        policeBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        /*SharedPreferences preferences = getActivity().getSharedPreferences("coordinate",Context.MODE_PRIVATE);
        currentLat= preferences.getString("currentLat",null);
        currentLon = preferences.getString("currentLon",null);*/


    }


    @Override
    public void onPause() {

        super.onPause();
    }



    @Override
    public void onClick(View v) {

        if (!ConnectivityReceiver.isConnected()){
            Toast.makeText(getActivity(), "Opps! No internet Connection. First check internet connection, please!", Toast.LENGTH_LONG).show();
            return;
        }

        String catagory=null;
        switch (v.getId()){

            case R.id.catagoryFood:
                catagory = "food";
                Log.d("click","clicked");
                break;
            case R.id.catagoryResturant:
                catagory = "resturant";
                Log.d("click","clicked");
                break;
            case R.id.catagoryHotel:
                catagory = "hotel";
                Log.d("click","clicked");
                break;
            case R.id.catagoryHospital:
                catagory = "hospital";
                Log.d("click","clicked");
                break;
            case R.id.catagoryATM:
                catagory = "atm";
                Log.d("click","clicked");
                break;
            case R.id.catagoryPolice:
                catagory = "police";
                Log.d("click","clicked");
                break;
            default:
                Toast.makeText(getActivity(), "UnKnown Event Clicked", Toast.LENGTH_SHORT).show();
        }

        nearByListener.showNearBy(catagory);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nearByListener = (NearByListener) context;

    }

    public interface NearByListener{
        void showNearBy(String catagory);
    }


    /*public int getIcon(String iconCode) {
        int code = 0;
        switch (iconCode) {
            case "200":
                code = R.drawable.t01d;
                break;
            case "201":
                code = R.drawable.t02d;
                break;
            case "202":
                code = R.drawable.t03d;
                break;
            case "230":
            case "231":
            case "232":
                code = R.drawable.t04d;
                break;
            case "233":
                code = R.drawable.t05d;
                break;
            case "300":
                code = R.drawable.d01d;
                break;
            case "301":
                code = R.drawable.d02d;
                break;
            case "302":
                code = R.drawable.d03d;
                break;
            case "500":
                code = R.drawable.r01d;
                break;
            case "501":
                code = R.drawable.r02d;
                break;
            case "502":
                code = R.drawable.r03d;
                break;
            case "511":
                code = R.drawable.f01d;
                break;
            default:
                code = R.drawable.c01n;
        }


        return code;

    }*/
}

