package com.upt.cti.photogmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.upt.cti.photogmap.clientfragments.MapClientFragment;
import com.upt.cti.photogmap.photographerfragments.RankPhotographerFragment;

import java.util.Objects;

public class MainActivityClient extends AppCompatActivity {
    private static int lastSelectedItem = 0;
    BottomNavigationView navClient;
    MapClientFragment mapClientFragment = new MapClientFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); //hide the title bar
        setContentView(R.layout.activity_main_client);
        navClient = findViewById(R.id.navClient);

        navClient.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {

                        case R.id.ic_rank:
                            lastSelectedItem = 0;
                            getSupportFragmentManager().beginTransaction().replace(R.id.containerClient, mapClientFragment).commit();
                            return true;

                        case R.id.ic_favorites:

                            return true;

                        case R.id.ic_logout:
                            lastSelectedItem = 2;
                            FirebaseAuth.getInstance().signOut(); //logout user from application
                            FirebaseFirestore.getInstance().terminate();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                            finish();
                            return true;
                    }
                    return false;
                }
        );
        navClient.setSelectedItemId(R.id.ic_profile);
        navClient.setItemIconTintList(null);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}