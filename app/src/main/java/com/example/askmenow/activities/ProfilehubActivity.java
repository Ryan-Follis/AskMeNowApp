package com.example.askmenow.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.askmenow.R;
import com.example.askmenow.utilities.ProfileAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class ProfilehubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilehub);

        // set up viewPager2
        ViewPager2 profileContainer = findViewById(R.id.profile_container);
        ProfileAdapter profileAdapter = new ProfileAdapter();
        profileContainer.setAdapter(profileAdapter);

        profileContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
        // TabLayout dotIndicator = findViewById(R.id.dot_indicator);
        // new TabLayoutMediator(dotIndicator, picContainer, (tab, position) -> {}).attach();

        // register click events for top menu buttons
        ImageButton search = findViewById(R.id.search_user);
        ImageButton friendRequest = findViewById(R.id.friend_request);
        ImageButton rememberUser = findViewById(R.id.remember_user);
        ImageButton sendDM = findViewById(R.id.send_dm);
        search.setOnClickListener((View v)-> onSearchRequested());
        friendRequest.setOnClickListener((View v)->{

        });
        rememberUser.setOnClickListener((View v)->{

        });
        sendDM.setOnClickListener((View v)-> {
            // uploadPhoto(selectPhoto);
        });
    }

    // this activity launcher gets the photo that the user chooses and pass it to uploadPhotoActivity
    ActivityResultLauncher<Intent> selectPhotoActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent photo = result.getData();
                    if (photo != null && photo.getData() != null) {
                        Uri photoUri = photo.getData();
                        // pass the URI to uploadPhotoActivity
                        Intent editText = new Intent(ProfilehubActivity.this, UploadPhotoActivity.class);
                        editText.putExtra("photoURI", photoUri.toString());
                        ProfilehubActivity.this.startActivity(editText);
                    }
                }
            });

    public void chooseImage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        selectPhotoActivity.launch(i);
    }

    public void uploadPhoto(ImageButton selectPhoto) {
        // set up the popup menu
        PopupMenu photoMenu = new PopupMenu(ProfilehubActivity.this, selectPhoto);
        photoMenu.getMenuInflater().inflate(R.menu.select_photo_menu, photoMenu.getMenu());

        photoMenu.setOnMenuItemClickListener((MenuItem item) -> {
            if (item.getItemId() == R.id.gallery) {
                // select photo from the gallery
                chooseImage();
            }
            return true;
        });
        photoMenu.show();
    }
}