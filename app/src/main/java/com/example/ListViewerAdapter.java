package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewerAdapter extends RecyclerView.Adapter<ListViewerAdapter.ViewHolder> {
    private static final int MAX_Q = 10;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.list.setAdapter(new QAListAdapter());
         holder.list.setLayoutManager(new LinearLayoutManager(holder.list.getContext()));
    }

    @Override
    public int getItemCount() {
        return MAX_Q;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         private final RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list = itemView.findViewById(R.id.qa_list);
        }
    }
}