package com.example.askmenow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askmenow.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<String> commentList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text);
        }
        public void bind(String comment) {
            commentTextView.setText(comment);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void addComment(String comment) {
        commentList.add(comment);
        notifyItemInserted(commentList.size() - 1);
    }

}