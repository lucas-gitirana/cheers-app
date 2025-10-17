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

// 1. A classe agora herda de Fragment, não de AppCompatActivity
public class CategoriesFragment extends Fragment {

    // 2. As variáveis de view e adapter são as mesmas
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private CategoryViewModel viewModel;

    public CategoriesFragment() {
        // Construtor vazio obrigatório para Fragments
    }

    // 3. O método onCreateView é onde o layout do Fragment é criado
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // "Infla" (cria a view) do layout XML e a retorna
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    // 4. O método onViewCreated é chamado logo após a view ser criada.
    // É o local ideal para inicializar as views e a lógica.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Acessamos o RecyclerView usando a 'view' do fragment
        recyclerView = view.findViewById(R.id.recyclerViewCategories);
        // Para o LayoutManager, usamos getContext() em vez de 'this'
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos o adapter com o contexto do fragment e uma lista vazia
        adapter = new CategoryAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // A inicialização do ViewModel é idêntica, mas passamos 'this' (o Fragment)
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // A observação do LiveData é quase idêntica, mas usamos getViewLifecycleOwner()
        // Isso garante que o observador só funcione quando a UI do fragment está ativa.
        viewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            // A lógica aqui dentro é a mesma
            adapter.setCategories(categories);
        });

        // A chamada para carregar os dados permanece a mesma
        viewModel.loadCategories();
    }
}
