package com.example.askmenow.ui.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.databinding.FragmentQuestionsBinding;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.model.User;
import com.example.askmenow.utilities.ProfileAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class QuestionsFragment extends Fragment {
    private FragmentQuestionsBinding binding;
    private final DataAccess da = new DataAccess();
    private final User self = DataAccess.getSelf();
    private final ProfileAdapter[] profileAdapter = new ProfileAdapter[1];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuestionsViewModel questionsViewModel =
                new ViewModelProvider(this).get(QuestionsViewModel.class);

        binding = FragmentQuestionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // set up viewPager2
        View load = root.findViewById(R.id.load);
        ViewPager2 profileContainer = root.findViewById(R.id.profile_container);
        load.setVisibility(View.VISIBLE);
        da.getAllUser(params -> {
            List<User> users = (List<User>) params[0];
            if (users.size() == 0) {
                Toast.makeText(getActivity(), "no user found", Toast.LENGTH_SHORT).show();
            }
//            ProfileAdapter profileAdapter = new ProfileAdapter(this.getActivity(), users, self);
            profileAdapter[0] = new ProfileAdapter(this.getActivity(), users, self);
            profileContainer.setAdapter(profileAdapter[0]);
            load.setVisibility(View.GONE);
        });

        profileContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                profileAdapter[0].updateRememberState(position);
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
}
