package com.example.askmenow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ViewPager2 picContainer = findViewById(R.id.pic_container);
        InfoBarUtils info = new InfoBarUtils(this);
        PictureViewerAdapter picAdapter = new PictureViewerAdapter(info);
        picContainer.setAdapter(picAdapter);
        ViewPager2 listContainer = findViewById(R.id.list_container);
        ListViewerAdapter listAdapter = new ListViewerAdapter();
        listContainer.setAdapter(listAdapter);


        picContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // update info bar
                info.updateName("test name " + position);
                info.updateAge(String.valueOf(position * 5));
                Boolean nearby = false;
                if (position % 3 == 0)
                    nearby = true;
                info.isNearBy(nearby);
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


        // connect tab layout to the picContainer
        TabLayout dotIndicator = (TabLayout) findViewById(R.id.dot_indicator);
        new TabLayoutMediator(dotIndicator, picContainer, (tab, position) -> {}).attach();

        // register click events for top menu buttons
        ImageButton search = (ImageButton) findViewById(R.id.search_user);
        ImageButton friendRequest = (ImageButton) findViewById(R.id.friend_request);
        ImageButton rememberUser = (ImageButton) findViewById(R.id.remember_user);
        ImageButton newDM = (ImageButton) findViewById(R.id.new_dm);
        search.setOnClickListener((View v)->{

        });
        friendRequest.setOnClickListener((View v)->{

        });
        rememberUser.setOnClickListener((View v)->{

        });
        newDM.setOnClickListener((View v)->{

        });
    }
}