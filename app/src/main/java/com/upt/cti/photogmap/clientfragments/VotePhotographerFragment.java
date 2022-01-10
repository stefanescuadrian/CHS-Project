package com.upt.cti.photogmap.clientfragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.upt.cti.photogmap.Photographer;
import com.upt.cti.photogmap.PhotographerAdapter;
import com.upt.cti.photogmap.PhotographerAdapterClient;
import com.upt.cti.photogmap.R;
import com.upt.cti.photogmap.photographerfragments.RankPhotographerFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VotePhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VotePhotographerFragment extends Fragment {

    private ArrayList<Photographer> photographersList;
    private PhotographerAdapterClient photographerAdapterClient;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VotePhotographerFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vote_client, containerClient, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();


        photographersList = new ArrayList<Photographer>();
        RecyclerView recyclerPhotographersList_Client = view.findViewById(R.id.recyclerPhotographersList_Client);

        recyclerPhotographersList_Client.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerPhotographersList_Client.setLayoutManager(linearLayoutManager);



        photographerAdapterClient = new PhotographerAdapterClient(getActivity(), photographersList);

        recyclerPhotographersList_Client.setAdapter(photographerAdapterClient);



        EventChangeListener();

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.refreshLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VotePhotographerFragment votePhotographerFragment = new VotePhotographerFragment();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerClient, votePhotographerFragment).commit();
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


                photographerAdapterClient.notifyDataSetChanged();
                photographersList = new ArrayList<Photographer>();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });


    }

}
