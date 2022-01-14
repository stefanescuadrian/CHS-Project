package com.upt.cti.photogmap;

import android.content.Context;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotographerAdapter extends RecyclerView.Adapter<PhotographerAdapter.PhotographerViewHolder>{
    private String userType;
    StorageReference storageReference;
    Context context;
    ArrayList<Photographer> photographerArrayList;

    public PhotographerAdapter(Context context, ArrayList<Photographer> photographerArrayList) {
        this.context = context;
        this.photographerArrayList = photographerArrayList;
    }

    @NonNull
    @Override
    public PhotographerAdapter.PhotographerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.photographer_item_rank, parent, false);


        return new PhotographerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotographerAdapter.PhotographerViewHolder holder, int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Photographer photographer = photographerArrayList.get(position);



        StorageReference profileReference = storageReference.child("Users/" + photographer.getUserId() + "/profile.jpg");


        profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(context).load(uri).into(holder.imgProfilePicturePhotographerItem);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });





      holder.firstName.setText(photographer.getFirstName());
      holder.lastName.setText(photographer.getLastName());
      holder.mail.setText(photographer.getMail());
      holder.phoneNumber.setText(photographer.getPhoneNumber());
      holder.score.setText(String.valueOf(photographer.getScore()));
      holder.noOfVotes.setText(String.valueOf(photographer.getNoOfVotes()));
      holder.county.setText(String.valueOf(photographer.getCounty()));
      holder.country.setText(String.valueOf(photographer.getCountry()));
      holder.locality.setText(String.valueOf(photographer.getLocality()));

       float avg = (float) photographer.getScore() / (float) photographer.getNoOfVotes();
       holder.tAvg.setText(String.valueOf(avg));




      holder.imgProfilePicturePhotographerItem.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              System.out.println(photographer.getFirstName());

              //Todo : Open photographer gallery when click on its image
          }
      });

    }

    @Override
    public int getItemCount() {
        return photographerArrayList.size();
    }

    public static class PhotographerViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout photographerEntry;
        RatingBar ratingPhotographer;

        Button btnVote;
        TextView firstName, lastName, mail, phoneNumber, score, noOfVotes, locality, county, country, tAvg;
        CircleImageView imgProfilePicturePhotographerItem;

        public PhotographerViewHolder(@NonNull View itemView) {
            super(itemView);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
            System.out.println(documentReference);
            System.out.println(userId);

            firstName = itemView.findViewById(R.id.tPhotographerSurname);
            lastName = itemView.findViewById(R.id.tPhotographerName);
            mail = itemView.findViewById(R.id.tPhotographerEmail);
            phoneNumber = itemView.findViewById(R.id.tPhotographerPhoneNumber);
            score = itemView.findViewById(R.id.tPhotographerScore);
            noOfVotes = itemView.findViewById(R.id.tPhotographerVotes);
            photographerEntry = itemView.findViewById(R.id.photographerEntry);
            locality = itemView.findViewById(R.id.tPhotographerLocality);
            country = itemView.findViewById(R.id.tPhotographerCountry);
            county = itemView.findViewById(R.id.tPhotographerCounty);
            imgProfilePicturePhotographerItem = itemView.findViewById(R.id.imgProfilePicturePhotographerItem);
            tAvg = itemView.findViewById(R.id.tAvgPhotographer);

        }
    }

}
