package com.upt.cti.photogmap.photographerfragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.upt.cti.photogmap.Constants;
import com.upt.cti.photogmap.FetchAddressIntentService;
import com.upt.cti.photogmap.MainActivityPhotographer;
import com.upt.cti.photogmap.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    FloatingActionButton fabChangeProfilePicture;
    ProgressBar progressBarLoadPicture;
    Button btnSaveLocation;
    private boolean gps_enable = false;
    private boolean network_enable = false;

    TextView tLocation;
    private ResultReceiver resultReceiver;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    ProgressBar progressBarLoadLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    public String latitude, longitude;
    private Geocoder geocoder;
    private List<Address> myAddress;




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
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        fabChangeProfilePicture = view.findViewById(R.id.fabChangeProfilePicture);
        progressBarLoadPicture = view.findViewById(R.id.progressBarLoadPicture);
        btnSaveLocation = view.findViewById(R.id.btnSaveLocation);
        tLocation = view.findViewById(R.id.tLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        resultReceiver = new AddressResultReceiver(new Handler());
        progressBarLoadLocation = view.findViewById(R.id.progressBarLoadLocation);
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) !=
              PackageManager.PERMISSION_GRANTED){
                  ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
              }
              else {
                  getCurrentLocation();
              }
            }
        });

        imgProfilePicture.setVisibility(View.INVISIBLE);
        fabChangeProfilePicture.setVisibility(View.INVISIBLE);
        progressBarLoadPicture.setVisibility(View.VISIBLE);

        StorageReference profileReference = storageReference.child("Users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(getActivity()).load(uri).into(imgProfilePicture);
                progressBarLoadPicture.setVisibility(View.INVISIBLE);
                imgProfilePicture.setVisibility(View.VISIBLE);
                fabChangeProfilePicture.setVisibility(View.VISIBLE);
            }
        });

        profileReference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarLoadPicture.setVisibility(View.INVISIBLE);
                imgProfilePicture.setVisibility(View.VISIBLE);
                fabChangeProfilePicture.setVisibility(View.VISIBLE);
            }
        });


        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
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
                tFirstName.setText(documentSnapshot.getString("firstName"));
                tLastName.setText(documentSnapshot.getString("lastName"));
                tEmail.setText(documentSnapshot.getString("mail"));
                tPhone.setText(documentSnapshot.getString("phoneNumber"));
                tRole.setText(documentSnapshot.getString("userType"));
            }
        });


        return view;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Permisiuni refuzate pentru locație", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        progressBarLoadLocation.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    tLocation.setText(String.valueOf(latitude) + "," + String.valueOf(longitude));


                    Location location = new Location("providerNA");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    fetchAddressFromLatLong(location);



                } else {
                    progressBarLoadLocation.setVisibility(View.GONE);
                }

                progressBarLoadLocation.setVisibility(View.VISIBLE);
            }
        }, Looper.getMainLooper());

    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        requireActivity().startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            System.out.println(resultCode);
            if (resultCode == Constants.SUCCESS_RESULT){

                tLocation.setText(resultData.getString(Constants.RESULT_DATA_KEY));


                String address = tLocation.getText().toString();

                String[] addressList = address.split(",");

                String county;
                String locality;
                String country;

                locality = addressList[0];
                county = addressList[1];
                country = addressList[2];


                String userID = firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("county", county);
                user.put("locality", locality);
                user.put("country", country);

                documentReference.update(user);

            }
            else {

                Toast.makeText(getActivity(),resultData.getString(Constants.RESULT_DATA_KEY),Toast.LENGTH_SHORT).show();
            }

            progressBarLoadLocation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imgUri = data.getData();
                imgProfilePicture.setImageURI(imgUri);

            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //imgProfilePicture.setImageBitmap(imageBitmap);


            File file = createImageFile();

            //get the uri from file created before
            if (file != null){
                FileOutputStream fout;
                try {
                    fout = new FileOutputStream(file);
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 70, fout);
                    fout.flush();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Uri imageUri = Uri.fromFile(file);
                uploadImageToFirebase(imageUri);
            }
        }

    }

    public File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        String root = getActivity().getDir("my_photo_dir", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File (root + "/Img");
        if (!myDir.exists()){
            myDir.mkdirs();
        }
        try {
            mFileTemp = File.createTempFile(imageFileName, ".jpg",myDir.getAbsoluteFile());
        } catch (IOException e1){
            e1.printStackTrace();
        }
        return mFileTemp;


    }



    private void uploadImageToFirebase(Uri imageUri) {

        StorageReference fileReference = storageReference.child("Users/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()+"/profile.jpg");
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Profile Image Uploaded...", Toast.LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getActivity()).load(uri).into(imgProfilePicture);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload profile image failed...", Toast.LENGTH_SHORT).show();
            }
        });
    }


}