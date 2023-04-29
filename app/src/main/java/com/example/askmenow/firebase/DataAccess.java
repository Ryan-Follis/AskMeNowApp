package com.example.askmenow.firebase;

import android.graphics.Bitmap;

import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public int getCount(CollectionReference collection) {
        return 0;
    }

    public List<User> getAllUser() {
        return new ArrayList<>();
    }

    public List<User> searchUser(String query) {
        return new ArrayList<>();
    }

    public List<Bitmap> getUserPics(String id) {
        return new ArrayList<>();
    }

    public List<QA> getDisplayQuestions(String id) {
        return new ArrayList<>();
    }

    public boolean checkLocation(String id1, String id2) {
        return false;
    }

    // get the user who is using this app
    public User getSelf() {
        return null;
    }
}
