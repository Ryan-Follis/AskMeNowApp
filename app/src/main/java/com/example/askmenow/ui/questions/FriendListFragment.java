package com.example.askmenow.ui.questions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.askmenow.R;
import com.example.askmenow.activities.MainActivity;
import com.example.askmenow.firebase.FriendListOperations;
import com.example.askmenow.utilities.SearchAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendListFragment extends Fragment {

    private FriendListOperations oper;
    private String id;

    public FriendListFragment(String id) {
        this.id = id;
        oper = new FriendListOperations(id);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_friend_list, container, false);

        // check friend request
        View friendRequest = root.findViewById(R.id.new_friend_request);
        String friendId = checkFriendRequest(friendRequest);

        // set up buttons
        ImageButton returnProfileHub = root.findViewById(R.id.profile_hub);
        Button newFriendProfile = root.findViewById(R.id.view_friend_profile);
        Button declineFriend = root.findViewById(R.id.decline_friend);
        Button acceptFriend = root.findViewById(R.id.accept_friend);

        returnProfileHub.setOnClickListener(view->getActivity().onBackPressed());
        newFriendProfile.setOnClickListener(view->{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("dest", "search result");
            intent.putExtra("id", friendId);
            startActivity(intent);
        });
        declineFriend.setOnClickListener(view->{
            oper.updateFriendRequest(friendId, false);
            checkFriendRequest(friendRequest);
        });
        acceptFriend.setOnClickListener(view->{
            oper.updateFriendRequest(friendId, true);
            checkFriendRequest(friendRequest);
        });

        // show friend list
        RecyclerView resultList = root.findViewById(R.id.search_result_list);
        resultList.setAdapter(getFriends(id));
        resultList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    private SearchAdapter getFriends(String id) {
        return new SearchAdapter(this.getActivity(), oper.getFriendList());
    }

    // return the id of the user
    // return null if no friend request
    private String checkFriendRequest(View requestView) {
        String requestId = oper.getFriendRequest();
        if (requestId != null) {
            requestView.setVisibility(View.VISIBLE);
        } else {
            requestView.setVisibility(View.INVISIBLE);
        }
        return requestId;
    }

}
