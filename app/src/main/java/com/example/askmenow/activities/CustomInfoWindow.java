package com.example.askmenow.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askmenow.R;
import com.example.askmenow.adapters.CommentAdapter;
import com.example.askmenow.models.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CustomInfoWindow extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editText;
    private Button buttonAsk, buttonAnswer;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_info_window);
        Intent intent = getIntent();
        markerInfo(intent);
        String markerID = (String) intent.getStringExtra("MARKERNAME");
        recyclerView = findViewById(R.id.questions_list);
        editText = findViewById(R.id.response_text);
        buttonAsk = findViewById(R.id.send_button);
        //buttonAnswer = findViewById(R.id.respond_question_button);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CommentAdapter adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        database = FirebaseFirestore.getInstance();

        setUpWindow(database, adapter, markerID);

        buttonAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString().trim();
                if(!text.isEmpty()){
                    adapter.addComment(text);
                    editText.setText("");
                }

                Map<String, Object> comment = new HashMap<>();
                comment.put("text", text);

                database.collection("marker").document(markerID)
                        .collection("comments").add(comment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                System.out.println("SUCCESS");
                            }
                        });
            }

    });

    }

    private void markerInfo(Intent intent) {
        String markerName = (String) intent.getStringExtra("MARKERNAME");
        String markerAddr = (String) intent.getStringExtra("MARKERADDR");
        TextView markerNameText = findViewById(R.id.marker_name);
        TextView markerAddrText = findViewById(R.id.marker_address);
        markerNameText.setText(markerName);
        markerAddrText.setText(markerAddr);
    }

    private void setUpWindow(FirebaseFirestore database, CommentAdapter adapter, String markerID) {
        database.collection("marker").document(markerID)
                .collection("comments").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Comment comment = documentSnapshot.toObject(Comment.class);
                            adapter.addComment(comment.getText());
                        }
                    }
                });
    }

}
