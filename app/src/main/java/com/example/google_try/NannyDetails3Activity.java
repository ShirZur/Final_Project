package com.example.google_try;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import im.delight.android.location.SimpleLocation;

public class NannyDetails3Activity extends AppCompatActivity {

    private String email, uid, firstName, lastName, numberPhone, age, hourlyRate, content_nanny;

    private Boolean isSmoke, drivingLicense, experience;
    private List<String> languages = new ArrayList<String>();

    private EditText content_nanny_editTXT;

    private Button next3_BTN, back3_BTN;

    private TextView choose_from_library_TXT;

    private Uri imagePath;

    private CircleImageView  choose_profile_IMG;

    private String profilePicture;

    private double lat = 0.0;

    private double lon = 0.0;

    private SimpleLocation simpleLocation;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_details_3);


        findViews();
        getTheIntent();
        simpleLocation = new SimpleLocation(getApplicationContext());
        findLocation(simpleLocation);
        setOnClick();
    }

    private void findLocation(SimpleLocation simpleLocation) {
        simpleLocation.beginUpdates();
        this.lat = simpleLocation.getLatitude();
        this.lon = simpleLocation.getLongitude();
    }


    private void setOnClick() {
        next3_BTN.setOnClickListener(v -> putDetails());
        choose_from_library_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/+");
                startActivityForResult(photoIntent,1);

            }
        });

        back3_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NannyDetails3Activity.this,NannyDetails2Activity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
           imagePath = data.getData();
            uploadImage();
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
        choose_profile_IMG.setImageBitmap(bitmap);

    }

    private void putDetails() {
        content_nanny = content_nanny_editTXT.getText().toString();
        Nanny nanny = new Nanny();
        nanny.setFirstName(firstName);
        nanny.setLastName(lastName);
        nanny.setEmail(email);
        nanny.setUid(uid);
        nanny.setNumberPhone(numberPhone);
        nanny.setAge(age);
        nanny.setPerHour(hourlyRate);
        nanny.setLanguages(languages);
        nanny.setExperienceWithChildren(experience);
        nanny.setSmoker(isSmoke);
        nanny.setHasDrivingLicense(drivingLicense);
        nanny.setContentAboutNanny(content_nanny);
        nanny.setLat(lat);
        nanny.setLon(lon);
        nanny.setProfilePicture(profilePicture);
        nanny.loadToDataBade();
        startActivity(new Intent(NannyDetails3Activity.this,MainActivity.class));
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                profilePicture = task.getResult().toString();
                               // updateProfileImage(task.getResult().toString());
                            }
                        }
                    });
                }else{

                }
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                progressDialog.setMessage(" Uploaded " + (int)progress + "%");
            }
        });
    }

    private void updateProfileImage(String profilePicture) {
        FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePicture").setValue(profilePicture);
    }

    private void findViews() {
        content_nanny_editTXT = findViewById(R.id.content_nanny_editTXT);
        next3_BTN = findViewById(R.id.next3_BTN);
        back3_BTN = findViewById(R.id.back3_BTN);
        choose_from_library_TXT = findViewById(R.id.choose_from_library_TXT);
        choose_profile_IMG = findViewById(R.id.choose_profile_IMG);
    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        numberPhone = getIntent().getStringExtra("NUMBER_PHONE");
        age = getIntent().getStringExtra("AGE");
        hourlyRate = getIntent().getStringExtra("HOURLY_RATE");
        languages = getIntent().getStringArrayListExtra("LANGUAGES");
        isSmoke = getIntent().getBooleanExtra("SMOKER",false);
        drivingLicense = getIntent().getBooleanExtra("LICENSE",false);
        experience = getIntent().getBooleanExtra("EXPERIENCE",false);

    }
}