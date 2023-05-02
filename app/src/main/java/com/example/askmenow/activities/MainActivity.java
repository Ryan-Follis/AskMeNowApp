package com.example.askmenow.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.askmenow.R;
import com.example.askmenow.databinding.ActivityMainBinding;
import com.example.askmenow.ui.profile_hub.RememberListFragment;
import com.example.askmenow.ui.profile_hub.SearchResultFragment;
import com.example.askmenow.ui.profile_hub.SearchUserFragment;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

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
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        preferenceManager = new PreferenceManager(getApplicationContext());
        getToken();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        Task<String> tokenTask = FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
        if(tokenTask.toString() == null){
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
    }

    private void updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                // Line commented out below may not be needed
                // .addOnSuccessListener(unused -> showToast("Token updated successfully."))
                .addOnFailureListener(e -> showToast("Unable to update token."));
    }

    // Added by Letong
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
                RememberListFragment friendList = new RememberListFragment(id);
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, friendList).commit();
            }
        }
    }

}