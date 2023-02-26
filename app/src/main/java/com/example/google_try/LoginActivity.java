package com.example.google_try;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView create_account_TXT;
    private Button login_BTN;
    private EditText  email_login_editTXT;

    private TextInputEditText password_text_input;
    private TextInputLayout password_login_editTXT;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        requestLocationPermissions();
        setOnClick();





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


    private void setOnClick() {
        create_account_TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateUserActivity.class));
            }
        });

        login_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                String email = email_login_editTXT.getText().toString();
                String password = password_text_input.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    email_login_editTXT.setError("Email is invalid");
                }
                if(password.length() < 6){
                    password_text_input.setError("Password length is invalid");
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                            //progressDialog.setTitle("Uploading");
                            progressDialog.show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }else{
                            Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                        }
                    });
                }



            }
        });
    }

    private void findViews() {
        create_account_TXT = findViewById(R.id.create_account_TXT);
        login_BTN = findViewById(R.id.login_BTN);
        email_login_editTXT = findViewById(R.id.email_login_editTXT);
        password_login_editTXT = findViewById(R.id.password_login_editTXT);
        password_text_input = findViewById(R.id.password_text_input);
    }
}