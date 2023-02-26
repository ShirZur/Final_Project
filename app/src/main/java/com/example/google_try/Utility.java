package com.example.google_try;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForFindBabysitterProfile(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("findProfile").document(currentUser.getUid()).collection("theProfile");
    }

    static boolean isNull(Context context,String str, String message){
        if(str.isEmpty()){
            showToast(context,message);
            return false;
        }
        return true;
    }
}
