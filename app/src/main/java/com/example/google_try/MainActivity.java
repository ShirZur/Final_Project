package com.example.google_try;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.google_try.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private GoogleMapFragment googleMapFragment;
    private ProfileFragment profileFragment;
    private HomeFragment homeFragment;

    private HomeFamilyFragment homeFamilyFragment;

    private EditProfileFragment editProfileFragment;

    private EditProfileFamilyFragment editProfileFamilyFragment;
    private SettingsFragment settingsFragment;

    ActivityMainBinding binding;

    private RecyclerView recyclerView;

    private FamilyProfileFragment familyProfileFragment;

    private DatabaseReference databaseReference;
    private MyAdapter myAdapter;
    private ArrayList<Nanny> nannyUserList;
    private FirebaseAuth firebaseAuth;

    Map_Callback map_callback = new Map_Callback() {
        @Override
        public void setLocation(GoogleMapFragment fragment, double lat, double lon, String name) {
            fragment.setLocation(lat,lon,name);
        }
    };


    private SetUid_Callback setUid_callback = new SetUid_Callback() {
        @Override
        public void setUid(String uid) {
            firebaseAuth = FirebaseAuth.getInstance();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("Family_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()) {
                            Log.d("AABB", uid);
                            if(!uid.equals(firebaseAuth.getCurrentUser().getUid())){
                                profileFragment = new ProfileFragment();
                                profileFragment.setUid(uid);;
                                replaceFragment(profileFragment);
                            }else{
                                familyProfileFragment = new FamilyProfileFragment();
                                familyProfileFragment.setUid(uid);
                                replaceFragment(familyProfileFragment);
                            }

                        } else {
                            if(!uid.equals(firebaseAuth.getCurrentUser().getUid())){
                                familyProfileFragment = new FamilyProfileFragment();
                                familyProfileFragment.setUid(uid);
                                replaceFragment(familyProfileFragment);
                            }else{
                                profileFragment = new ProfileFragment();
                                profileFragment.setUid(uid);
                                replaceFragment(profileFragment);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeFragment = new HomeFragment();
        homeFragment.setSetUid_callback(setUid_callback);

        homeFamilyFragment = new HomeFamilyFragment();
        homeFamilyFragment.setSetUid_callback(setUid_callback);
        homeFamilyFragment.setMap_callback(map_callback);



        editProfileFragment = new EditProfileFragment();
        editProfileFragment.setSetUid_callback(setUid_callback);

        editProfileFamilyFragment = new EditProfileFamilyFragment();
        editProfileFamilyFragment.setSetUid_callback(setUid_callback);



        //replaceFragment(homeFragment);

        profileFragment = new ProfileFragment();
        settingsFragment = new SettingsFragment();

       // firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Family_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()) {
                    replaceFragment(homeFamilyFragment);
                } else {
                    replaceFragment(homeFragment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Family_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()) {
                                replaceFragment(homeFamilyFragment);
                            } else {
                                replaceFragment(homeFragment);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    break;
                case R.id.profile:
                    setUid_callback.setUid(firebaseAuth.getCurrentUser().getUid());
                    break;
                case R.id.settings:
                    replaceFragment(settingsFragment);
            }

            return true;
        });


    }





    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


         }