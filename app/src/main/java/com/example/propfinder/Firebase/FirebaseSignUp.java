package com.example.propfinder.Firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.propfinder.businessLogic.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSignUp {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    public static void createUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Successfully created account");
                } else {
                    Log.d(TAG, "error making account");
                }
            }
        });
    }
    public static FirebaseAuth getAuth() {
        return auth;
    }
}
