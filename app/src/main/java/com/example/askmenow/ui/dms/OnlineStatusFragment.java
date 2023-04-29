package com.example.askmenow.ui.dms;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OnlineStatusFragment extends Fragment {

    private DocumentReference documentReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
    }

    @Override
    public void onPause(){
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY, 0);
    }

    @Override
    public void onResume(){
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY, 1);
    }

}
