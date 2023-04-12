package com.example.askmenow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.askmeknow.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureViewerAdapter extends RecyclerView.Adapter<PictureViewerAdapter.ViewHolder> {
    private static final int MAX_PIC = 10;

    private final int uid;

    public PictureViewerAdapter(int uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.test_pic);
    }

    @Override
    public int getItemCount() {
        return MAX_PIC;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.picture);
        }
    }
}
