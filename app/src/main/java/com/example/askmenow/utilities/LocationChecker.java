package com.example.askmenow.utilities;


import android.content.Intent;
import android.os.Bundle;

import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.databinding.ActivityUsersBinding;
import com.example.askmenow.listeners.UserListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.askmenow.models.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;




public class LocationChecker  extends AppCompatActivity {
    private PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
    }
    public List<User> getUsers(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserID = preferenceManager.getString(Constants.KEY_USER_ID);
                        //List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserID.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.username = queryDocumentSnapshot.getString(Constants.KEY_USERNAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            user.latitude = queryDocumentSnapshot.getDouble(Constants.KEY_LATITUDE);
                            user.longitude = queryDocumentSnapshot.getDouble(Constants.KEY_LONGITUDE);
                            user.weekScore = (int[])queryDocumentSnapshot.get(Constants.KEY_WEEK_SCORE);
                            users.add(user);
                        }

                });
        return users;
    }
    public List<User> usersInArea(List<User> users, int range){
        //get activity get content and intent
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);


        //FirebaseFirestore database = FirebaseFirestore.getInstance();
        //database.collection(Constants.KEY_LOCATION)
        List<User> usersInRange = new ArrayList<User>(); //Array to hold users in range

        //Get original users location
        double userLat = Double.parseDouble(intent.getStringExtra(Constants.KEY_LATITUDE)); //Get user lat
        double userLon = Double.parseDouble(intent.getStringExtra(Constants.KEY_LONGITUDE)); //Get user long

        //loop through all users and see if their location is within radius
        double newUserLat;
        double newUserLon;
        double distance;
        for(int i =0; i < users.size(); i++){
            newUserLat = users.get(i).latitude;
            newUserLon = users.get(i).longitude;
            double lat = degToRad(newUserLat-userLat);
            double lon = degToRad(newUserLon-userLon);
            double lat1 = degToRad(userLat);
            double lat2 = degToRad(newUserLat);

            double a = Math.sin(lat/2) * Math.sin(lat/2) + Math.sin(lon/2) * Math.sin(lon/2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
            distance = c * 3963;
            if(distance < range){
                usersInRange.add(users.get(i));
            }
        }
        //add user profiles who are in range
        //return list of users
        return usersInRange;
    }
    public static double degToRad(double deg){
        return deg * Math.PI /180;
    }
    Calendar c = Calendar.getInstance();
    int day = c.get(Calendar.DAY_OF_WEEK) - 1;
    public void setUsersReputationForDay(int day){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        int[] week = intent.getIntArrayExtra(Constants.KEY_WEEK_SCORE); //Get user lat
        //User.asked * predetermined val
        int asked = 0;
        //User.answered * different val
        int answered = 0;
        //User.asked-answered * val
        int askedAnswered = 0;
        week[day] = asked*5 + answered*3 + askedAnswered*2;
        intent.putExtra(Constants.KEY_WEEK_SCORE, week);
    }

    public int getUserWeeklyRep(User user){
        int total = 0;
        for(int i=0; i < 7; i++){
            total+=user.weekScore[i];
        }
        return total;
    }



    public List<User> getUsersProfileHubFeed(int range){ //Get order of users in range given the range
        List<User> usersInRange = new ArrayList<>();
        User temp;
        usersInRange = usersInArea(getUsers(),range);
        int best;
        int position;
        for(int i = 0; i < usersInRange.size(); i++){
            for(int j = i+1; j < usersInRange.size(); j++){
                if(getUserWeeklyRep(usersInRange.get(i)) < getUserWeeklyRep(usersInRange.get(j))){
                    temp = usersInRange.get(i);
                    usersInRange.set(i,usersInRange.get(j));
                    usersInRange.set(j,temp);
                }
            }
        }
        return usersInRange;
    }


}
