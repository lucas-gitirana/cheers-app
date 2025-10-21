package com.example.cheers.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheers.R;
import com.example.cheers.ui.CategoryAdapter;
import com.example.cheers.ui.CategoryViewModel;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryViewModel viewModel;

    public CategoriesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CategoryAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        viewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            adapter.setCategories(categories);
        });

        viewModel.loadCategories();
    }
}
