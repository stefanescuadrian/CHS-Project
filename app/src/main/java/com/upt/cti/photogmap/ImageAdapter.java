package com.upt.cti.photogmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context myContext;
    private List<Upload> photoListUploads;

    public ImageAdapter(Context context, List<Upload> uploads){
        myContext = context;
        photoListUploads = uploads;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.photo_item_gallery, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload uploadCurrent = photoListUploads.get(position);
        holder.textViewNamePhoto.setText(uploadCurrent.getPhotoName());
        holder.textViewDescriptionPhoto.setText(uploadCurrent.getPhotoDescription());
        Picasso.with(myContext).load(uploadCurrent.getPhotoUrl()).placeholder(R.mipmap.ic_no_image) .fit().centerCrop().into(holder.uploadedPhotoView); //or i can use centerInside
    }

    @Override
    public int getItemCount() {
        return photoListUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewNamePhoto;
        public TextView textViewDescriptionPhoto;
        public ImageView uploadedPhotoView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNamePhoto = itemView.findViewById(R.id.textPhotoName);
            textViewDescriptionPhoto = itemView.findViewById(R.id.textPhotoDescription);
            uploadedPhotoView = itemView.findViewById(R.id.uploadedPhotoView);

        }
    }

}
