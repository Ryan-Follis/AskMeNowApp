package com.example.askmenow.ui.questions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.askmenow.R;
import com.example.askmenow.utilities.SearchAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchUserFragment extends Fragment {

    private final String query;

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
        resultList.setAdapter(search(query));
        resultList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    private SearchAdapter search(String query) {
        // do search

        // return the result
        String[] test = new String[20];
        for (int i = 0; i < 20; i++) {
            test[i] = "test name " + query + " " + i;
        }

        return new SearchAdapter(this.getActivity(), test);
    }
}
