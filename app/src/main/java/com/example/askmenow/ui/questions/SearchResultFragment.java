package com.example.askmenow.ui.questions;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;
import com.example.askmenow.utilities.ListViewerAdapter;
import com.example.askmenow.utilities.PictureViewerAdapter;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class SearchResultFragment extends Fragment {

    private final String resultId;
    private final DataAccess da = new DataAccess();

    public SearchResultFragment(String id) {
        resultId = id;
        da.setRoot(getActivity());

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_result, container, false);

        // set up ViewPager2
        ViewPager2 picContainer = root.findViewById(R.id.pic_container);
        ViewPager2 listContainer = root.findViewById(R.id.list_container);
        View loadImg = root.findViewById(R.id.load_image);
        View loadQA = root.findViewById(R.id.load_qa);
        loadImg.setVisibility(View.VISIBLE);
        loadQA.setVisibility(View.VISIBLE);

        da.getUserPics(resultId, params -> {
            List<Bitmap> pics = (List<Bitmap>) params[0];
            PictureViewerAdapter picAdapter = new PictureViewerAdapter(pics);
            picContainer.setAdapter(picAdapter);
            loadImg.setVisibility(View.GONE);
        });
        da.getDisplayQuestions(resultId, params -> {
            List<QA> qaList = (List<QA>) params[0];
            ListViewerAdapter listViewerAdapter = new ListViewerAdapter(qaList);
            listContainer.setAdapter(listViewerAdapter);
            loadQA.setVisibility(View.GONE);
        });

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
        ImageButton remember = root.findViewById(R.id.remember_user);
        ImageButton sendDM = root.findViewById(R.id.send_dm);
        retProfileHub.setOnClickListener((View v)->getActivity().onBackPressed());
        sendDM.setOnClickListener((View v)-> {
        });

        // show basic information
        TextView userName = root.findViewById(R.id.user_name);
        TextView nearby = root.findViewById(R.id.nearby);
        ImageButton interests = root.findViewById(R.id.show_interest);

        da.getUser(resultId, params -> {
            if (params == null || params[0] == null) {
                Toast.makeText(getActivity(), "Some error happened. Please try again", Toast.LENGTH_SHORT).show();
            } else {
                User user = (User) params[0];
                userName.post(() -> userName.setText(user.username));
                interests.setOnClickListener(view -> {
                    Dialog interestPopup = new Dialog(getActivity());
                    interestPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    interestPopup.setContentView(R.layout.interest_popup);
                    interestPopup.setCanceledOnTouchOutside(true);
                    FlexboxLayout layout = interestPopup.findViewById(R.id.interest_popup);
                    if (user.interests != null) {
                        for (String interest : user.interests) {
                            Button button = new Button(getActivity());
                            button.setLayoutParams(new FlexboxLayout.LayoutParams(
                                    FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT));
                            button.setText(interest);
                            button.setTextColor(R.color.black);
                            button.setBackgroundResource(R.drawable.interest_button);
                            button.setOnClickListener(v -> {

                            });
                            layout.addView(button);
                        }
                    }
                    interestPopup.show();
                });

                // update remember
                RememberListOperations.remembered(user.id, params1 -> {
                    Boolean result = (Boolean) params1[0];
                    if (result) {
                        remember.setImageResource(R.drawable.remembered); // image attribution Vecteezy.com
                        remember.setOnClickListener(v -> forgetListener(remember, user));
                    } else {
                        remember.setImageResource(R.drawable.remember); // image attribution Vecteezy.com
                        remember.setOnClickListener(v -> rememberListener(remember, user));
                    }
                });
            }
        });

        return root;
    }

    private void rememberListener(ImageButton remember, User user) {
        RememberListOperations.rememberUser(user.id);
        remember.setImageResource(R.drawable.remembered);
        remember.setOnClickListener(v -> forgetListener(remember, user));
    }

    private void forgetListener(ImageButton remember, User user) {
        RememberListOperations.forgetUser(user.id);
        remember.setImageResource(R.drawable.remember);
        remember.setOnClickListener(v -> rememberListener(remember, user));
    }
}
