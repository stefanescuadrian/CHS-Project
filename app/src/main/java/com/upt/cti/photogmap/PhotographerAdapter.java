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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class PhotographerAdapter extends RecyclerView.Adapter<PhotographerAdapter.PhotographerViewHolder>{

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
      Photographer photographer = photographerArrayList.get(position);

      holder.firstName.setText(photographer.getFirstName());
      holder.lastName.setText(photographer.getLastName());
      holder.mail.setText(photographer.getMail());
      holder.phoneNumber.setText(photographer.getPhoneNumber());
      holder.score.setText(String.valueOf(photographer.getScore()));
      holder.noOfVotes.setText(String.valueOf(photographer.getNoOfVotes()));

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

    public static class PhotographerViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout photographerEntry;
        RatingBar ratingPhotographer;

        Button btnVote;
        TextView firstName, lastName, mail, phoneNumber, score, noOfVotes;

        public PhotographerViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.tPhotographerSurname);
            lastName = itemView.findViewById(R.id.tPhotographerName);
            mail = itemView.findViewById(R.id.tPhotographerEmail);
            phoneNumber = itemView.findViewById(R.id.tPhotographerPhoneNumber);
            score = itemView.findViewById(R.id.tPhotographerScore);
            noOfVotes = itemView.findViewById(R.id.tPhotographerVotes);
            photographerEntry = itemView.findViewById(R.id.photographerEntry);
            btnVote = itemView.findViewById(R.id.btnVote);
            ratingPhotographer = itemView.findViewById(R.id.ratingPhotographer);




        }
    }

}
