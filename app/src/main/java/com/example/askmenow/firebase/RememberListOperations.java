package com.example.askmenow.firebase;

import android.widget.Toast;

import com.example.askmenow.model.User;
import com.example.askmenow.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RememberListOperations {

    private static final DataAccess da = new DataAccess();

//    // return the string of the user id
//    // return null if no new friend request
//    public String getFriendRequest() {
//        return null;
//    }
//
//    // requestId - the id of the user that sends the request
//    // accept - whether the user accept the request
//    public void updateFriendRequest(String requestId, boolean accept) {
//
//    }

    public static void getRememberList(DataAccessListener listener) {
        da.getField(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, params -> {
            List<String> resultList = (List<String>) params[0];
            List<User> rememberList = new ArrayList<>();
            if (resultList != null && resultList.size() != 0) {
                final int[] taskWaiting = new int[1];
                taskWaiting[0] = resultList.size();
                for (String id : resultList)
                    da.getUser(id, params1 -> {
                        rememberList.add((User) params1[0]);
                        taskWaiting[0]--;
                        if (taskWaiting[0] == 0)
                            listener.executeAfterComplete(rememberList);
                    });
            }
            listener.executeAfterComplete(rememberList);
        });
    }

    public static void rememberUser(String userId) {
        da.addToArray(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, userId, params -> {
            Boolean result = (Boolean) params[0];
            if (!result)
                Toast.makeText(da.getRoot(), "Failed to add this user to your remember list", Toast.LENGTH_SHORT).show();
        });
    }

    public static void forgetUser(String userId) {
        da.removeFromArray(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, userId, params -> {
            Boolean result = (Boolean) params[0];
            if (!result)
                Toast.makeText(da.getRoot(), "Failed to remove this user from your remember list", Toast.LENGTH_SHORT).show();
        });
    }

    public static void rememberUser(String userId, List<String> list) {
        da.addToArray(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, userId, params -> {
            Boolean result = (Boolean) params[0];
            if (!result) {
                Toast.makeText(da.getRoot(), "Failed to add this user to your remember list", Toast.LENGTH_SHORT).show();
                list.remove(userId);
            }
        });
    }

    public static void forgetUser(String userId, List<String> list) {
        da.removeFromArray(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, userId, params -> {
            Boolean result = (Boolean) params[0];
            if (!result) {
                Toast.makeText(da.getRoot(), "Failed to remove this user from your remember list", Toast.LENGTH_SHORT).show();
                list.add(userId);
            }
        });
    }

    public static void remembered(String userId, DataAccessListener listener) {
        da.getField(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, params -> {
            List<String> rememberList = (List<String>) params[0];
            if (rememberList == null) {
                listener.executeAfterComplete(false);
            } else {
                listener.executeAfterComplete(rememberList.contains(userId));
            }
        });
    }

    public static void remembered(String userId, int position, DataAccessListener listener) {
        da.getField(Constants.KEY_COLLECTION_USERS, da.getSelf().id, Constants.KEY_REMEMBER, params -> {
            List<String> rememberList = (List<String>) params[0];
            if (rememberList == null) {
                listener.executeAfterComplete(false, position);
            } else {
                listener.executeAfterComplete(rememberList.contains(userId), position);
            }
        });
    }
}
