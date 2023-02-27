package com.example.google_try;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;


public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView name_TXT, age_TXT, hourly_rate_TXT, content_nanny_TXT, email_profile_TXT;
    private ImageView experience_IMG, smoker_IMG, driving_IMG, log_out_IMG;
    private  Button contact_number_BTN, profile1_BTN, profile2_BTN, profile3_BTN, profile4_BTN, profile5_BTN;
    private static Button edit_profile_BTN;
    private Button[] profile_BTN;
    private CircleImageView profile_IMG;

    private Uri imagePath;

    private String pictureProfile;

    private FirebaseAuth firebaseAuth;

    private GoogleMapFragment googleMapFragment;

    private String uid;

    private SimpleLocation simpleLocation;

    private double lat, lon;




    public String getmParam1() {
        return mParam1;
    }

    public String getUid() {
        return uid;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        findViews(view);


        firebaseAuth = FirebaseAuth.getInstance();

        if(!uid.equals(firebaseAuth.getCurrentUser().getUid())){
            edit_profile_BTN.setVisibility(View.GONE);
        }
        getNannyDataFromFirebase();






        setOnClick();

        return view;
    }

    private void findLocation(SimpleLocation simpleLocation) {
        simpleLocation.beginUpdates();
        this.lat = simpleLocation.getLatitude();
        this.lon = simpleLocation.getLongitude();
    }


    private void setOnClick() {
        profile_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/+");
                startActivityForResult(photoIntent,1);
            }
        });

        log_out_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        edit_profile_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    private void getNannyDataFromFirebase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String path = "";
                if(!uid.equals(firebaseAuth.getCurrentUser().getUid())){
                    path = "Family_User";
                }
                else{
                    path = "Nanny_User";
                }
                if(snapshot.child(path).child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()){
                    databaseReference.child("Nanny_User").child(uid).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Nanny nanny = task.getResult().getValue(Nanny.class);

                            assert nanny != null;
                            googleMapFragment = new GoogleMapFragment();
                            Bundle bundle = new Bundle();
                            bundle.putDouble("LAT", nanny.getLat());
                            bundle.putDouble("LON", nanny.getLon());
                            bundle.putString("NAME", nanny.getFirstName());

                            getChildFragmentManager().beginTransaction().add(R.id.google_maps_FRAME,GoogleMapFragment.class,bundle).commit();


                            name_TXT.setText(nanny.getFirstName() + " " + nanny.getLastName());
                            age_TXT.setText(nanny.getAge());
                            hourly_rate_TXT.setText(nanny.getPerHour()+".00/hr");
                            content_nanny_TXT.setText(nanny.getContentAboutNanny());
                            email_profile_TXT.setText(nanny.getEmail());
                            contact_number_BTN.setText("Contact " + nanny.getFirstName() + " " + nanny.getNumberPhone());
                            Picasso.get().load(nanny.getProfilePicture()).into(profile_IMG);
                            if(nanny.isSmoker()){
                                smoker_IMG.setImageResource(R.drawable.baseline_check_24);
                                smoker_IMG.setVisibility(View.VISIBLE);
                            }else{
                                smoker_IMG.setImageResource(R.drawable.baseline_close_24);
                                smoker_IMG.setVisibility(View.VISIBLE);
                            }
                            if(nanny.isHasDrivingLicense()){
                                driving_IMG.setImageResource(R.drawable.baseline_check_24);
                                driving_IMG.setVisibility(View.VISIBLE);
                            }else{
                                driving_IMG.setImageResource(R.drawable.baseline_close_24);
                                driving_IMG.setVisibility(View.VISIBLE);
                            }
                            if(nanny.isExperienceWithChildren()){
                                experience_IMG.setImageResource(R.drawable.baseline_check_24);
                                experience_IMG.setVisibility(View.VISIBLE);
                            }else{
                                experience_IMG.setImageResource(R.drawable.baseline_close_24);
                                experience_IMG.setVisibility(View.VISIBLE);
                            }

                            for(int i =0;i<nanny.getLanguages().size(); i++){
                                profile_BTN[i].setText(nanny.getLanguages().get(i));
                                profile_BTN[i].setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findViews(View view) {
        age_TXT = view.findViewById(R.id.age_TXT);
        name_TXT = view.findViewById(R.id.name_TXT);
        experience_IMG = view.findViewById(R.id.experience_IMG);
        smoker_IMG = view.findViewById(R.id.smoker_IMG);
        driving_IMG = view.findViewById(R.id.driving_IMG);
        contact_number_BTN = view.findViewById(R.id.contact_number_BTN);
        hourly_rate_TXT = view.findViewById(R.id.hourly_rate_TXT);
        content_nanny_TXT = view.findViewById(R.id.content_nanny_TXT);
        email_profile_TXT = view.findViewById(R.id.email_profile_TXT);
       edit_profile_BTN = view.findViewById(R.id.edit_profile_BTN);
        log_out_IMG = view.findViewById(R.id.log_out_IMG);
        profile_IMG = view.findViewById(R.id.profile_IMG);
        profile_IMG = view.findViewById(R.id.profile_IMG);
        profile1_BTN = view.findViewById(R.id.profile2_BTN);
        profile2_BTN = view.findViewById(R.id.profile1_BTN);
        profile3_BTN = view.findViewById(R.id.profile3_BTN);
        profile4_BTN = view.findViewById(R.id.profile4_BTN);
        profile5_BTN = view.findViewById(R.id.profile5_BTN);
        profile_BTN = new Button[]{profile1_BTN, profile2_BTN, profile3_BTN, profile4_BTN, profile5_BTN};
    }

    public void setUid(String uid) {

        this.uid = uid;

    }
}