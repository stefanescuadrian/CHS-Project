package com.upt.cti.photogmap.photographerfragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.upt.cti.photogmap.Photographer;
import com.upt.cti.photogmap.PhotographerAdapter;
import com.upt.cti.photogmap.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankPhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankPhotographerFragment extends Fragment {
    private ArrayList<Photographer> photographersList;
    private PhotographerAdapter photographerAdapter;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;


    private ProgressBar progressRankPhotographers;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public RankPhotographerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankPhotographerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankPhotographerFragment newInstance(String param1, String param2) {
        RankPhotographerFragment fragment = new RankPhotographerFragment();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank_photographer, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        photographersList = new ArrayList<Photographer>();
        RecyclerView recyclerPhotographersList = view.findViewById(R.id.recyclerPhotographersList);

        recyclerPhotographersList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerPhotographersList.setLayoutManager(linearLayoutManager);



        photographerAdapter = new PhotographerAdapter(getActivity(), photographersList);

        recyclerPhotographersList.setAdapter(photographerAdapter);


//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        String userID =  Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().toString();
//
//        System.out.println(userID);
//        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//
//                assert documentSnapshot != null;
//
//               String userType = documentSnapshot.getString("userType");
//
//                assert userType != null;
//
//                if (userType.equals("Fotograf")){
//                    View inflatedView = getLayoutInflater().inflate(R.layout.photographer_item_rank, container,false);
//                    voteLL = inflatedView.findViewById(R.id.voteLL);
//                    voteLL.setVisibility(View.GONE);
//
//               }
//
//
//            }
//        });




        EventChangeListener();

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.refreshLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RankPhotographerFragment rankPhotographerFragment = new RankPhotographerFragment();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, rankPhotographerFragment).commit();
                pullToRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("Users").orderBy("score", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {





                if (error != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("Firestore error...", error.getMessage());
                    return;
                }

                assert value != null;
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    photographersList.add(documentChange.getDocument().toObject(Photographer.class));
                }


                photographerAdapter.notifyDataSetChanged();
                photographersList = new ArrayList<Photographer>();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });



    }




}