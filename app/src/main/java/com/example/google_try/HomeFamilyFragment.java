package com.example.google_try;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFamilyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFamilyFragment extends Fragment implements RecyclerViewClickInterface{

    private GoogleMapFragment googleMapFragment;
    private RecyclerView family_user_list_recycler;

    private TextView hello_family_TXT, result_family_TXT;

    private ImageView change_height_IMG;

    private FrameLayout FRAME_map_family;

    private DatabaseReference databaseReference;
    private MyAdapter myAdapter;

    private  FamilyAdapter familyAdapter;
    private static ArrayList<Nanny> nannyUserList;

    private static ArrayList<Family> familyUserList;

    private LinearLayout list_layout;

    private FirebaseAuth firebaseAuth;

    private static boolean isNanny = false;

    private static SetUid_Callback callback;

    public void setSetUid_callback(SetUid_Callback callback) {

        this.callback = callback;

    }

    Map_Callback map_callback;

    public void setMap_callback(Map_Callback map_callback){
        this.map_callback = map_callback;
    }


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public HomeFamilyFragment() {
    }


    public static HomeFamilyFragment newInstance(String param1, String param2) {
        HomeFamilyFragment fragment = new HomeFamilyFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_family,container,false);
        findViews(view);
        setOnClick();



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference("Nanny_User");
        family_user_list_recycler.setHasFixedSize(true);
        family_user_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        nannyUserList = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), nannyUserList,this);
        family_user_list_recycler.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapShot : snapshot.getChildren()){
                    Nanny nanny = dataSnapShot.getValue(Nanny.class);
                    nannyUserList.add(nanny);
                }
                result_family_TXT.setText("We have " + nannyUserList.size() + " results for you");
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        initView();

        return view;
    }

    private void initView() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = db.getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Family_User").child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    hello_family_TXT.setText("Hello " + dataSnapshot.child("Family_User").child(firebaseAuth.getCurrentUser().getUid()).child("family_name").getValue(String.class) + " Family!");
                }  //do something if not exists
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void setOnClick() {
    }

    private void findViews(View view) {
       // FRAME_map_family = view.findViewById(R.id.FRAME_map_family);
        family_user_list_recycler = view.findViewById(R.id.family_user_list_recycler);
        result_family_TXT = view.findViewById(R.id.result_family_TXT);
        hello_family_TXT = view.findViewById(R.id.hello_family_TXT);
    }//

    @Override
    public void onItemClick(int position) {
        callback.setUid(nannyUserList.get(position).getUid());

    }

    @Override
    public void onLongItemClick(int position) {

    }
}