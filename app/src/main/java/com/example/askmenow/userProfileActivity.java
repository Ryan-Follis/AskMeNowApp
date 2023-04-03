package com.example.askmenow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class userProfileActivity extends AppCompatActivity {
    private static String[]  data = new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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