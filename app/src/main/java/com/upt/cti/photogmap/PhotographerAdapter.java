package com.upt.cti.photogmap;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
      holder.points.setText(String.valueOf(photographer.getScore()));
      holder.votes.setText(String.valueOf(photographer.getNoOfVotes()));


    }

    @Override
    public int getItemCount() {
        return photographerArrayList.size();
    }

    public static class PhotographerViewHolder extends RecyclerView.ViewHolder{

        TextView firstName, lastName, mail, phoneNumber, points, votes;

        public PhotographerViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.tPhotographerSurname);
            lastName = itemView.findViewById(R.id.tPhotographerName);
            mail = itemView.findViewById(R.id.tPhotographerEmail);
            phoneNumber = itemView.findViewById(R.id.tPhotographerPhoneNumber);
            points = itemView.findViewById(R.id.tPhotographerScore);
            votes = itemView.findViewById(R.id.tPhotographerVotes);



        }
    }

}
