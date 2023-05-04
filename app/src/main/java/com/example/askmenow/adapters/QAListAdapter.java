package com.example.askmenow.adapters;

import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class QAListAdapter extends RecyclerView.Adapter<QAListAdapter.ViewHolder> {

    // maximum number of answers.
    // Total number of items is MAX_A + 1 since there is also one question
    private static final int MAX_A = 10;
    private final QA question;
    private int length;
    private final DataAccess da = new DataAccess();
    private final Activity root;

    public QAListAdapter(QA question, Activity activity) {
        this.question = question;
        length = Math.min(MAX_A + 2, question.getAnswers().size() + 2);
        root = activity;
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
            // show question
            holder.rowText.setText(question.getQuestion());
        } else if (position == length - 1) {
            // send answer
            holder.newAnswerText.setHint("answer me now");
            holder.sendAnswer.setOnClickListener(v -> {
                Map<String, String> newAnswer = new HashMap<>();
                String newAsnwerText = holder.newAnswerText.getText().toString();
                if (!newAsnwerText.trim().equals("")) {
                    newAnswer.put(Constants.KEY_ANSWER_ID, DataAccess.getSelf().id);
                    newAnswer.put(Constants.KEY_ANSWER_CONTENT, newAsnwerText);
                    newAnswer.put(Constants.KEY_ANSWER_NAME, DataAccess.getSelf().username);

                    // choose who can see the answer
                    final int selectedItem[] = new int[1];
                    selectedItem[0] = 0;
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(root);
                    dialogBuilder.setTitle("display answer to");
                    final String[] choices = {"everyone", "everyone anonymously", "this user only"};
                    dialogBuilder.setSingleChoiceItems(choices, selectedItem[0], ((dialog1, which) -> {
                        selectedItem[0] = which;
                    }));
                    dialogBuilder.setPositiveButton("send answer", ((dialog, which) -> {
                        newAnswer.put(Constants.KEY_USER_ACCESS, Constants.VALUE_USER_ACCESS[selectedItem[0]]);
                        da.addToArray(Constants.KEY_COLLECTION_QA, question.getqId(), Constants.KEY_ANSWERS, newAnswer, params -> {
                            if ((Boolean) params[0]) {
                                if (selectedItem[0] != 2) {
                                    question.getAnswers().add(newAnswer);
                                    length++;
                                    notifyItemInserted(position);
                                }
                                holder.newAnswerText.setText("");
                            }
                        });
                    }));
                    dialogBuilder.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
                    dialogBuilder.create().show();
                }
            });
        } else {
            // show answer
            Map<String, String> answer = question.getAnswers().get(position - 1);
            String access = answer.get(Constants.KEY_USER_ACCESS);
            if (Constants.VALUE_USER_ACCESS[2].equals(access))
                return;
            holder.rowText.setText(answer.get(Constants.KEY_ANSWER_CONTENT));
            if (Constants.VALUE_USER_ACCESS[1].equals(access))
                holder.userName.setText("anonymous answer");
            else {
                holder.userName.setText(answer.get(Constants.KEY_ANSWER_NAME));
            }
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