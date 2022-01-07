package com.upt.cti.photogmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.upt.cti.photogmap.photographerfragments.GalleryPhotographerFragment;

import java.util.Objects;

public class UploadPhotoActivity extends AppCompatActivity {

    public static int PICK_IMAGE_REQUEST = 1;

    private EditText ePhotoName, ePhotoDescription;
    private ImageView imgUploaded;
    private Button btnAddPhoto, btnAddAnotherPhoto;
    private Uri imgUploadedUri;
    ProgressBar progressBarUploadPhoto;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        ePhotoName = findViewById(R.id.ePhotoName);
        ePhotoDescription = findViewById(R.id.ePhotoDescription);
        imgUploaded = findViewById(R.id.imgUploaded);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnAddAnotherPhoto = findViewById(R.id.btnAddAnotherPhoto);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBarUploadPhoto = findViewById(R.id.progressBarUploadPhoto);


        storageReference = FirebaseStorage.getInstance().getReference("Gallery Uploads/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        databaseReference = FirebaseDatabase.getInstance("https://photog-map-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Gallery Uploads/"+firebaseAuth.getCurrentUser().getUid());


      //  StorageReference profileReference = storageReference.child("Users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);



        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storageUploadTask != null && storageUploadTask.isInProgress()) {
                    Toast.makeText(UploadPhotoActivity.this, "Se adaugă fotografia...", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPhoto();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();


                        }
                    }, 3000);
                }
            }
        });

    }

    private void uploadPhoto() {
        if (imgUploadedUri != null){
            StorageReference photoReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUploadedUri));
            storageUploadTask = photoReference.putFile(imgUploadedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBarUploadPhoto.setProgress(0);
                        }
                    }, 3000);

                    Toast.makeText(UploadPhotoActivity.this, "Poza a fost adăugată...", Toast.LENGTH_LONG).show();

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            Upload upload = new Upload(ePhotoName.getText().toString().trim(), ePhotoDescription.getText().toString().trim(),photoLink);

                                    String uploadID = databaseReference.push().getKey();
                            assert uploadID != null;
                            databaseReference.child(uploadID).setValue(upload);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadPhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBarUploadPhoto.setProgress((int) progress);
                }
            });
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri imgUploadedUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgUploadedUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUploadedUri = data.getData();
            Picasso.with(this).load(imgUploadedUri).into(imgUploaded);
        }

    }

}