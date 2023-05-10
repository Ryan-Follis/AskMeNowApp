package com.example.askmenow.ui.profile_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.activities.ChatActivity;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.databinding.FragmentProfileHubBinding;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.models.User;
import com.example.askmenow.adapters.ProfileAdapter;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class ProfileHubFragment extends Fragment {

    private FragmentProfileHubBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private final DataAccess da = new DataAccess();
    private final User self = DataAccess.getSelf();
    private int currentPos = 0;
    private List<User> users;
    private static List<String> rememberList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileHubBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        database = FirebaseFirestore.getInstance();

        // set up viewPager2
        View load = root.findViewById(R.id.load);
        ViewPager2 profileContainer = root.findViewById(R.id.profile_container);
        load.setVisibility(View.VISIBLE);
        da.getAllUser(params -> {
            users = (List<User>) params[0];
            if (users.size() == 0) {
                Toast.makeText(getActivity(), "no user found", Toast.LENGTH_SHORT).show();
            }
//            ProfileAdapter profileAdapter = new ProfileAdapter(this.getActivity(), users, self);
            ProfileAdapter profileAdapter = new ProfileAdapter(this.getActivity(), users);
            profileContainer.setAdapter(profileAdapter);
            load.setVisibility(View.GONE);

            // get remember list
            RememberListOperations.getRememberList(params1 -> {
                List<User> resultList = (List<User>) params1[0];
                rememberList = new ArrayList<>();
                for (User user : resultList)
                    rememberList.add(user.id);
            });
        });

        profileContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // update remember
                ImageButton remember = getActivity().findViewById(R.id.remember_user);
                currentPos = position;
                if (rememberList != null && rememberList.contains(users.get(position).id)) {
                    remember.setImageResource(R.drawable.remembered); // image attribution Vecteezy.com
                    remember.setOnClickListener(v -> forgetListener(remember, users.get(position)));
                } else {
                    remember.setImageResource(R.drawable.remember); // image attribution Vecteezy.com
                    remember.setOnClickListener(v -> rememberListener(remember, users.get(position)));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // connect tab layout to the picContainer
        // TabLayout dotIndicator = findViewById(R.id.dot_indicator);
        // new TabLayoutMediator(dotIndicator, picContainer, (tab, position) -> {}).attach();

        // register click events for top menu buttons
        ImageButton search = root.findViewById(R.id.search_user);
        ImageButton friendRequest = root.findViewById(R.id.friend_request);
        ImageButton sendDM = root.findViewById(R.id.send_dm);
        search.setOnClickListener((View v)-> getActivity().onSearchRequested());
        friendRequest.setOnClickListener((View v)->{
            Intent friendIntent = new Intent(getActivity(), MainActivity.class);
            friendIntent.putExtra("dest", "friend list");
            friendIntent.putExtra("user id", self.id);
            startActivity(friendIntent);
        });
        sendDM.setOnClickListener((View v)-> {
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_USERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            documentReference.update(Constants.KEY_CURR_MSG_STATUS, 0);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, users.get(currentPos));
            startActivity(intent);
        });

        // Added by Ryan - profileHubQNA
        ImageButton askQuestion = root.findViewById(R.id.ask_question);
        askQuestion.setOnClickListener((View v) ->{
            // This intent launches a ChatActivity just like clicking the sendDM
            // button does. However, this button also changes the currMsgStatus
            // field of the current user to indicate that a question is being asked.
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_USERS).document(
                            preferenceManager.getString(Constants.KEY_USER_ID)
                    );
            documentReference.update(Constants.KEY_CURR_MSG_STATUS, Constants.ASKING_QUESTION);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, users.get(currentPos));
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void rememberListener(ImageButton remember, User user) {
        RememberListOperations.rememberUser(user.id);
        remember.setImageResource(R.drawable.remembered);
        remember.setOnClickListener(v -> forgetListener(remember, user));
        rememberList.add(user.id);
    }

    private void forgetListener(ImageButton remember, User user) {
        RememberListOperations.forgetUser(user.id);
        remember.setImageResource(R.drawable.remember);
        remember.setOnClickListener(v -> rememberListener(remember, user));
        rememberList.remove(user.id);
    }

    public static void rememberUser(String userId) {
        rememberList.add(userId);
    }

    public static void forgetUser(String userId) {
        rememberList.remove(userId);
    }

}
