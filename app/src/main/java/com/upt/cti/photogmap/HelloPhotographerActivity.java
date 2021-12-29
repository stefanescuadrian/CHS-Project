package com.upt.cti.photogmap;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HelloPhotographerActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_photographer);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(HelloPhotographerActivity.this, MainActivityPhotographer.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 3000);


    }
}