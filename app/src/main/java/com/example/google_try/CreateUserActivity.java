package com.example.google_try;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {

    private TextView login_TXT;
    private EditText email_sign_editTXT;

    private TextInputLayout password_login_layout;

    private TextInputEditText password_text_input, confirm_password_text_input;
    private Button create_account_BTN;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        findViews();
        requestLocationPermissions();
        setOnClick();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setOnClick() {
        login_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateUserActivity.this,LoginActivity.class));
            }
        });

        create_account_BTN.setOnClickListener(v -> createAccount());


    }

    private void findViews() {
        login_TXT = findViewById(R.id.login_TXT);
        email_sign_editTXT = findViewById(R.id.email_sign_editTXT);
        password_text_input = findViewById(R.id.password_text_input);
        password_login_layout = findViewById(R.id.password_login_layout);
        confirm_password_text_input = findViewById(R.id.confirm_password_text_input);
        create_account_BTN = findViewById(R.id.create_account_BTN);

    }

    void createAccount(){
        String email = email_sign_editTXT.getText().toString();
        String password = password_text_input.getText().toString();
        String confirm_password = confirm_password_text_input.getText().toString();

        boolean isValidated = validateData(email,password,confirm_password);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);



    }

    private void createAccountInFirebase(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String uid =  Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid() + "";
                Utility.showToast(CreateUserActivity.this,"Succesfully create account");
                Intent intent = new Intent(CreateUserActivity.this, NannyOrJobActivity.class);
                intent.putExtra("EMAIL",email);
                intent.putExtra("UID",uid);
                startActivity(intent);
                finish();

            }else{
                Utility.showToast(CreateUserActivity.this, task.getException().getLocalizedMessage());
            }
        });

    }

    boolean validateData(String email, String password, String confirm_password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_sign_editTXT.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            password_text_input.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(confirm_password)) {
            confirm_password_text_input.setError("Password not matched");
            return false;
        }
        if (!Utility.isNull(this, email, "add email") || !Utility.isNull(this, password, "add password") || !Utility.isNull(this, confirm_password, "add confirm password")) {
            return false;
        }
        return true;
    }

    private void requestLocationPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                Log.d("permission", "fine location granted");
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d("permission", "coarse location granted");
                            } else {
                                Log.d("permission", "location permission denied");
                            }
                        }
                );

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }



}