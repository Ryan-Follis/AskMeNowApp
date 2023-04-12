package com.example.askmenow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.askmeknow.R;

import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent result = getIntent();
        int resultId = Integer.parseInt(result.getStringExtra("id"));

        // set up ViewPager2
        ViewPager2 picContainer = findViewById(R.id.pic_container);
        ViewPager2 listContainer = findViewById(R.id.list_container);
        PictureViewerAdapter pictureViewerAdapter = new PictureViewerAdapter(resultId);
        ListViewerAdapter listViewerAdapter = new ListViewerAdapter(resultId);
        picContainer.setAdapter(pictureViewerAdapter);
        listContainer.setAdapter(listViewerAdapter);

        picContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        listContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // set up buttons
        ImageButton retProfileHub = findViewById(R.id.profile_hub);
        ImageButton friendRequest = findViewById(R.id.friend_request);
        ImageButton rememberUser = findViewById(R.id.remember_user);
        ImageButton sendDM = findViewById(R.id.send_dm);
        retProfileHub.setOnClickListener((View v)->this.finish());
        friendRequest.setOnClickListener((View v)->{

        });
        rememberUser.setOnClickListener((View v)->{

        });
        sendDM.setOnClickListener((View v)-> {
        });

        // show basic information
        TextView userName = findViewById(R.id.user_name);
        TextView userAge = findViewById(R.id.user_age);
        TextView nearby = findViewById(R.id.nearby);

        userName.post(() -> userName.setText("test id " + resultId));
        userAge.post(() -> userAge.setText(String.valueOf(resultId)));

    }

}
