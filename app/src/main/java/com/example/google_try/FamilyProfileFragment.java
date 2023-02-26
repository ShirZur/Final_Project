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


public class FamilyProfileFragment extends Fragment {

    private TextView family_name_profile_TXT, content_family_TXT, number_children_profile_TXT, hourly_wage_TXT,email_profile_family_TXT;

    private ImageView pets_IMG, cooking_IMG, hw_IMG, house_IMG, log_out_family_IMG;

    private Button BTN1, BTN2, BTN3, BTN4, BTN5, contact_number_family_BTN, edit_profile_family_BTN;;
    private Button[] family_BTN;

    private CircleImageView profile_family_IMG;

    private Uri imagePath;

    private String pictureProfile;

    private FirebaseAuth firebaseAuth;

    private GoogleMapFragment googleMapFragment;

    private String uid;

    public String getmParam1() {
        return mParam1;
    }

    public String getUid() {
        return uid;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;



    public FamilyProfileFragment() {
        // Required empty public constructor
    }


    public static FamilyProfileFragment newInstance(String param1, String param2) {
        FamilyProfileFragment fragment = new FamilyProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_family_profile,container,false);
        findViews(view);
        getFamilyDataFromFirebase();
        googleMapFragment = new GoogleMapFragment();
        getChildFragmentManager().beginTransaction().add(R.id.google_maps_FRAME,googleMapFragment).commit();

        setOnClick();

        return view;
    }

    private void setOnClick() {
        log_out_family_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        edit_profile_family_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditProfileFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    private void getFamilyDataFromFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String path = "";
                if(!uid.equals(firebaseAuth.getCurrentUser().getUid())){
                    path = "Nanny_User";
                }
                else{
                    path = "Family_User";
                }
                if(snapshot.child(path).child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()){
                    databaseReference.child("Family_User").child(uid).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Family family = task.getResult().getValue(Family.class);

                            assert family != null;

                            family_name_profile_TXT.setText(family.getFamily_name());
                            content_family_TXT.setText(family.getContent_of_family());
                            hourly_wage_TXT.setText(family.getHourly_wage() + ".00/hr");
                            email_profile_family_TXT.setText(family.getEmail());
                            number_children_profile_TXT.setText(family.getNumber_of_kids());
                            contact_number_family_BTN.setText("Contact " + family.getFamily_name() + " " + family.getNumberPhone());
                            Picasso.get().load(family.getProfilePicture()).into(profile_family_IMG);
                            if(family.isPets()){
                                pets_IMG.setImageResource(R.drawable.baseline_check_24);
                                pets_IMG.setVisibility(View.VISIBLE);
                            }else{
                                pets_IMG.setImageResource(R.drawable.baseline_close_24);
                                pets_IMG.setVisibility(View.VISIBLE);
                            }
                            if(family.isCooking()){
                                cooking_IMG.setImageResource(R.drawable.baseline_check_24);
                                cooking_IMG.setVisibility(View.VISIBLE);
                            }else{
                                cooking_IMG.setImageResource(R.drawable.baseline_close_24);
                                cooking_IMG.setVisibility(View.VISIBLE);
                            }
                            if(family.isHw()){
                                hw_IMG.setImageResource(R.drawable.baseline_check_24);
                                hw_IMG.setVisibility(View.VISIBLE);
                            }else{
                                hw_IMG.setImageResource(R.drawable.baseline_close_24);
                                hw_IMG.setVisibility(View.VISIBLE);
                            }
                            if(family.isHouseChores()){
                                house_IMG.setImageResource(R.drawable.baseline_check_24);
                                house_IMG.setVisibility(View.VISIBLE);
                            }else{
                                house_IMG.setImageResource(R.drawable.baseline_close_24);
                                house_IMG.setVisibility(View.VISIBLE);
                            }
                            int i = 0;
                            if(family.isBaby()) {
                                family_BTN[i].setText("Baby");
                                family_BTN[i].setVisibility(View.VISIBLE);
                                i++;
                            }
                            if(family.isToddle()){
                                family_BTN[i].setText("Toddle");
                                family_BTN[i].setVisibility(View.VISIBLE);
                                i++;
                            }
                            if(family.isPreschooler()){
                                family_BTN[i].setText("Preschooler");
                                family_BTN[i].setVisibility(View.VISIBLE);
                                i++;
                            }
                            if(family.isStudent()){
                                family_BTN[i].setText("Student");
                                family_BTN[i].setVisibility(View.VISIBLE);
                                i++;
                            }
                            if(family.isAdolescent()){
                                family_BTN[i].setText("Adolescent");
                                family_BTN[i].setVisibility(View.VISIBLE);
                                i++;
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
        family_name_profile_TXT = view.findViewById(R.id.family_name_profile_TXT);
        content_family_TXT = view.findViewById(R.id.content_family_TXT);
        number_children_profile_TXT = view.findViewById(R.id.number_children_profile_TXT);
        profile_family_IMG = view.findViewById(R.id.profile_family_IMG);
        hourly_wage_TXT = view.findViewById(R.id.hourly_wage_TXT);
        pets_IMG = view.findViewById(R.id.pets_IMG);
        contact_number_family_BTN = view.findViewById(R.id.contact_number_family_BTN);
        cooking_IMG = view.findViewById(R.id.cooking_IMG);
        hw_IMG = view.findViewById(R.id.hw_IMG);
        house_IMG = view.findViewById(R.id.house_IMG);
        email_profile_family_TXT = view.findViewById(R.id.email_profile_family_TXT);
        edit_profile_family_BTN = view.findViewById(R.id.edit_profile_family_BTN);
        log_out_family_IMG = view.findViewById(R.id.log_out_family_IMG);

        BTN1 = view.findViewById(R.id.BTN1);
        BTN2 = view.findViewById(R.id.BTN2);
        BTN3 = view.findViewById(R.id.BTN3);
        BTN4 = view.findViewById(R.id.BTN4);

        family_BTN = new Button[]{BTN1,BTN2,BTN3,BTN4,BTN5};

    }

    public void setUid(String uid) {
        this.uid = uid;

    }
}