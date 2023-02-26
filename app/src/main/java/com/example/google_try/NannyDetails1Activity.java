package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class NannyDetails1Activity extends AppCompatActivity {

    private EditText first_name_editTXT,last_name_editTXT, number_phone_editTXT, age_editTXT;
    private Button next1_BTN, back1_BTN;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_details_1);

        findViews();
        getTheIntent();
        setOnClick();

    }

    private void setOnClick() {
        next1_BTN.setOnClickListener(v -> checkDetails());
        back1_BTN.setOnClickListener(v-> logOut());
    }

    private void logOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(NannyDetails1Activity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkDetails() {
        String firstName = first_name_editTXT.getText().toString();
        String lastName = last_name_editTXT.getText().toString();
        String numberPhone = number_phone_editTXT.getText().toString();
        String age = age_editTXT.getText().toString();

        boolean isValidateData = validateData(firstName,lastName,numberPhone,age);
        if(!isValidateData){
            return;
        }

        putDetails(firstName,lastName,numberPhone,age);



    }

    private void putDetails(String firstName, String lastName, String numberPhone, String age) {
        Intent intent = new Intent(NannyDetails1Activity.this,NannyDetails2Activity.class);
        intent.putExtra("EMAIL",email);
        intent.putExtra("UID",uid);
        intent.putExtra("FIRST_NAME",firstName);
        intent.putExtra("LAST_NAME",lastName);
        intent.putExtra("NUMBER_PHONE",numberPhone);
        intent.putExtra("AGE",age);
        startActivity(intent);

    }

    private boolean validateData(String firstName, String lastName, String numberPhone, String age) {
        if(firstName.isEmpty() || lastName.isEmpty() || numberPhone.isEmpty() || age.isEmpty()){
            Utility.showToast(NannyDetails1Activity.this,"You must fill in all fields");
            return false;
        }
        return true;
    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
    }

    private void findViews() {
        first_name_editTXT = findViewById(R.id.first_name_editTXT);
        last_name_editTXT = findViewById(R.id.last_name_editTXT);
        number_phone_editTXT = findViewById(R.id.number_phone_editTXT);
        age_editTXT = findViewById(R.id.age_editTXT);
        next1_BTN = findViewById(R.id.next1_BTN);
        back1_BTN = findViewById(R.id.back1_BTN);

    }
}