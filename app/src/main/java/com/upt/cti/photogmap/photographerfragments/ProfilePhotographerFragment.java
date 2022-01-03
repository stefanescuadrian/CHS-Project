package com.upt.cti.photogmap.photographerfragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    String userId;
    TextView tFirstName;
    TextView tLastName;

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
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        System.out.println(documentReference);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                assert value != null;
                tFirstName.setText(value.getString("Prenume"));
                tLastName.setText(value.getString("Nume de familie"));
            }
        });


        return view;
    }
    


}