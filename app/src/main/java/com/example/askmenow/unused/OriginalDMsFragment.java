package com.example.askmenow.unused;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.askmenow.databinding.FragmentDmsBinding;

public class OriginalDMsFragment extends Fragment {

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
}