package com.example.askmenow.ui.dms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.askmenow.activities.SignInActivity;
import com.example.askmenow.activities.UsersActivity;
import com.example.askmenow.databinding.ActivityDirectMessagesBinding;
import com.example.askmenow.databinding.FragmentDmsBinding;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class DMsFragment extends Fragment {

    /* private FragmentDmsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DMsViewModel homeViewModel =
                new ViewModelProvider(this).get(DMsViewModel.class);

        binding = FragmentDmsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDms;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    } */

    private ActivityDirectMessagesBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDirectMessagesBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_direct_messages);
        // setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();
    }

    private void setListeners(){
        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.newChat.setOnClickListener(v ->
                startActivity(new Intent(getActivity().getApplicationContext(), UsersActivity.class)));
    }

    private void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.profileImage.setImageBitmap(bitmap);
    }

    private void showToast(String message){
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
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

    // This method should be moved into the personalProfile activity if we want
    // signing out to occur through that section of the app.
    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getActivity().getApplicationContext(), SignInActivity.class));
                    getActivity().finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out."));
    }
}