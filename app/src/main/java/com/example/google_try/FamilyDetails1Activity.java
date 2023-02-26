package com.example.google_try;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

public class FamilyDetails1Activity extends AppCompatActivity {

    private Spinner num_of_kids_spinner;

    private CheckedTextView chacked_text_view1, chacked_text_view2, chacked_text_view3, chacked_text_view4, chacked_text_view5;

    private Button next1_family_BTN, back1_family_BTN;

    private EditText number_phone_family_edit_TXT, family_name_edit_TXT;

    private String email, uid, num_kids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details_1);

        findViews();
        getTheIntent();
        setOnClick();

        num_of_kids_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               num_kids = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void getTheIntent() {
        email = getIntent().getStringExtra("EMAIL");
        uid = getIntent().getStringExtra("UID");
    }

    private void setOnClick() {

        chacked_text_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_view1.toggle();
            }
        });

        chacked_text_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_view2.toggle();
            }
        });

        chacked_text_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_view3.toggle();
            }
        });

        chacked_text_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_view4.toggle();
            }
        });

        chacked_text_view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chacked_text_view5.toggle();
            }
        });

        next1_family_BTN.setOnClickListener(v -> checkDetails());
    }

    private void checkDetails() {
        
            String family_name = family_name_edit_TXT.getText().toString();
            String number_phone = number_phone_family_edit_TXT.getText().toString();
            

            boolean isValidateData = validateData(family_name,number_phone);
            if(!isValidateData){
                return;
            }
            putDetails(family_name,number_phone);
            
    }

    private void putDetails(String family_name, String number_phone) {
        Intent intent = new Intent(FamilyDetails1Activity.this,FamilyDetails2Activity.class);
        intent.putExtra("EMAIL",email);
        intent.putExtra("UID",uid);
        intent.putExtra("FAMILY_NAME",family_name);
        intent.putExtra("NUMBER_PHONE",number_phone);
        intent.putExtra("NUMBER_OF_KIDS",num_kids);
        intent.putExtra("BABY",chacked_text_view1.isChecked());
        intent.putExtra("TODDLER",chacked_text_view2.isChecked());
        intent.putExtra("PRESCHOOLER",chacked_text_view3.isChecked());
        intent.putExtra("STUDENT",chacked_text_view4.isChecked());
        intent.putExtra("ADOLESCENT",chacked_text_view5.isChecked());
        startActivity(intent);

    }

    private boolean validateData(String family_name, String number_phone) {
        if(family_name.isEmpty() || number_phone.isEmpty() ){
            Utility.showToast(FamilyDetails1Activity.this,"You must fill in all fields");
            return false;
        }
        return true;
    }

    private void findViews() {

        num_of_kids_spinner=findViewById(R.id.num_of_kids_spinner);
        chacked_text_view1 = findViewById(R.id.chacked_text_view1);
        chacked_text_view2 = findViewById(R.id.chacked_text_view2);
        chacked_text_view3 = findViewById(R.id.chacked_text_view3);
        chacked_text_view4 = findViewById(R.id.chacked_text_view4);
        chacked_text_view5 = findViewById(R.id.chacked_text_view5);
        next1_family_BTN = findViewById(R.id.next1_family_BTN);
        back1_family_BTN = findViewById(R.id.back1_family_BTN);
        number_phone_family_edit_TXT = findViewById(R.id.number_phone_family_edit_TXT);
        family_name_edit_TXT = findViewById(R.id.family_name_edit_TXT);


    }
}