package com.upt.cti.photogmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

public class UploadPhotoActivity extends AppCompatActivity {

    EditText ePhotoName, ePhotoDescription;
    ImageView imgUploaded;
    Button btnAddPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);



        ePhotoName = findViewById(R.id.ePhotoName);
        ePhotoDescription = findViewById(R.id.ePhotoDescription);
        imgUploaded = findViewById(R.id.imgUploaded);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}