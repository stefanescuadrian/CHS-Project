package com.upt.cti.photogmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivityClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_main_client);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //logout user from application
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}