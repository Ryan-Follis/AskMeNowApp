package com.example.askmenow.firebase;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;
import com.example.askmenow.utilities.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    private static User self;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static boolean checkResult(Task<QuerySnapshot> task) {
        return task != null && task.isSuccessful();
    }

    // result will be the number of data entries in a collection
    // result will be -1 if cannot access the database
    public void getCount(String collection, DataAccessListener listener) {
        final int[] result = new int[1];
        db.collection(collection).get().addOnCompleteListener(task -> {
            if (checkResult(task)) {
                result[0] = task.getResult().size();
            } else {
                result[0] = -1;
            }
            listener.executeAfterComplete(result[0]);
        });
    }

    // result is a list of all users.
    // result is an empty list if error happens.
    public void getAllUser(DataAccessListener listener) {
        final List<User> result = new ArrayList<User>();
        db.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            if (checkResult(task)) {
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    User user = new User();
                    user.id = document.getId();
                    user.name = document.getString(Constants.KEY_NAME);
                    user.image = document.getString(Constants.KEY_IMAGE);
                    user.username = document.getString(Constants.KEY_USERNAME);
                    user.email = document.getString(Constants.KEY_EMAIL);
                    result.add(user);
                }
            }
            listener.executeAfterComplete(result);
        });
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
    public static User getSelf() {
        return self;
    }

    public static void setSelf(User self) {
        DataAccess.self = self;
    }
}
