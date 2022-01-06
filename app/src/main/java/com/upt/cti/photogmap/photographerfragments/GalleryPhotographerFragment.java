package com.upt.cti.photogmap.photographerfragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.upt.cti.photogmap.R;
import com.upt.cti.photogmap.UploadPhotoActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryPhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryPhotographerFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    FloatingActionButton fabAddPhotosToMyGallery;


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
        fabAddPhotosToMyGallery = view.findViewById(R.id.fabAddPhotosToMyGallery);


        fabAddPhotosToMyGallery.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UploadPhotoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);

        });


        return view;
    }
}