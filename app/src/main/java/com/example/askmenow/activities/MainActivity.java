package com.example.askmenow.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.askmenow.R;
import com.example.askmenow.databinding.ActivityMainBinding;
import com.example.askmenow.ui.questions.FriendListFragment;
import com.example.askmenow.ui.questions.SearchResultFragment;
import com.example.askmenow.ui.questions.SearchUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dms, R.id.navigation_map, R.id.navigation_notifications,
                R.id.navigation_questions, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // change fragment
        callFragment(getIntent());
    }

    // call a fragment
    // the intent needs to have a dest string extra
    private void callFragment(Intent intent) {
        String destination = intent.getStringExtra("dest");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // get query and pass to SearchUserFragment
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchUserFragment searchUser = new SearchUserFragment(query);
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, searchUser).commit();
        } else if (destination != null) {
            if (destination.equals("search result")) {
                // show search result
                String id = intent.getStringExtra("id");
                SearchResultFragment searchResult = new SearchResultFragment(id);
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, searchResult).commit();
            } else if (destination.equals("friend list")) {
                // show friend list
                String id = intent.getStringExtra("id");
                FriendListFragment friendList = new FriendListFragment(id);
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, friendList).commit();
            }
        }
    }
}