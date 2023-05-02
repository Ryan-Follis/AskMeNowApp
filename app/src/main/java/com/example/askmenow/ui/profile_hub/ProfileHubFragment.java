package com.example.askmenow.ui.profile_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.databinding.FragmentProfileHubBinding;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.models.User;
import com.example.askmenow.adapters.ProfileAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import androidx.fragment.app.Fragment;

public class ProfileHubFragment extends Fragment {

    private FragmentProfileHubBinding binding;
    private final DataAccess da = new DataAccess();
    private final User self = DataAccess.getSelf();
    private List<User> users;
    private List<String> rememberList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileHubBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
            RememberListOperations.getRememberList(params1 -> rememberList = (List<String>) params1[0]);
        });

        profileContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                // update remember
                ImageButton remember = getActivity().findViewById(R.id.remember_user);
                if (rememberList != null && rememberList.contains(users.get(position).id)) {
                    remember.setImageResource(R.drawable.remembered); // image attribution Vecteezy.com
                    remember.setOnClickListener(v -> forgetListener(remember, users.get(position)));
                } else {
                    remember.setImageResource(R.drawable.remember); // image attribution Vecteezy.com
                    remember.setOnClickListener(v -> rememberListener(remember, users.get(position)));
                }
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
            // uploadPhoto(sendDM);
        });

        return root;
    }

//    // this activity launcher gets the photo that the user chooses and pass it to uploadPhotoActivity
//    ActivityResultLauncher<Intent> selectPhotoActivity = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Intent photo = result.getData();
//                    if (photo != null && photo.getData() != null) {
//                        Uri photoUri = photo.getData();
//                        // pass the URI to uploadPhotoActivity
//                        Intent editText = new Intent(getActivity(), UploadPhotoActivity.class);
//                        editText.putExtra("photoURI", photoUri.toString());
//                        getActivity().startActivity(editText);
//                    }
//                }
//            });
//
//    public void chooseImage() {
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.setType("image/*");
//        selectPhotoActivity.launch(i);
//    }
//
//    public void uploadPhoto(ImageButton selectPhoto) {
//        // set up the popup menu
//        PopupMenu photoMenu = new PopupMenu(getActivity(), selectPhoto);
//        photoMenu.getMenuInflater().inflate(R.menu.select_photo_menu, photoMenu.getMenu());
//
//        photoMenu.setOnMenuItemClickListener((MenuItem item) -> {
//            if (item.getItemId() == R.id.gallery) {
//                // select photo from the gallery
//                chooseImage();
//            }
//            return true;
//        });
//        photoMenu.show();
//    }

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

}
