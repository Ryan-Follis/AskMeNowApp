package com.example.askmenow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.ViewHolder> {

    // maximum number of answers.
    // Total number of items is MAX_A + 1 since there is also one question
    private static final int MAX_A = 10;

    @NonNull
    @Override
    public QAListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAListAdapter.ViewHolder holder, int position) {
        if (position == 0) {
            String q = "test question";
            holder.rowText.setText(q);
        }else {
            String a = "test answer " + position;
            holder.rowText.setText(a);
        }
    }

    @Override
    public int getItemCount() {
        return MAX_A + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView rowText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowText = itemView.findViewById(R.id.qa_row);
        }

    }

}
