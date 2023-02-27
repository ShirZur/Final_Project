package com.example.google_try;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import im.delight.android.location.SimpleLocation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RecyclerViewClickInterface , LocationListener {

    private GoogleMapFragment googleMapFragment;

    GoogleMap googleMap;
    private RecyclerView recyclerView;

    private ImageView change_height_IMG;

    private FrameLayout FRAME_map;

    private TextView hello_TXT, result_TXT;

    private DatabaseReference databaseReference;
    private MyAdapter myAdapter;

    private  FamilyAdapter familyAdapter;

    private static ArrayList<Nanny> nannyUserList;

    private static ArrayList<Family> familyUserList;

    private LinearLayout list_layout;

    private FirebaseAuth firebaseAuth;

    private SimpleLocation simpleLocation;


    private double lat,lon;

    private static boolean isNanny = false;

    private LocationListener locationListener;

    private static SetUid_Callback callback;

    public void setSetUid_callback(SetUid_Callback callback) {

        this.callback = callback;

    }

    private static Map_Callback map_callback;

    public void setMap_callback(Map_Callback map_callback){
        this.map_callback = map_callback;
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home,container,false);
        findViews(view);
        setOnClick();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("Family_User");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        familyUserList = new ArrayList<>();
        familyAdapter = new FamilyAdapter(getContext(), familyUserList,this);
        recyclerView.setAdapter(familyAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapShot : snapshot.getChildren()){
                    Family family = dataSnapShot.getValue(Family.class);
                    familyUserList.add(family);
                }
                result_TXT.setText("We have " + familyUserList.size() + " results for you");
                familyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



        initViews();



        return view;
    }

    private void setOnClick() {

    }

    private void findLocation(SimpleLocation simpleLocation) {
        //simpleLocation.beginUpdates();
        //this.lat = simpleLocation.getLatitude();
        //this.lon = simpleLocation.getLongitude();

    }

    private void initViews() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = db.getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Nanny_User").child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    hello_TXT.setText("Hello " + dataSnapshot.child("Nanny_User").child(firebaseAuth.getCurrentUser().getUid()).child("firstName").getValue(String.class) + "!");
                }  //do something if not exists
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.nanny_user_list);
        list_layout = view.findViewById(R.id.list_layout);
        hello_TXT = view.findViewById(R.id.hello_TXT);
        result_TXT = view.findViewById(R.id.result_TXT);
    }

    @Override
    public void onItemClick(int position) {
        callback.setUid(familyUserList.get(position).getUid());

    }

    @Override
    public void onLongItemClick(int position) {


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}