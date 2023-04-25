package com.example.askmenow.ui.questions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.askmenow.R;
import com.example.askmenow.utilities.ListViewerAdapter;
import com.example.askmenow.utilities.PictureViewerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class SearchResultFragment extends Fragment {

    private final int resultId;

    public SearchResultFragment(int id) {
        resultId = id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_result, container, false);

        // set up ViewPager2
        ViewPager2 picContainer = root.findViewById(R.id.pic_container);
        ViewPager2 listContainer = root.findViewById(R.id.list_container);
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
        ImageButton retProfileHub = root.findViewById(R.id.profile_hub);
        ImageButton friendRequest = root.findViewById(R.id.friend_request);
        ImageButton rememberUser = root.findViewById(R.id.remember_user);
        ImageButton sendDM = root.findViewById(R.id.send_dm);
        retProfileHub.setOnClickListener((View v)->getActivity().onBackPressed());
        friendRequest.setOnClickListener((View v)->{

        });
        rememberUser.setOnClickListener((View v)->{

        });
        sendDM.setOnClickListener((View v)-> {
        });

        // show basic information
        TextView userName = root.findViewById(R.id.user_name);
        TextView userAge = root.findViewById(R.id.user_age);
        TextView nearby = root.findViewById(R.id.nearby);

        userName.post(() -> userName.setText("test id " + resultId));
        userAge.post(() -> userAge.setText(String.valueOf(resultId)));

        return root;
    }

}
