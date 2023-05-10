package com.example.askmenow.ui.profile_hub;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.activities.ChatActivity;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.models.QA;
import com.example.askmenow.models.User;
import com.example.askmenow.adapters.ListViewerAdapter;
import com.example.askmenow.adapters.PictureViewerAdapter;
import com.example.askmenow.utilities.Constants;
import com.google.android.flexbox.FlexboxLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class SearchResultFragment extends Fragment {

    private final User resultUser;
    private final DataAccess da = new DataAccess();

    public SearchResultFragment(User user) {
        resultUser = user;
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

        da.getUserPics(resultUser.id, params -> {
            List<Bitmap> pics = (List<Bitmap>) params[0];
            PictureViewerAdapter picAdapter = new PictureViewerAdapter(pics);
            picContainer.setAdapter(picAdapter);
            loadImg.setVisibility(View.GONE);
        });
        da.getDisplayQuestions(resultUser.id, params -> {
            List<QA> qaList = (List<QA>) params[0];
            ListViewerAdapter listViewerAdapter = new ListViewerAdapter(qaList, getActivity());
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
        ImageButton remember = root.findViewById(R.id.like_user);
        ImageButton sendDM = root.findViewById(R.id.send_dm);
        retProfileHub.setOnClickListener((View v)->getActivity().onBackPressed());
        sendDM.setOnClickListener((View v)-> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, resultUser);
            startActivity(intent);
        });

        // show basic information
        TextView userName = root.findViewById(R.id.user_name);
        TextView nearby = root.findViewById(R.id.nearby);
        ImageButton interests = root.findViewById(R.id.show_interest);

        // show interests
        da.getUser(resultUser.id, params -> {
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
                            button.setTextColor(Color.parseColor("#000000"));
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
                        remember.setImageResource(R.drawable.dislike); // image attribution Vecteezy.com
                        remember.setOnClickListener(v -> forgetListener(remember, user));
                    } else {
                        remember.setImageResource(R.drawable.like); // image attribution Vecteezy.com
                        remember.setOnClickListener(v -> rememberListener(remember, user));
                    }
                });
            }
        });

        // ask question
        EditText questionText = root.findViewById(R.id.qa_input);
        ImageButton submitQuestion = root.findViewById(R.id.send_answer);
        submitQuestion.setOnClickListener(v -> {
            String question = questionText.getText().toString();
            if (!question.trim().equals("")) {
                // choose who can see the question
                final int[] selectedItem = new int[1];
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle("display answer to");
                final String[] choices = {"everyone", "this user anonymously", "this user only"};
                dialogBuilder.setSingleChoiceItems(choices, selectedItem[0], ((dialog1, which) -> {
                    selectedItem[0] = which;
                }));
                dialogBuilder.setPositiveButton("ask", ((dialog, which) -> {
                    Map<String, String> questionFields = new HashMap<>();
                    questionFields.put(Constants.KEY_USER_ID, DataAccess.getSelf().id);
                    questionFields.put(Constants.KEY_QUESTION, question);
                    questionFields.put(Constants.KEY_USER_ACCESS, Constants.VALUE_USER_ACCESS[selectedItem[0]]);
                    if (selectedItem[0] != 0) {
                        // question to a particular user, need a questionTo field
                        questionFields.put(Constants.KEY_QUESTION_TO, resultUser.id);
                    }
                    da.addDoc(Constants.KEY_COLLECTION_QA, questionFields, params -> {
                        questionText.setText("");
                    });
                }));
                dialogBuilder.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
                dialogBuilder.create().show();
            }
        });

        return root;
    }

    private void rememberListener(ImageButton remember, User user) {
        RememberListOperations.rememberUser(user.id);
        remember.setImageResource(R.drawable.dislike);
        remember.setOnClickListener(v -> forgetListener(remember, user));
        ProfileHubFragment.rememberUser(user.id);
    }

    private void forgetListener(ImageButton remember, User user) {
        RememberListOperations.forgetUser(user.id);
        remember.setImageResource(R.drawable.like);
        remember.setOnClickListener(v -> rememberListener(remember, user));
        ProfileHubFragment.forgetUser(user.id);
    }
}