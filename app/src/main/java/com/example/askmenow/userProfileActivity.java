package com.example.askmenow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class userProfileActivity extends AppCompatActivity {
    private static String[]  data = new String[6];
    private static int visibility = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.visibility_dropdown, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        return true;
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