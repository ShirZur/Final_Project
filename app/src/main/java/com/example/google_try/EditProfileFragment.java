package com.example.google_try;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {

    private EditText email_edit, first_name_edit, last_name_edit,number_phone_edit, age_edit, content_nanny_edit;

    private Spinner hourly_rate_spinner_edit;

    private String hourly_rate;

    private CheckedTextView smoker_chacked_edit, license_chacked_edit, experience_chacked_edit;

    private Button done_edit_profile, cancel_edit_profile;


    private FirebaseAuth firebaseAuth;

    private CircleImageView profile_edit_IMG;

    private Uri imagePath;
    private ProfileFragment profileFragment;

    private TextView edit_profile_picture_TXT;

    private String profilePicture;



    private SetUid_Callback setUid_callback = new SetUid_Callback() {
        @Override
        public void setUid(String uid) {
            Fragment fragment = new ProfileFragment();
            profileFragment.setUid(uid);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    };




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private static SetUid_Callback callback;

    public void setSetUid_callback(SetUid_Callback callback) {

        this.callback = callback;

    }

    public EditProfileFragment() {

    }



    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
       View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);
       findViews(view);
       getNannyDataFromFirebase();
       setOnClick();
       return view;
    }

    private void setOnClick() {

        edit_profile_picture_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/+");
                startActivityForResult(photoIntent,1);

            }
        });

        hourly_rate_spinner_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourly_rate = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        done_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/firstName").setValue(first_name_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastName").setValue(last_name_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/email").setValue(email_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/numberPhone").setValue(number_phone_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/contentAboutNanny").setValue(content_nanny_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/age").setValue(age_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/isSmoker").setValue(smoker_chacked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/experienceWithChildren").setValue(experience_chacked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/hasDrivingLicense").setValue(license_chacked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Nanny_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/perHour").setValue(hourly_rate);
                callback.setUid(firebaseAuth.getCurrentUser().getUid());
            }
        });
        cancel_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.setUid(firebaseAuth.getCurrentUser().getUid());
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profile_edit_IMG.setImageBitmap(bitmap);
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                                updateProfileImage(task.getResult().toString());
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

    private void findViews(View view) {
        email_edit = view.findViewById(R.id.email_edit);
        first_name_edit = view.findViewById(R.id.first_name_edit);
        last_name_edit = view.findViewById(R.id.last_name_edit);
        number_phone_edit = view.findViewById(R.id.number_phone_edit);
        age_edit = view.findViewById(R.id.age_edit);
        hourly_rate_spinner_edit = view.findViewById(R.id.hourly_rate_spinner_edit);
        smoker_chacked_edit = view.findViewById(R.id.smoker_chacked_edit);
        license_chacked_edit = view.findViewById(R.id.license_chacked_edit);
        experience_chacked_edit = view.findViewById(R.id.experience_chacked_edit);
        done_edit_profile = view.findViewById(R.id.done_edit_profile);
        profile_edit_IMG = view.findViewById(R.id.profile_edit_IMG);
        content_nanny_edit = view.findViewById(R.id.content_nanny_edit);
        edit_profile_picture_TXT = view.findViewById(R.id.edit_profile_picture_TXT);
        cancel_edit_profile = view.findViewById(R.id.cancel_edit_profile);
        done_edit_profile = view.findViewById(R.id.done_edit_profile);


    }

    private void getNannyDataFromFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Nanny_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()){
                    databaseReference.child("Nanny_User").child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Nanny nanny = task.getResult().getValue(Nanny.class);

                            assert nanny != null;

                            Picasso.get().load(nanny.getProfilePicture()).into(profile_edit_IMG);
                            email_edit.setText(nanny.getEmail());
                            first_name_edit.setText(nanny.getFirstName());
                            last_name_edit.setText(nanny.getLastName());
                            age_edit.setText(nanny.getAge());
                            number_phone_edit.setText(nanny.getNumberPhone());
                            int i = Integer.parseInt(nanny.getPerHour());
                            hourly_rate_spinner_edit.setSelection((i/10)-1);
                            smoker_chacked_edit.setChecked(nanny.isSmoker());
                            license_chacked_edit.setChecked(nanny.isHasDrivingLicense());
                            experience_chacked_edit.setChecked(nanny.isExperienceWithChildren());
                            content_nanny_edit.setText(nanny.getContentAboutNanny());


                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}