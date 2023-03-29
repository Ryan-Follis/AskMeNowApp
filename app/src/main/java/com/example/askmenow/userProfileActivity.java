package com.example.askmenow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class userProfileActivity extends AppCompatActivity {
    private static String username, age, birthday,
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public int push(String field){
        return 0;
    }

    public int pullProfile(String[] profileInfo){
        return 0;
    }


}