package com.example.cheers.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cheers.R;
import com.example.cheers.ui.CreationsAdapter;
import com.example.cheers.ui.CreationsViewModel;

public class CreationsFragment extends Fragment {

    private CreationsViewModel viewModel;
    private CreationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_creations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCreations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CreationsAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CreationsViewModel.class);
        viewModel.getAllCreations().observe(getViewLifecycleOwner(), creations -> {
            adapter.setCreations(creations);
        });
    }
}