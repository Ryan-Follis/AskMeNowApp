package com.example.askmenow.utilities;

import java.util.ArrayList;
import java.util.List;

public class LocationChecker {
    public List<Integer> usersInArea(int[] users, int user){
        List<Integer> usersInRange = new ArrayList<Integer>(); //Array to hold users in range
        //Get original users location
        int userX = 0; //Get user lat and multiply to 69
        int userY = 0; //Get user long and multiply to 50
        //lat  69 miles
        //long 50 miles
        // circle centered around or maybe a square so +/- 10
        //greater than min x less than max x
        //greater than min y less than max y
        //Make equation to determine radius around
        //loop through all users and see if their location is within radius
        int newUserX;
        int newUserY;
        for(int i =0; i < users.length; i++){
            newUserX = 0; //Get users[i].lat*69
            newUserY = 0; //Get users[i].long*50
            if(newUserX>userX-10 && newUserX<userX+10 && newUserY>userY-10 && newUserY>userY+10){
                usersInRange.add(users[i]);
            }
        }
        //add user profiles who are in range
        //return list of users
        return usersInRange;
    }
}
