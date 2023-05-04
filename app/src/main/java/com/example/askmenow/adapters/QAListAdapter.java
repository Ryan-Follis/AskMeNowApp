package com.example.askmenow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.models.QA;
import com.example.askmenow.utilities.Constants;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.ViewHolder> {

    // maximum number of answers.
    // Total number of items is MAX_A + 1 since there is also one question
    private static final int MAX_A = 10;
    private final QA question;
    private int length;
    private final DataAccess da = new DataAccess();

    public QAListAdapter(QA question) {
        this.question = question;
        length = Math.min(MAX_A + 2, question.getAnswers().size() + 2);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.qa_row;
        if (viewType == 1) {
            layout = R.layout.chat_row;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.rowText.setText(question.getQuestion());
        } else if (position == length - 1) {
            holder.newAnswerText.setHint("answer me now");
            holder.sendAnswer.setOnClickListener(v -> {
                Map<String, String> newAnswer = new HashMap<>();
                String newAsnwerText = holder.newAnswerText.getText().toString();
                if (!newAsnwerText.trim().equals("")) {
                    newAnswer.put(Constants.KEY_ANSWER_ID, DataAccess.getSelf().id);
                    newAnswer.put(Constants.KEY_ANSWER_CONTENT, newAsnwerText);
                    newAnswer.put(Constants.KEY_ANSWER_NAME, DataAccess.getSelf().username);
                    da.addToArray(Constants.KEY_COLLECTION_QA, question.getqId(), Constants.KEY_ANSWERS, newAnswer, params -> {
                        if ((Boolean) params[0]) {
                            question.getAnswers().add(newAnswer);
                            length++;
                            notifyItemInserted(position);
                        }
                    });
                }
            });
        } else {
            Map<String, String> answer = question.getAnswers().get(position - 1);
            holder.rowText.setText(answer.get(Constants.KEY_ANSWER_CONTENT));
            holder.userName.setText(answer.get(Constants.KEY_ANSWER_NAME));
        }
    }

    @Override
    public int getItemCount() {
        return length;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == length - 1)
            return 1;
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView userName;
        private final TextView rowText;
        private final EditText newAnswerText;
        private final ImageButton sendAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.answer_name);
            rowText = itemView.findViewById(R.id.answer_text);
            sendAnswer = itemView.findViewById(R.id.send_answer);
            newAnswerText = itemView.findViewById(R.id.qa_input);
        }

    }

}