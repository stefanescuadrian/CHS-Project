package com.upt.cti.photogmap.photographerfragments;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.photogmap.ImageAdapter;
import com.upt.cti.photogmap.R;
import com.upt.cti.photogmap.Upload;
import com.upt.cti.photogmap.UploadPhotoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryPhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryPhotographerFragment extends Fragment {
    private RecyclerView recyclerGalleryView;
    private ImageAdapter imgAdapter;

    private DatabaseReference databaseReference;
    private List<Upload> uploadListPhotos;
    private FirebaseAuth firebaseAuth;
    private ProgressBar imgProgressCircle;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryPhotographerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryPhotographerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryPhotographerFragment newInstance(String param1, String param2) {
        GalleryPhotographerFragment fragment = new GalleryPhotographerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_photographer, container, false);
        FloatingActionButton fabAddPhotosToMyGallery = view.findViewById(R.id.fabAddPhotosToMyGallery);


        fabAddPhotosToMyGallery.setOnClickListener(v -> {
            uploadListPhotos.clear();
            getFragmentManager().beginTransaction().remove(this).commit();

            Intent intent = new Intent(getActivity(), UploadPhotoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);

        });

        imgProgressCircle = view.findViewById(R.id.progressMyGallery);

        recyclerGalleryView = view.findViewById(R.id.recyclerGalleryView);
        recyclerGalleryView.setHasFixedSize(true);
        recyclerGalleryView.setLayoutManager(new LinearLayoutManager(getActivity()));

        uploadListPhotos = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://photog-map-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Gallery Uploads/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadListPhotos.add(upload);
                }

                imgAdapter = new ImageAdapter(getActivity(),uploadListPhotos);

                recyclerGalleryView.setAdapter(imgAdapter);
                imgProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
                imgProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }
}