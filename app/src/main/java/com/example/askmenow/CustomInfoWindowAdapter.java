package com.example.askmenow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class CustomInfoWindowAdapter extends AppCompatActivity {

    /*private final View mWindow;
    private final Context mContext;*/
    private String markerName;
    private String markerAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_info_window);
        Intent intent = getIntent();
        markerInfo(intent);
        questionsListView();
    }

    private void markerInfo(Intent intent) {
        markerName = (String) intent.getStringExtra("MARKERNAME");
        markerAddr = (String) intent.getStringExtra("MARKERADDR");
        TextView markerNameText = findViewById(R.id.marker_name);
        TextView markerAddrText = findViewById(R.id.marker_address);
        markerNameText.setText(markerName);
        markerAddrText.setText(markerAddr);
    }
    ArrayAdapter<String> adapter;
    private void questionsListView(){
        Button send = findViewById(R.id.send_button);
        EditText responseText = findViewById(R.id.response_text);
        ListView listView = findViewById(R.id.questions_list);
        ArrayList<String> list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.marker_info_window, list);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = responseText.getText().toString();
                list.add(input);
                //listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

            }
        });
        // TODO: Add EditText's message into Firebase

    }
}
