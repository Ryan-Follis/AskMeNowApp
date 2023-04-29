package com.example.askmenow.utilities;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureViewerAdapter extends RecyclerView.Adapter<PictureViewerAdapter.ViewHolder> {

    private static final int MAX_PIC = 10;
    private final List<Bitmap> pics;

    public PictureViewerAdapter(List<Bitmap> pics) {
        this.pics = pics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(pics.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.min(MAX_PIC, pics.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.picture);
        }
    }
}
