package com.example.askmenow.firebase;

import com.example.askmenow.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FriendListOperations {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String userId;

    public FriendListOperations(String id) {
        userId = id;
    }

    // return the string of the user id
    // return null if no new friend request
    public String getFriendRequest() {
        return null;
    }

    // requestId - the id of the user that sends the request
    // accept - whether the user accept the request
    public void updateFriendRequest(String requestId, boolean accept) {

    }

    public List<User> getFriendList() {
        return null;
    }
}
