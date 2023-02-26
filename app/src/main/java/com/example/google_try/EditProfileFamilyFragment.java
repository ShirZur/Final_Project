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
import android.widget.ImageView;
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


public class EditProfileFamilyFragment extends Fragment {
    private TextView family_name_edit_profile, number_phone_family_edit_profile,  hourly_wage_TXT,content_family_edit;

   private CheckedTextView chacked_text_edit1, chacked_text_edit2, chacked_text_edit3 , chacked_text_edit4,chacked_text_edit5, home_checked_edit, babysitterhome_checked_edit, pets_checked_edit;

    private CheckedTextView cooking_checked_edit, hw_checked_edit, house_checked_edit;

   private Spinner hourly_wage_spinner_edit, num_of_kids_spinner;

   private Button done_edit_family_profile, cancel_edit_profile;

   private String hourly_wage, num_kids;

    private FirebaseAuth firebaseAuth;

    private CircleImageView family_profile_edit_IMG;
    private ProfileFragment profileFragment;

    private Uri imagePath;

    private TextView edit_family_profile_picture_TXT;

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

    public EditProfileFamilyFragment() {
        // Required empty public constructor
    }

    private static SetUid_Callback callback;

    public void setSetUid_callback(SetUid_Callback callback) {

        this.callback = callback;

    }




    public static EditProfileFamilyFragment newInstance(String param1, String param2) {
        EditProfileFamilyFragment fragment = new EditProfileFamilyFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile_family, container, false);
        findViews(view);
        getFamilyDataFromFirebase();
        setOnClick();


        return view;
    }

    private void setOnClick() {
       edit_family_profile_picture_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/+");
                startActivityForResult(photoIntent,1);

            }
        });

        chacked_text_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_edit1.toggle();
            }
        });

        chacked_text_edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_edit2.toggle();
            }
        });

        chacked_text_edit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_edit3.toggle();
            }
        });

        chacked_text_edit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_edit4.toggle();
            }
        });

        chacked_text_edit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_edit5.toggle();
            }
        });

        pets_checked_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pets_checked_edit.toggle();
            }
        });
        cooking_checked_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooking_checked_edit.toggle();
            }
        });
       hw_checked_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hw_checked_edit.toggle();
            }
        });
       house_checked_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house_checked_edit.toggle();
            }
        });

        hourly_wage_spinner_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourly_wage = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        num_of_kids_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num_kids = parent.getSelectedItem().toString();;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        done_edit_family_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/firstName").setValue(family_name_edit_profile.getText().toString());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastName").setValue(number_phone_family_edit_profile.getText().toString());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/content_of_family").setValue(content_family_edit.getText().toString());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/number_of_kids").setValue(num_kids);
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/hourly_wage").setValue(hourly_wage);
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/baby").setValue(chacked_text_edit1.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/toddle").setValue(chacked_text_edit2.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/preschooler").setValue(chacked_text_edit3.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/student").setValue(chacked_text_edit4.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/adolescent").setValue(chacked_text_edit5.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/pets").setValue(pets_checked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/cooking").setValue(cooking_checked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/hw").setValue(home_checked_edit.isChecked());
                FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/houseChores").setValue(house_checked_edit.isChecked());

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
        family_profile_edit_IMG.setImageBitmap(bitmap);
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
        FirebaseDatabase.getInstance().getReference("Family_User/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePicture").setValue(profilePicture);
    }


    private void getFamilyDataFromFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Family_User").child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).exists()){
                    databaseReference.child("Family_User").child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Family family = task.getResult().getValue(Family.class);

                            assert family != null;

                            family_name_edit_profile.setText(family.getFamily_name());
                            content_family_edit.setText(family.getContent_of_family());
                            //hourly_wage_TXT.setText(family.getHourly_wage() + ".00/hr");
                            int i = Integer.parseInt(family.getHourly_wage());
                            hourly_wage_spinner_edit.setSelection((i/10)-1);
                            int j = Integer.parseInt(family.getNumber_of_kids());
                            Log.d("AAA", family.getNumber_of_kids());
                            num_of_kids_spinner.setSelection(j-1);
                            number_phone_family_edit_profile.setText(family.getNumberPhone());
                            Picasso.get().load(family.getProfilePicture()).into(family_profile_edit_IMG);
                            chacked_text_edit1.setChecked((family.isBaby()));
                            chacked_text_edit2.setChecked(family.isToddle());
                            chacked_text_edit3.setChecked(family.isPreschooler());
                            chacked_text_edit4.setChecked(family.isStudent());
                            chacked_text_edit5.setChecked(family.isAdolescent());
                            cooking_checked_edit.setChecked(family.isCooking());
                            hw_checked_edit.setChecked(family.isHw());
                            house_checked_edit.setChecked(family.isHouseChores());
                            pets_checked_edit.setChecked(family.isPets());

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
        family_name_edit_profile = view.findViewById(R.id.family_name_edit_profile);
        number_phone_family_edit_profile = view.findViewById(R.id.number_phone_family_edit_profile);
        num_of_kids_spinner = view.findViewById(R.id.num_of_kids_spinner);
        hourly_wage_spinner_edit  = view. findViewById(R.id.hourly_wage_spinner_edit);
        content_family_edit = view.findViewById(R.id.content_family_edit);
        chacked_text_edit1 = view.findViewById(R.id.chacked_text_edit1);
        chacked_text_edit2 = view.findViewById(R.id.chacked_text_edit2);
        chacked_text_edit3 = view.findViewById(R.id.chacked_text_edit3);
        chacked_text_edit4 = view.findViewById(R.id.chacked_text_edit4);
        chacked_text_edit5 = view.findViewById(R.id.chacked_text_edit5);
        home_checked_edit = view.findViewById(R.id.home_checked_edit);
        babysitterhome_checked_edit = view.findViewById(R.id.babysitterhome_checked_edit);
        pets_checked_edit = view.findViewById(R.id.pets_checked_edit);
        cooking_checked_edit = view.findViewById(R.id.cooking_checked_edit);
        hw_checked_edit = view.findViewById(R.id.hw_checked_edit);
        house_checked_edit = view.findViewById(R.id.house_checked_edit);
        family_profile_edit_IMG = view.findViewById(R.id.family_profile_edit_IMG);
        edit_family_profile_picture_TXT = view.findViewById(R.id.edit_family_profile_picture_TXT);
        done_edit_family_profile = view.findViewById(R.id.done);
        cancel_edit_profile = view.findViewById(R.id.cancel_edit_profile);


    }
}