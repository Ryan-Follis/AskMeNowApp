package com.example.askmenow.ui.questions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.askmenow.databinding.FragmentQuestionsBinding;
import com.example.askmenow.ui.map.MapViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class QuestionsFragment extends Fragment {
    private FragmentQuestionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentQuestionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textMap;
        //mapViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
