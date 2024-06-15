package com.example.propfinder.Firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FirebaseSignIn {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseUser user = auth.getCurrentUser();
    public static FirebaseAuth getAuth() {
        return auth;
    }
    public static void signIn(FirebaseAuth auth, String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Sign_in successful");
                    setUser();
                } else {
                    Log.d(TAG, "Error Signing_in");
                }
            }
        });
    }
    private static void setUser(){
        user = auth.getCurrentUser();
    }
    public static String getEmail(){
        String email = user.getEmail();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
}
