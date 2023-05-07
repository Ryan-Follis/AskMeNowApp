package com.example.askmenow.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.askmenow.R;
import com.example.askmenow.models.QA;
import com.example.askmenow.utilities.Constants;

import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewerAdapter extends RecyclerView.Adapter<ListViewerAdapter.ViewHolder> {
    private static final int MAX_Q = 10;

    private final List<QA> qaList;
    private final Activity root;

    public ListViewerAdapter(List<QA> qaList, Activity activity) {
        filterList(qaList);
        this.qaList = qaList;
        root = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.list.setAdapter(new QAListAdapter(qaList.get(position), root));
        holder.list.setLayoutManager(new LinearLayoutManager(holder.list.getContext()));
    }

    @Override
    public int getItemCount() {
        return Math.min(MAX_Q, qaList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list = itemView.findViewById(R.id.qa_list);
        }
    }

    // remove private questions from the question list
    private void filterList(List<QA> qaList) {
        qaList.removeIf(question -> !Constants.VALUE_USER_ACCESS[0].equals(question.getQuestionAccess()));
    }
}