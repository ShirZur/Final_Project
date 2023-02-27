package com.example.google_try;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsFragment extends Fragment {

    private Button Log_out_BTN, delete_account_BTN, change_password_BTN;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        findViews(view);
        setOnClick();


        return view;
    }

    private void setOnClick() {
        Log_out_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

       delete_account_BTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               FirebaseDatabase.getInstance().getReference().child("Nanny_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Intent intent = new Intent(getContext(), CreateUserActivity.class);
                                           startActivity(intent);
                                       }else{

                                       }
                                   }
                               });
                           }
                       });
           }
       });

       change_password_BTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.frame_layout,changePasswordFragment);
               fragmentTransaction.commit();
           }
       });
    }

    private void findViews(View view) {
        Log_out_BTN = view.findViewById(R.id.Log_out_BTN);
        delete_account_BTN = view.findViewById(R.id.delete_account_BTN);
        change_password_BTN = view.findViewById(R.id.change_password_BTN);

    }
}