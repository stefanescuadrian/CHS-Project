package com.upt.cti.photogmap.clientfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Use the {@link GalleryPhotographerFragment_Client#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryPhotographerFragment_Client extends Fragment {
    private RecyclerView recyclerGalleryView;
    private ImageAdapter imgAdapter;

    private DatabaseReference databaseReference;
    private List<Upload> uploadListPhotos;
    private FirebaseAuth firebaseAuth;
    private String photographerId;
    private ProgressBar imgProgressCircle;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryPhotographerFragment_Client() {
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
    public static GalleryPhotographerFragment_Client newInstance(String param1, String param2) {
        GalleryPhotographerFragment_Client fragment = new GalleryPhotographerFragment_Client();
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

        photographerId =getArguments().getString("photographerId");
        System.out.println(photographerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_photographer_client, container, false);




        imgProgressCircle = view.findViewById(R.id.progressMyGallery);

        recyclerGalleryView = view.findViewById(R.id.recyclerGalleryView);
        recyclerGalleryView.setHasFixedSize(true);
        recyclerGalleryView.setLayoutManager(new LinearLayoutManager(getActivity()));

        uploadListPhotos = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://photog-map-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Gallery Uploads/"+ photographerId);

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