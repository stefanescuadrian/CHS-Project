package com.upt.cti.photogmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.upt.cti.photogmap.photographerfragments.MapPhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.ProfilePhotographerFragment;

import java.util.Objects;

public class MainActivityPhotographer extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_main_photographer);

        BottomNavigationView navClient;

        Fragment profilePhotographerFragment = new ProfilePhotographerFragment();
        Fragment mapFragment = new MapPhotographerFragment();

        navClient = findViewById(R.id.navClient);
        navClient.setItemIconTintList(null);
      //  navClient.setSelectedItemId(R.id.ic_profile);

//        navClient.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.ic_profile:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profilePhotographerFragment).commit();
//                    return true;
//
//                case R.id.ic_2:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
//                    return true;
//            }
//            return false;
//        });

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); //logout user from application
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


}