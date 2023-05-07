package com.example.askmenow.ui.profile_hub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.askmenow.R;
import com.example.askmenow.firebase.RememberListOperations;
import com.example.askmenow.models.User;
import com.example.askmenow.adapters.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RememberListFragment extends Fragment {
    private final String id;

    public RememberListFragment(String id) {
        this.id = id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_user, container, false);

//        // check friend request
//        View friendRequest = root.findViewById(R.id.new_friend_request);
//        String friendId = checkFriendRequest(friendRequest);
//
//        // set up buttons
//        ImageButton returnProfileHub = root.findViewById(R.id.profile_hub);
//        Button newFriendProfile = root.findViewById(R.id.view_friend_profile);
//        Button declineFriend = root.findViewById(R.id.decline_friend);
//        Button acceptFriend = root.findViewById(R.id.accept_friend);
//
//        returnProfileHub.setOnClickListener(view->getActivity().onBackPressed());
//        newFriendProfile.setOnClickListener(view->{
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.putExtra("dest", "search result");
//            intent.putExtra("id", friendId);
//            startActivity(intent);
//        });
//        declineFriend.setOnClickListener(view->{
//            oper.updateFriendRequest(friendId, false);
//            checkFriendRequest(friendRequest);
//        });
//        acceptFriend.setOnClickListener(view->{
//            oper.updateFriendRequest(friendId, true);
//            checkFriendRequest(friendRequest);
//        });

        // show remember list
        RecyclerView resultList = root.findViewById(R.id.search_result_list);
        ImageButton ret = root.findViewById(R.id.profile_hub);
        ret.setOnClickListener(v -> getActivity().onBackPressed());
        View load = root.findViewById(R.id.load_result);
        RememberListOperations.getRememberList(params -> {
            List<User> list = (List<User>) params[0];
            resultList.setAdapter(new SearchAdapter(getActivity(), list));
            resultList.setLayoutManager(new LinearLayoutManager(getActivity()));
            load.setVisibility(View.GONE);
        });

        return root;
    }

//    // return the id of the user
//    // return null if no friend request
//    private String checkFriendRequest(View requestView) {
//        String requestId = oper.getFriendRequest();
//        if (requestId != null) {
//            requestView.setVisibility(View.VISIBLE);
//        } else {
//            requestView.setVisibility(View.INVISIBLE);
//        }
//        return requestId;
//    }

}