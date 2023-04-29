package com.example.askmenow.utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.askmenow.R;
import com.example.askmenow.model.QA;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.ViewHolder> {

    // maximum number of answers.
    // Total number of items is MAX_A + 1 since there is also one question
    private static final int MAX_A = 10;
    private final QA question;

    public QAListAdapter(QA question) {
        this.question = question;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.rowText.setText(question.getQuestion());
        }else {
            holder.rowText.setText(question.getAnswers().get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(MAX_A + 1, question.getAnswers().size() + 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView rowText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowText = itemView.findViewById(R.id.qa_row);
        }

    }

}
