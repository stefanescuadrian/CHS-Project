package com.upt.cti.photogmap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HelloClientActivity extends AppCompatActivity {

    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_client);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(HelloClientActivity.this, MainActivityClient.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 3000);
    }
}