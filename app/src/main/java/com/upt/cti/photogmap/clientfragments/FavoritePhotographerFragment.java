package com.upt.cti.photogmap.clientfragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.upt.cti.photogmap.Photographer;
import com.upt.cti.photogmap.PhotographerAdapterClient;
import com.upt.cti.photogmap.PhotographerAdapterClientFavorite;
import com.upt.cti.photogmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VotePhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritePhotographerFragment extends Fragment{

    private ArrayList<Photographer> photographersList;
    private PhotographerAdapterClientFavorite photographerAdapterClientFavorite;
    private static String filter = "Toate";


    private FirebaseFirestore firebaseFirestore;
    Spinner spinnerWithLocality;
    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritePhotographerFragment() {
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
    public static VotePhotographerFragment newInstance(String param1, String param2) {
        VotePhotographerFragment fragment = new VotePhotographerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup containerClient,
                             Bundle savedInstanceState) {

        spinnerWithLocality = getActivity().findViewById(R.id.filterLocality);
        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> filterArray = new ArrayList<String>();
        spinnerWithLocality = getActivity().findViewById(R.id.filterLocality);
        filterArray.add("Toate");
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").whereEqualTo("userType", "Fotograf").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){


                    for (QueryDocumentSnapshot document: task.getResult()) {
                        String locality = document.getString("locality");
                        System.out.println(locality);
                        System.out.println("\n");
                        if (locality != null) {
                            filterArray.add(locality);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerWithLocality.setAdapter(adapter);
                    filter = spinnerWithLocality.getSelectedItem().toString();
                }
                else {
                    System.out.println("ERROR");
                }
            }
        });


        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_vote_client, containerClient, false);





        spinnerWithLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = spinnerWithLocality.getSelectedItem().toString();

                firebaseFirestore = FirebaseFirestore.getInstance();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Fetching Data...");
                progressDialog.show();


                photographersList = new ArrayList<Photographer>();
                RecyclerView recyclerPhotographersList_Client = view1.findViewById(R.id.recyclerPhotographersList_Client);

                recyclerPhotographersList_Client.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                recyclerPhotographersList_Client.setLayoutManager(linearLayoutManager);



                photographerAdapterClientFavorite = new PhotographerAdapterClientFavorite(getActivity(), photographersList, getActivity().findViewById(R.id.navClient));

                recyclerPhotographersList_Client.setAdapter(photographerAdapterClientFavorite);



                EventChangeListener();

                final SwipeRefreshLayout pullToRefresh = view1.findViewById(R.id.refreshLayout);
                pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        FavoritePhotographerFragment favoritePhotographerFragment = new FavoritePhotographerFragment();
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerClient, favoritePhotographerFragment).commit();
                        pullToRefresh.setRefreshing(false);
                    }
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view1;
    }

    private void EventChangeListener() {
        Query query;
        if (filter == "Toate"){
            query = firebaseFirestore.collection("Users").orderBy("score", Query.Direction.DESCENDING);
        } else {
            query = firebaseFirestore.collection("Users").orderBy("score", Query.Direction.DESCENDING).whereEqualTo("locality",filter);
        }
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if (error != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("Firestore error...", error.getMessage());
                    return;
                }

                List<String> list = new ArrayList<String>();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String userId = firebaseAuth.getCurrentUser().getUid();
                firebaseFirestore.collection("Favorites").whereEqualTo("Client", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                list.add(documentSnapshot.getString("Photographer"));
                            }

                            assert value != null;
                            for (DocumentChange documentChange : value.getDocumentChanges()){

                                boolean ok = false;
                                for (String favoritePhotographer : list
                                ) {
                                    if (documentChange.getDocument().getId().equals(favoritePhotographer)){
                                        ok = true;
                                    }
                                }

                                if (ok)
                                    photographersList.add(documentChange.getDocument().toObject(Photographer.class));

                            }

                            photographerAdapterClientFavorite.notifyDataSetChanged();
                            photographersList = new ArrayList<Photographer>();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }



                    }
                });




            }
        });


    }


}
