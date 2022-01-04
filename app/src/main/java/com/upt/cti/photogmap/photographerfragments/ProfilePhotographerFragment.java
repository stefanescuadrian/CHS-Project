package com.upt.cti.photogmap.photographerfragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upt.cti.photogmap.MainActivityPhotographer;
import com.upt.cti.photogmap.R;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePhotographerFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    TextView tFirstName;
    TextView tLastName;
    TextView tEmail, tRole, tPhone;
    ImageView imgProfilePicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilePhotographerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePhotographerFragment newInstance(String param1, String param2) {
        ProfilePhotographerFragment fragment = new ProfilePhotographerFragment();
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
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_photographer, container, false);
        tFirstName = view.findViewById(R.id.tFirstNameProfile);
        tLastName = view.findViewById(R.id.tLastNameProfile);
        tEmail = view.findViewById(R.id.tEmail);
        tRole = view.findViewById(R.id.tRole);
        tPhone = view.findViewById(R.id.tPhone);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        storageReference = FirebaseStorage.getInstance().getReference();



        imgProfilePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

//                //open gallery when click on profile photo
//                Intent openGalleryIntent = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGalleryIntent, 1000);

            }
        });

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        System.out.println(documentReference);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                assert documentSnapshot != null;
                tFirstName.setText(documentSnapshot.getString("Prenume"));
                tLastName.setText(documentSnapshot.getString("Nume de familie"));
                tEmail.setText(documentSnapshot.getString("Adresă de mail"));
                tPhone.setText(documentSnapshot.getString("Număr de telefon"));
                tRole.setText(documentSnapshot.getString("Tip utilizator"));
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imgUri = data.getData();
                imgProfilePicture.setImageURI(imgUri);

            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgProfilePicture.setImageBitmap(imageBitmap);
        }

    }



}