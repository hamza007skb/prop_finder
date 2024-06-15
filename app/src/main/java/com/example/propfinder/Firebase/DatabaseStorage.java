package com.example.propfinder.Firebase;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseStorage {
    private static final String TAG = "DatabaseStorage";
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final StorageReference ref = storage.getReference();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Uri[] currentUri = new Uri[1];

    public interface UploadCallback {
        void onUploadSuccess(Uri uri);
        void onUploadFailure(Exception e);
    }

    public static void storeImage(Uri uri, UploadCallback callback) {
        String fileName = "img" + System.currentTimeMillis() + ".jpg";
        StorageReference imgRef = ref.child("IMAGES").child(fileName);
        imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Image Uploaded Successfully!!");
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Firebase", "Download URI: " + uri.toString());
                        callback.onUploadSuccess(uri);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase", "Failed to upload image", e);
                callback.onUploadFailure(e);
            }
        });
    }
        public static Uri getCurrentUri() {
        return currentUri[0];
    }
}


