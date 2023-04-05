package com.example.askmenow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class userProfileActivity extends AppCompatActivity {
    private static String[]  data = new String[6];
    private static int visibility = 0;
    String[] dropdownMenu = {"Everyone", "Friends Only", "Only Me"};
    AutoCompleteTextView auto;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        auto = findViewById(R.id.autodrop);
        adapter = new ArrayAdapter<String>(this,R.layout.list, dropdownMenu);
        auto.setAdapter(adapter);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(userProfileActivity.this, item,Toast.LENGTH_SHORT).show();
                visibility = i;
            }
        });
    }
    
    public int push(String field){
        return 0;
    }

    public int pullProfile(String[] profileInfo){
        boolean check = true;
        for (int i = 0; i<data.length; i++){
            data[i] = profileInfo[i];
            if(data[i] == null){
                check = false;
            }
        }
        if(check){
            return 0;
        }
        else{
            return 1;
        }
    }


}