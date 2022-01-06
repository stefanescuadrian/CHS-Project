package com.upt.cti.photogmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.upt.cti.photogmap.photographerfragments.GalleryPhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.MapPhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.ProfilePhotographerFragment;

import java.util.Objects;

public class MainActivityPhotographer extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView navPhotographer;
    ProfilePhotographerFragment profilePhotographerFragment = new ProfilePhotographerFragment();
    MapPhotographerFragment mapFragment = new MapPhotographerFragment();
    GalleryPhotographerFragment myGalleryFragment = new GalleryPhotographerFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_main_photographer);



        navPhotographer = findViewById(R.id.navPhotographer);

        navPhotographer.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.ic_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profilePhotographerFragment).commit();
                    return true;

                case R.id.ic_2:
                    FirebaseAuth.getInstance().signOut(); //logout user from application
                    FirebaseFirestore.getInstance().terminate();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.ic_3:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myGalleryFragment).commit();
                    return true;
            }
            return false;
                }
        );
        navPhotographer.setSelectedItemId(R.id.ic_profile);
        navPhotographer.setItemIconTintList(null);

    }

//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut(); //logout user from application
//        FirebaseFirestore.getInstance().terminate();
//        Intent intent = new Intent(getApplicationContext(), Login.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
//        finish();
//    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}