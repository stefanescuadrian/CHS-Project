package com.upt.cti.photogmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.upt.cti.photogmap.photographerfragments.GalleryPhotographerFragment;
import com.upt.cti.photogmap.clientfragments.VotePhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.ProfilePhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.RankPhotographerFragment;

import java.util.Objects;

public class MainActivityPhotographer extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private static int lastSelectedItem = 0;
    BottomNavigationView navPhotographer;
    ProfilePhotographerFragment profilePhotographerFragment = new ProfilePhotographerFragment();
    VotePhotographerFragment mapFragment = new VotePhotographerFragment();
    GalleryPhotographerFragment myGalleryFragment = new GalleryPhotographerFragment();
    RankPhotographerFragment rankPhotographerFragment = new RankPhotographerFragment();



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
                    lastSelectedItem = 0;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profilePhotographerFragment).commit();
                    return true;

                case R.id.ic_2:
                    lastSelectedItem = 2;
                    FirebaseAuth.getInstance().signOut(); //logout user from application
                    FirebaseFirestore.getInstance().terminate();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.ic_my_gallery:
                    lastSelectedItem = 1;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myGalleryFragment).commit();
                    return true;
                case R.id.ic_rank:
                    lastSelectedItem = 3;
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, rankPhotographerFragment).commit();
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
protected void onRestart() {

    super.onRestart();
    switch (lastSelectedItem){
        case 0:
            navPhotographer.setSelectedItemId(R.id.ic_profile);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, profilePhotographerFragment).commit();
            break;
        case 1:
            navPhotographer.setSelectedItemId(R.id.ic_my_gallery);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, myGalleryFragment).commit();
            break;
    }


}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}