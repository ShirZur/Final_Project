package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class NannyDetails2Activity extends AppCompatActivity {


    private String email,uid, firstName, lastName, numberPhone, age,hourlyRate;

    private Boolean isSmoke , drivingLicense, experience;
    private CheckedTextView smoker_chacked_text_view, license_chacked_text_view, experience_chacked_text_view;
    private Spinner hourly_rate_spinner;

    private Button hebrew_BTN, english_BTN, arabic_BTN, spanish_BTN, franch_BTN, next2_BTN, back2_BTN;

    private boolean hebrew, arabic, english, spanish, french = false;


    private List<String> languages = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nanny_details_2);
        isSmoke = false;
        drivingLicense = false;
        experience = false;

        findViews();
        getTheIntent();
        setOnClick();


        hourly_rate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourlyRate = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setOnClick() {
        hebrew_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hebrew){
                    hebrew = false;
                    hebrew_BTN.setBackgroundColor(Color.BLUE);
                    languages.remove("HEBREW");
                }
                else{
                    hebrew = true;
                    hebrew_BTN.setBackgroundColor(Color.BLACK);
                    languages.add("HEBREW");
                }
            }
        });

        english_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(english){
                    english = false;
                    english_BTN.setBackgroundColor(Color.BLUE);
                    languages.remove("ENGLISH");
                }
                else{
                    english = true;
                    english_BTN.setBackgroundColor(Color.BLACK);
                    languages.add("ENGLISH");
                }
            }
        });

        arabic_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arabic){
                    arabic = false;
                    arabic_BTN.setBackgroundColor(Color.BLUE);
                    languages.remove("ARABIC");
                }
                arabic_BTN.setBackgroundColor(Color.BLACK);
                languages.add("ARABIC");
            }
        });

        spanish_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spanish_BTN.setBackgroundColor(Color.BLACK);
                languages.add("SPANISH");
            }
        });

        franch_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                franch_BTN.setBackgroundColor(Color.BLACK);
                languages.add("FRENCH");
            }
        });

        smoker_chacked_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoker_chacked_text_view.toggle();
                isSmoke = smoker_chacked_text_view.isChecked();
            }
        });

        license_chacked_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                license_chacked_text_view.toggle();
                drivingLicense = license_chacked_text_view.isChecked();
            }
        });

        experience_chacked_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experience_chacked_text_view.toggle();
                experience = experience_chacked_text_view.isChecked();
            }
        });

        next2_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDetails();
            }
        });

        back2_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NannyDetails2Activity.this,NannyDetails1Activity.class));
            }
        });




    }

    private void putDetails() {
        Intent intent = new Intent(NannyDetails2Activity.this,NannyDetails3Activity.class);
        intent.putExtra("EMAIL",email);
        intent.putExtra("UID",uid);
        intent.putExtra("FIRST_NAME",firstName);
        intent.putExtra("LAST_NAME",lastName);
        intent.putExtra("NUMBER_PHONE",numberPhone);
        intent.putExtra("AGE",age);
        intent.putExtra("HOURLY_RATE",hourlyRate);
        intent.putStringArrayListExtra("LANGUAGES", (ArrayList<String>) languages);
        intent.putExtra("SMOKER",isSmoke);
        intent.putExtra("LICENSE", drivingLicense);
        intent.putExtra("EXPERIENCE", experience);
        startActivity(intent);





    }



    private void findViews() {
        smoker_chacked_text_view = findViewById(R.id.smoker_chacked_text_view);
        license_chacked_text_view = findViewById(R.id.license_chacked_text_view);
        experience_chacked_text_view = findViewById(R.id.experience_chacked_text_view);
        hourly_rate_spinner = (Spinner)findViewById(R.id.hourly_rate_spinner);
        hebrew_BTN = findViewById(R.id.hebrew_BTN);
        english_BTN = findViewById(R.id.english_BTN);
        arabic_BTN = findViewById(R.id.arabic_BTN);
        spanish_BTN = findViewById(R.id.spanish_BTN);
        franch_BTN = findViewById(R.id.frach_BTN);
        next2_BTN = findViewById(R.id.next2_BTN);
        back2_BTN = findViewById(R.id.back2_BTN);


    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        numberPhone = getIntent().getStringExtra("NUMBER_PHONE");
        age = getIntent().getStringExtra("AGE");


    }
}