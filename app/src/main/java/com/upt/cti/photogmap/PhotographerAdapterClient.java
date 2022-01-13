package com.upt.cti.photogmap;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.upt.cti.photogmap.clientfragments.GalleryPhotographerFragment_Client;
import com.upt.cti.photogmap.clientfragments.VotePhotographerFragment;
import com.upt.cti.photogmap.photographerfragments.GalleryPhotographerFragment;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotographerAdapterClient extends RecyclerView.Adapter<PhotographerAdapterClient.PhotographerClientViewHolder>{
    private static final int POSITION_NONE = 0;
    private String userType;

    Context context;
    ArrayList<Photographer> photographerArrayList;
    BottomNavigationView navigationBar;

    public PhotographerAdapterClient(Context context, ArrayList<Photographer> photographerArrayList, BottomNavigationView navigationBar) {
        this.context = context;

        this.photographerArrayList = photographerArrayList;
        this.navigationBar = navigationBar;
    }

    @NonNull
    @Override
    public PhotographerAdapterClient.PhotographerClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.photographer_item_rank_for_client, parent, false);


        return new PhotographerClientViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull PhotographerAdapterClient.PhotographerClientViewHolder holder, int position) {

        Photographer photographer = photographerArrayList.get(position);



        holder.firstName.setText(photographer.getFirstName());
        holder.lastName.setText(photographer.getLastName());
        holder.mail.setText(photographer.getMail());
        holder.phoneNumber.setText(photographer.getPhoneNumber());
        holder.score.setText(String.valueOf(photographer.getScore()));
        holder.noOfVotes.setText(String.valueOf(photographer.getNoOfVotes()));
        holder.county.setText(String.valueOf(photographer.getCounty()));
        holder.country.setText(String.valueOf(photographer.getCountry()));
        holder.locality.setText(String.valueOf(photographer.getLocality()));



      holder.btnVote.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FirebaseFirestore firebaseFirestore;
              FirebaseAuth firebaseAuth;
              firebaseFirestore = FirebaseFirestore.getInstance();
              firebaseAuth = FirebaseAuth.getInstance();

              String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

              double currentRating = holder.ratingPhotographer.getRating();


              System.out.println(holder.ratingPhotographer.getRating());
              System.out.println(photographer.getUserId());





             DocumentReference documentReference = firebaseFirestore.collection("Votes").document(userId+photographer.getUserId());

             documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @RequiresApi(api = Build.VERSION_CODES.N)
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){
                           DocumentSnapshot documentSnapshot = task.getResult();

                           Map<String, Object> vote = new HashMap<>();
                           vote.put("VoteBy",userId);
                           vote.put("VoteFor",photographer.getUserId());
                           vote.put("Score", holder.ratingPhotographer.getRating());
                           vote.put("Voted", 1);

                           if (documentSnapshot.exists()){
                               int lastScore = Integer.valueOf(Math.toIntExact(documentSnapshot.getLong("Score")));


                               documentReference.update(vote).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Votul a fost adăugat cu succes " + userId));

                               DocumentReference documentReference1 = firebaseFirestore.collection("Users").document(photographer.getUserId());

                               documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                       if (task.isSuccessful()) {
                                           DocumentSnapshot documentSnapshot1 = task.getResult();

                                           int scoreFromDatabase =  Integer.valueOf(Math.toIntExact(documentSnapshot1.getLong("score")));




                                           int score = (int) (holder.ratingPhotographer.getRating() + scoreFromDatabase - lastScore);


                                           Map<String, Object> update_votes = new HashMap<>();
                                           update_votes.put("score", score);

                                           documentReference1.update(update_votes).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Votul a fost actualizat " + photographer.getUserId()));




                                       }
                                   }
                               });






                           }
                           else {
                               documentReference.set(vote).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Votul a fost actualizat cu succes " + userId));


                               DocumentReference documentReference1 = firebaseFirestore.collection("Users").document(photographer.getUserId());


                               documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                       if (task.isSuccessful()) {
                                           DocumentSnapshot documentSnapshot1 = task.getResult();
                                           int noOfVotesFromDatabase = photographer.getNoOfVotes();
                                           int scoreFromDatabase = photographer.getScore();




                                           int noOfVotes = 1 + noOfVotesFromDatabase;
                                           int score = (int) (holder.ratingPhotographer.getRating() + scoreFromDatabase);


                                           Map<String, Object> update_votes = new HashMap<>();
                                           update_votes.put("noOfVotes", noOfVotes);
                                           update_votes.put("score", score);

                                           update_Favorites(photographer.getUserId(), noOfVotes, score);

                                           documentReference1.update(update_votes).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Votul a fost actualizat " + photographer.getUserId()));



//                                           documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                               @RequiresApi(api = Build.VERSION_CODES.N)
//                                               @Override
//                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                   if (task.isSuccessful()) {
//                                                       DocumentSnapshot documentSnapshot = task.getResult();
//
//                                                       Map<String, Object> favorites = new HashMap<>();
//
//                                                       favorites.put("noOfVotes", photographer.getNoOfVotes());
//                                                       favorites.put("score", photographer.getScore());
//                                                       documentReference2.set(favorites);
//
//
//                                                   }
//                                               }
//                                           });


                                       }
                                   }
                               });


                           }
                       } else
                       {
                           Log.d("notExists!", "Failed with ", task.getException());
                       }



                  }
              });



              Toast.makeText(v.getContext(), "Voted...", Toast.LENGTH_SHORT).show();

              System.out.println("Voted!");




          }

      });


        holder.imgProfilePicturePhotographerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(photographer.getFirstName());
                AppCompatActivity activity =(AppCompatActivity) v.getContext();
                GalleryPhotographerFragment_Client galleryPhotographerFragment_client = new GalleryPhotographerFragment_Client();
                Bundle args = new Bundle();

                Spinner spinner = activity.findViewById(R.id.filterLocality);
                spinner.setVisibility(View.GONE);
                args.putString("photographerId",photographer.getUserId());
                galleryPhotographerFragment_client.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containerClient, galleryPhotographerFragment_client).commit();

                //Todo : Open photographer gallery when click on its image
            }
        });



      holder.ratingPhotographer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
          @Override
          public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
              System.out.println("rating");
          }
      });

      holder.btnAddFavoritePhotographer.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              FirebaseFirestore firebaseFirestore;
              FirebaseAuth firebaseAuth;
              firebaseFirestore = FirebaseFirestore.getInstance();
              firebaseAuth = FirebaseAuth.getInstance();

              String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();


              DocumentReference documentReference = firebaseFirestore.collection("Favorites").document(userId+photographer.getUserId());

              documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @RequiresApi(api = Build.VERSION_CODES.N)
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot documentSnapshot = task.getResult();

                                                                        Map<String, Object> favorites = new HashMap<>();
                                                                        favorites.put("Client", userId);
                                                                        favorites.put("Photographer", photographer.getUserId());
                                                                        favorites.put("country", photographer.getCountry());
                                                                        favorites.put("county", photographer.getCounty());
                                                                        favorites.put("firstName", photographer.getFirstName());
                                                                        favorites.put("lastName", photographer.getLastName());
                                                                        favorites.put("locality", photographer.getLocality());
                                                                        favorites.put("mail", photographer.getMail());
                                                                        favorites.put("noOfVotes", photographer.getNoOfVotes());
                                                                        favorites.put("phoneNumber", photographer.getPhoneNumber());
                                                                        favorites.put("score", photographer.getScore());
                                                                        favorites.put("userId",photographer.getUserId());
                                                                        documentReference.set(favorites);


                                                                    }
                                                                }
                                                            });
                holder.btnAddFavoritePhotographer.setVisibility(View.GONE);
              Toast.makeText(v.getContext(), "Adăugat la favorite...", Toast.LENGTH_SHORT).show();
          }
      });

    }

    private void update_Favorites(String userId, int noOfVotes, int score) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String clientId = firebaseAuth.getCurrentUser().getUid();


        DocumentReference documentReference2 = firebaseFirestore.collection("Favorites").document(clientId+userId);


                                           documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @RequiresApi(api = Build.VERSION_CODES.N)
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       DocumentSnapshot documentSnapshot = task.getResult();
                                                       System.out.println("test");
                                                       Map<String, Object> favorites = new HashMap<>();

                                                       favorites.put("noOfVotes", noOfVotes);
                                                       favorites.put("score", score);
                                                       documentReference2.set(favorites);


                                                   }
                                               }
                                           });

    }

    @Override
    public int getItemCount() {
        return photographerArrayList.size();
    }

    public static class PhotographerClientViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout photographerEntry;
        RatingBar ratingPhotographer;
        Button btnAddFavoritePhotographer;

        Button btnVote;
        TextView firstName, lastName, mail, phoneNumber, score, noOfVotes, locality, county, country;
        CircleImageView imgProfilePicturePhotographerItem;

        public PhotographerClientViewHolder(@NonNull View itemView) {
            super(itemView);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
            System.out.println(documentReference);
            System.out.println(userId);

            firstName = itemView.findViewById(R.id.tPhotographerSurname_Client);
            lastName = itemView.findViewById(R.id.tPhotographerName_Client);
            mail = itemView.findViewById(R.id.tPhotographerEmail_Client);
            phoneNumber = itemView.findViewById(R.id.tPhotographerPhoneNumber_Client);
            score = itemView.findViewById(R.id.tPhotographerScore_Client);
            noOfVotes = itemView.findViewById(R.id.tPhotographerVotes_Client);
            photographerEntry = itemView.findViewById(R.id.photographerEntry_Client);
            btnVote = itemView.findViewById(R.id.btnVote_Client);
            ratingPhotographer = itemView.findViewById(R.id.ratingPhotographer_Client);
            locality = itemView.findViewById(R.id.tPhotographerLocality_Client);
            country = itemView.findViewById(R.id.tPhotographerCountry_Client);
            county = itemView.findViewById(R.id.tPhotographerCounty_Client);
            imgProfilePicturePhotographerItem = itemView.findViewById(R.id.imgProfilePicturePhotographerItem_Client);
            btnAddFavoritePhotographer = itemView.findViewById(R.id.btnAddFavoritePhotographer);

        }
    }


}
