package com.upt.cti.photogmap;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotographerAdapterClient extends RecyclerView.Adapter<PhotographerAdapterClient.PhotographerClientViewHolder>{
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



//      holder.btnVote.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              System.out.println("Voted!");
//          }
//
//      });
//
//      holder.ratingPhotographer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//          @Override
//          public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//              System.out.println("rating");
//          }
//      });

        holder.imgProfilePicturePhotographerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(photographer.getFirstName());

                //Todo : Open photographer gallery when click on its image
            }
        });


              holder.btnVote.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              System.out.println("Voted!");
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
