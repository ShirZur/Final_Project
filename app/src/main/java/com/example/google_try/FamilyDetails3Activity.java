package com.example.google_try;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class FamilyDetails3Activity extends AppCompatActivity {

    private boolean baby, toddler, preschooler, student, adolescent, isHome, isBabysitterHome, isPets, isCooking
            ,isHW, isHouseChores;

    private String email,uid,family_name, number_phone, num_kids , hourly_wage, content_of_family;

    private Button back3_family_BTN, next3_family_BTN;

    private EditText content_family_editTXT;

    private TextView choose_from_library_family_TXT;

    private CircleImageView choose_profile_family_IMG;

    private String profilePicture;

    private Uri imagePath;

    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details_3);

        findViews();
        getTheIntent();
        setOnClick();

    }

    private void setOnClick() {

        choose_from_library_family_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/+");
                startActivityForResult(photoIntent,1);
            }
        });
        back3_family_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FamilyDetails3Activity.this, FamilyDetails2Activity.class));
            }
        });
        next3_family_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDetails();
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

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        choose_profile_family_IMG.setImageBitmap(bitmap);

    }

    private void putDetails() {
       content_of_family = content_family_editTXT.getText().toString();
       Family family = new Family();
       family.setContent_of_family(content_of_family);
       family.setEmail(email);
       family.setNumberPhone(number_phone);
       family.setFamily_name(family_name);
       family.setHome(isHome);
       family.setBabysitterHome(isBabysitterHome);
       family.setNumber_of_kids(num_kids);
       family.setAdolescent(adolescent);
       family.setToddle(toddler);
       family.setHourly_wage(hourly_wage);
       family.setPreschooler(preschooler);
       family.setStudent(student);
       family.setPets(isPets);
       family.setHw(isHW);
       family.setCooking(isCooking);
       family.setHouseChores(isHouseChores);
       family.setUid(uid);
       family.setProfilePicture(profilePicture);
       family.loadToDataBade();
        startActivity(new Intent(FamilyDetails3Activity.this,MainActivity.class));


    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
        family_name = getIntent().getStringExtra("FAMILY_NAME");
        number_phone = getIntent().getStringExtra("NUMBER_PHONE");
        num_kids = getIntent().getStringExtra("NUMBER_OF_KIDS");
        Log.d("ABCDEF",num_kids);
        baby = getIntent().getBooleanExtra("BABY",false);
        toddler = getIntent().getBooleanExtra("TODDLER",false);
        preschooler = getIntent().getBooleanExtra("PRESCHOOLER",false);
        student = getIntent().getBooleanExtra("STUDENT",false);
        adolescent = getIntent().getBooleanExtra("ADOLESCENT",false);
        hourly_wage = getIntent().getStringExtra("HOURLY_WAGE");
        isHome = getIntent().getBooleanExtra("HOME",false);
        isBabysitterHome = getIntent().getBooleanExtra("BABY_HOME",false);
        isPets = getIntent().getBooleanExtra("PETS",false);
        isCooking = getIntent().getBooleanExtra("HW",false);
        isHW = getIntent().getBooleanExtra("COOKING",false);
        isHouseChores = getIntent().getBooleanExtra("HOUSE",false);

    }

    private void findViews() {
        content_family_editTXT = findViewById(R.id.content_family_editTXT);
        choose_profile_family_IMG = findViewById(R.id.choose_profile_family_IMG);
        choose_from_library_family_TXT = findViewById(R.id.choose_from_library_family_TXT);
        back3_family_BTN = findViewById(R.id.back3_family_BTN);
        next3_family_BTN = findViewById(R.id.next3_family_BTN);
    }
}