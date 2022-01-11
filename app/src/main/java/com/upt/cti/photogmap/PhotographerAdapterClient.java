package com.upt.cti.photogmap;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.upt.cti.photogmap.clientfragments.VotePhotographerFragment;

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

    public PhotographerAdapterClient(Context context, ArrayList<Photographer> photographerArrayList) {
        this.context = context;
        this.photographerArrayList = photographerArrayList;
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

                                           documentReference1.update(update_votes).addOnSuccessListener(unused -> Log.d("TAG", "onSuccess: Votul a fost actualizat " + photographer.getUserId()));

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

                //Todo : Open photographer gallery when click on its image
            }
        });



      holder.ratingPhotographer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
          @Override
          public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
              System.out.println("rating");
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

        }
    }


}
