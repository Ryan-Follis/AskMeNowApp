package com.example.askmenow.ui.dms;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askmenow.R;
import com.example.askmenow.activities.ChatActivity;
import com.example.askmenow.activities.SignInActivity;
import com.example.askmenow.activities.UsersActivity;
import com.example.askmenow.adapters.RecentConversationsAdapter;
import com.example.askmenow.databinding.FragmentDmsBinding;
import com.example.askmenow.listeners.ConversionListener;
import com.example.askmenow.models.ChatMessage;
import com.example.askmenow.models.User;
import com.example.askmenow.utilities.Constants;
import com.example.askmenow.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DMsFragment extends OnlineStatusFragment implements ConversionListener {

    private FragmentDmsBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private RecyclerView conversationsRecyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDmsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        conversationsRecyclerView = root.findViewById(R.id.conversationsRecyclerView);
        progressBar = root.findViewById(R.id.progressBar);
        initializeDMsFragment();
        setListeners();
        listenConversations();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentDmsBinding.inflate(getLayoutInflater());
        // setContentView(R.layout.activity_direct_messages);
        // setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        loadUserDetails();
    }

    private void initializeDMsFragment(){
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
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

    private void listenConversations(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = senderID;
                    chatMessage.receiverID = receiverID;
                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderID)){
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }
                    else{
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                }
                else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for(int i = 0; i < conversations.size(); i++){
                        String senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if(conversations.get(i).senderID.equals(senderID) && conversations.get(i).receiverID.equals(receiverID)){
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            conversationsRecyclerView.smoothScrollToPosition(0);
            conversationsRecyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    };

    // This method should be moved into the personalProfile activity if we want
    // signing out to occur through that section of the app.
    private void signOut(){
        // MainActivity.signOut();
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

    @Override
    public void onConversionClicker(User user) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}