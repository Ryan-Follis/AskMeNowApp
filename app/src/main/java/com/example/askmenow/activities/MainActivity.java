package com.example.askmenow.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.askmenow.R;
import com.example.askmenow.databinding.ActivityMainBinding;
import com.example.askmenow.models.User;
import com.example.askmenow.ui.dms.DMsFragment;
import com.example.askmenow.ui.personal_profile.PersonalProfileFragment;
import com.example.askmenow.ui.profile_hub.ProfileHubFragment;
import com.example.askmenow.ui.profile_hub.RememberListFragment;
import com.example.askmenow.ui.profile_hub.SearchResultFragment;
import com.example.askmenow.ui.profile_hub.SearchUserFragment;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dms, R.id.navigation_profile_hub,
                R.id.navigation_map, R.id.navigation_personal_profile)
                .build(); //R.id.navigation_notifications,
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        preferenceManager = new PreferenceManager(getApplicationContext());
        getToken();

        getLocationPermission();
        updateLocationInFirebase();

        callFragment(getIntent());
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
                .addOnFailureListener(e -> showToast("Unable to update token."));
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void updateLocationInFirebase() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location lastKnownLocation = task.getResult();
                            //Updates gps location to firebase
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            DocumentReference documentReference =
                                    database.collection(Constants.KEY_COLLECTION_USERS).document(
                                            preferenceManager.getString(Constants.KEY_USER_ID)
                                    );
                            documentReference.update(Constants.KEY_LATITUDE, lastKnownLocation.getLatitude());
                            documentReference.update(Constants.KEY_LONGITUDE, lastKnownLocation.getLongitude());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
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
                User user = (User) intent.getSerializableExtra("user");
                SearchResultFragment searchResult = new SearchResultFragment(user);
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