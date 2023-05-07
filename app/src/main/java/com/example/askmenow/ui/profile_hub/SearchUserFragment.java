package com.example.askmenow.ui.profile_hub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.askmenow.R;
import com.example.askmenow.firebase.DataAccess;
import com.example.askmenow.models.User;
import com.example.askmenow.adapters.SearchAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserFragment extends Fragment {

    private final String query;
    private final DataAccess da = new DataAccess();

    public SearchUserFragment(String query) {
        this.query = query;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_user, container, false);

        ImageButton returnProfileHub = root.findViewById(R.id.profile_hub);
        returnProfileHub.setOnClickListener(view->getActivity().onBackPressed());
        RecyclerView resultList = root.findViewById(R.id.search_result_list);

        // do search
        View loadResult = root.findViewById(R.id.load_result);
        loadResult.setVisibility(View.VISIBLE);
        da.searchUser(query, params -> {
            List<User> users = (List<User>) params[0];
            resultList.setAdapter(new SearchAdapter(getActivity(), users));
            resultList.setLayoutManager(new LinearLayoutManager(getActivity()));
            loadResult.setVisibility(View.GONE);
        });

        return root;
    }
}