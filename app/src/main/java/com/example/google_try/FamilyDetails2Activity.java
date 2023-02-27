package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;

public class FamilyDetails2Activity extends AppCompatActivity {

    private CheckedTextView  pets_checked, cooking_checked, hw_checked, house_checked;
    private Button back2_family_BTN, next2_family_BTN;

    private String email,uid,family_name, number_phone, num_kids , hourly_wage;

    private boolean baby, toddler, preschooler, student, adolescent;

    private Spinner hourly_wage_spinner;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details_2);

        findViews();
        getTheIntent();
        setOnClick();

        hourly_wage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourly_wage = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOnClick() {

        pets_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pets_checked.toggle();
            }
        });
        cooking_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooking_checked.toggle();
            }
        });
        hw_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hw_checked.toggle();
            }
        });
        house_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                house_checked.toggle();
            }
        });
        next2_family_BTN.setOnClickListener(v -> putDetails());
        back2_family_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FamilyDetails2Activity.this, FamilyDetails1Activity.class));
            }
        });

    }

    private void putDetails() {
        Intent intent = new Intent(FamilyDetails2Activity.this, FamilyDetails3Activity.class);
        intent.putExtra("EMAIL",email);
        intent.putExtra("HOURLY_WAGE",hourly_wage);
        intent.putExtra("UID",uid);
        intent.putExtra("FAMILY_NAME",family_name);
        intent.putExtra("NUMBER_PHONE",number_phone);
        intent.putExtra("NUMBER_OF_KIDS",num_kids);
        intent.putExtra("BABY",baby);
        intent.putExtra("TODDLER",toddler);
        intent.putExtra("PRESCHOOLER",preschooler);
        intent.putExtra("STUDENT",student);
        intent.putExtra("ADOLESCENT",adolescent);
        intent.putExtra("PETS",pets_checked.isChecked());
        intent.putExtra("HW",hw_checked.isChecked());
        intent.putExtra("COOKING",cooking_checked.isChecked());
        intent.putExtra("HOUSE",house_checked.isChecked());
        startActivity(intent);

    }

    private void getTheIntent() {

        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
        family_name = getIntent().getStringExtra("FAMILY_NAME");
        number_phone = getIntent().getStringExtra("NUMBER_PHONE");
        num_kids = getIntent().getStringExtra("NUMBER_OF_KIDS");
        baby = getIntent().getBooleanExtra("BABY",false);
        toddler = getIntent().getBooleanExtra("TODDLER",false);
        preschooler = getIntent().getBooleanExtra("PRESCHOOLER",false);
        student = getIntent().getBooleanExtra("STUDENT",false);
        adolescent = getIntent().getBooleanExtra("ADOLESCENT",false);


    }

    private void findViews() {
        pets_checked = findViewById(R.id.pets_checked);
        cooking_checked = findViewById(R.id.cooking_checked);
        hw_checked = findViewById(R.id.hw_checked);
        house_checked = findViewById(R.id.house_checked);
        back2_family_BTN = findViewById(R.id.back2_family_BTN);
        next2_family_BTN = findViewById(R.id.next2_family_BTN);
        hourly_wage_spinner = findViewById(R.id.hourly_wage_spinner);
    }
}