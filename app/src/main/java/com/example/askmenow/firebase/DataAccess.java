package com.example.askmenow.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.askmenow.model.QA;
import com.example.askmenow.model.User;
import com.example.askmenow.utilities.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.List;

public class DataAccess {

    private static User self;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private Activity root;

    public static boolean checkResult(Task task) {
        return task != null && task.isSuccessful();
    }

    public static User docToUser(DocumentSnapshot document) {
        User user = new User();
        user.id = document.getId();
        user.name = document.getString(Constants.KEY_NAME);
        user.image = document.getString(Constants.KEY_IMAGE);
        user.username = document.getString(Constants.KEY_USERNAME);
        user.email = document.getString(Constants.KEY_EMAIL);
        Long tempAge = document.getLong(Constants.KEY_AGE);
        if (tempAge != null) {
            user.age = tempAge.intValue();
        }
        user.interests = (List<String>) document.get(Constants.KEY_INTERESTS);
        return user;
    }

    public void getUser(String id, DataAccessListener listener) {
        db.collection(Constants.KEY_COLLECTION_USERS).document(id)
                .get().addOnCompleteListener(task -> {
                    User user = new User();
                    if (checkResult(task)) {
                        user = docToUser(task.getResult());
                    }
                    listener.executeAfterComplete(user);
                });
    }

    // result is a list of all users.
    // result is an empty list if error happens.
    public void getAllUser(DataAccessListener listener) {
        db.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            List<User> result = new ArrayList<>();
            if (checkResult(task)) {
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    result.add(docToUser(document));
                }
            }
            listener.executeAfterComplete(result);
        });
    }

    // search users with username that starts with query string
    public void searchUser(String query, DataAccessListener listener) {
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereGreaterThanOrEqualTo(Constants.KEY_USERNAME, query)
                .whereLessThan(Constants.KEY_USERNAME, query + '\uF8FF').get()
                .addOnCompleteListener(task -> {
                    List<User> result = new ArrayList<>();
                    if (checkResult(task)) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            result.add(docToUser(document));
                        }
                    }
                    listener.executeAfterComplete(result);
                });
    }

    // result is a list of bitmaps.
    // result is an empty list if error happens.
    public void getUserPics(String id, DataAccessListener listener) {
        db.collection(Constants.KEY_COLLECTION_USERS).document(id).get().
                addOnCompleteListener(task->{
                    final List<Bitmap> result = new ArrayList<>();
                    final int[] picsWaiting = new int[1];
                    if(checkResult(task)) {
                        DocumentSnapshot document = task.getResult();
                        List<String> picUrls = (List<String>) document.get(Constants.KEY_PICS);
                        if (picUrls == null)
                            picUrls = new ArrayList<>();
                        picsWaiting[0] =picUrls.size() ;
                        for (String url : picUrls) {
                            StorageReference ref = storage.getReferenceFromUrl(url);
                            ref.getBytes(Constants.IMAGE_MAX_SIZE).addOnFailureListener(exception -> {
                                if (root != null)
                                    Toast.makeText(root, "Failed to retrieve some images. Check your network",
                                            Toast.LENGTH_SHORT).show();

                            }).addOnSuccessListener(bytes -> {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                result.add(bitmap);
                                picsWaiting[0]--;
                                if (picsWaiting[0] == 0)
                                    listener.executeAfterComplete(result);
                            });
                        }
                        if (picsWaiting[0] == 0) {
                            if (root != null)
                                Toast.makeText(root, "this user didn't upload any picture", Toast.LENGTH_SHORT).show();
                            listener.executeAfterComplete(result);
                        }
                    }
                });
    }

    public void getDisplayQuestions(String id, DataAccessListener listener) {
        db.collection(Constants.KEY_COLLECTION_QA).whereEqualTo(Constants.KEY_USER_ID, id).
                get().addOnCompleteListener(task -> {
                    List<QA> qaList = new ArrayList<>();
                    if (checkResult(task)) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            QA qa = new QA();
                            qa.setQuestion((String)document.get(Constants.KEY_QUESTION));
                            qa.setAnswers((List<String>) document.get(Constants.KEY_ANSWERS));
                            qaList.add(qa);
                        }
                    }
                    if (qaList.size() == 0 && root != null) {
                        Toast.makeText(root, "this user do not want to display any question", Toast.LENGTH_SHORT).show();
                    }
                    listener.executeAfterComplete(qaList);
                });
    }

    public void getField(String collection, String id, String field, DataAccessListener listener) {
        db.collection(collection).document(id).get().addOnCompleteListener(task -> {
            Object result = null;
            if (checkResult(task)) {
                result = task.getResult().get(field);
            }
            listener.executeAfterComplete(result);
        });
    }

    // change the value of a field, listener is executed after the request is complete.
    // listener will receive two parameters. The parameter indicates whether the field is update successfully.
    // the second parameter is the return result of the update request.
    //
    // you can add or delete fields through this method. To add a field, just pass in the field name and initial value.
    // to delete a field, use FieldValue.delete() as the value.
    //
    // cannot update array
    public void changeField(String collection, String id, String field, Object value,
                                   DataAccessListener listener) {
        DocumentReference target = db.collection(collection).document(id);
        target.update(field, value).addOnCompleteListener(task -> {
            if (checkResult(task)) {
                listener.executeAfterComplete(true, task);
            } else {
                listener.executeAfterComplete(false, task);
            }
        });
    }

    public void addToArray(String collection, String id, String field, Object append,
                            DataAccessListener listener) {
        DocumentReference target = db.collection(collection).document(id);
        target.update(field, FieldValue.arrayUnion(append)).addOnCompleteListener(task -> {
            if (checkResult(task)) {
                listener.executeAfterComplete(true, task);
            } else {
                listener.executeAfterComplete(false, task);
            }
        });
    }

    public void removeFromArray(String collection, String id, String field, Object remove,
                           DataAccessListener listener) {
        DocumentReference target = db.collection(collection).document(id);
        target.update(field, FieldValue.arrayRemove(remove)).addOnCompleteListener(task -> {
            if (checkResult(task)) {
                listener.executeAfterComplete(true, task);
            } else {
                listener.executeAfterComplete(false, task);
            }
        });
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

    public Activity getRoot() {
        return root;
    }

    public void setRoot(Activity root) {
        this.root = root;
    }
}
