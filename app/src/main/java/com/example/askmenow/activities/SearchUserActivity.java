package com.example.askmenow.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.askmenow.R;
import com.example.askmenow.utilities.SearchAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SearchUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        ImageButton returnProfileHub = findViewById(R.id.profile_hub);
        returnProfileHub.setOnClickListener(view->this.finish());
        RecyclerView resultList = findViewById(R.id.search_result_list);

        // get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            resultList.setAdapter(search(query));
            resultList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private SearchAdapter search(String query) {
        // do search

        // return the result
        String[] test = new String[20];
        for (int i = 0; i < 20; i++) {
            test[i] = "test name " + query + " " + i;
        }

        return new SearchAdapter(this, test);
    }
}