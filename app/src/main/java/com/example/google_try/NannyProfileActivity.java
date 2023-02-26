package com.example.google_try;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NannyProfileActivity extends AppCompatActivity {

    private TextView name_TXT, age_TXT, hourly_rate_TXT, content_nanny_TXT, email_profile_TXT;
    private ImageView experience_IMG, smoker_IMG, driving_IMG, log_out_IMG;
    private Button contact_number_BTN, profile1_BTN, profile2_BTN, profile3_BTN, profile4_BTN, profile5_BTN;
    private Button[] profile_BTN;
    private CircleImageView profile_IMG;

    private Uri imagePath;

    private String pictureProfile;
    
    private FirebaseAuth firebaseAuth;

    private GoogleMapFragment googleMapFragment;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_profile);

        findViews();
        uid = getIntent().getStringExtra("UID");
        getNannyDataFromFirebase();
        googleMapFragment = new GoogleMapFragment();
        setOnClick();



        getSupportFragmentManager().beginTransaction().add(R.id.google_maps_FRAME,googleMapFragment).commit();
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
                Intent intent = new Intent(NannyProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
             bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profile_IMG.setImageBitmap(bitmap);

    }

    private void getNannyDataFromFirebase() {



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Nanny_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()){
                    databaseReference.child("Nanny_User").child(uid).get().addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           Nanny nanny = task.getResult().getValue(Nanny.class);

                           assert nanny != null;
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

    private void findViews() {
        age_TXT = findViewById(R.id.age_TXT);
        name_TXT = findViewById(R.id.name_TXT);
        experience_IMG = findViewById(R.id.experience_IMG);
        smoker_IMG = findViewById(R.id.smoker_IMG);
        driving_IMG = findViewById(R.id.driving_IMG);
        contact_number_BTN = findViewById(R.id.contact_number_BTN);
        hourly_rate_TXT = findViewById(R.id.hourly_rate_TXT);
        content_nanny_TXT = findViewById(R.id.content_nanny_TXT);
        email_profile_TXT = findViewById(R.id.email_profile_TXT);
        log_out_IMG = findViewById(R.id.log_out_IMG);
        profile_IMG = findViewById(R.id.profile_IMG);
        profile_IMG = findViewById(R.id.profile_IMG);
        profile1_BTN = findViewById(R.id.profile2_BTN);
        profile2_BTN = findViewById(R.id.profile1_BTN);
        profile3_BTN = findViewById(R.id.profile3_BTN);
        profile4_BTN = findViewById(R.id.profile4_BTN);
        profile5_BTN = findViewById(R.id.profile5_BTN);
        profile_BTN = new Button[]{profile1_BTN, profile2_BTN, profile3_BTN, profile4_BTN, profile5_BTN};
    }
}