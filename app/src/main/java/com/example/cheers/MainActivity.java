package com.example.cheers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.cheers.ui.CameraActivity;
import com.example.cheers.ui.FavoritesActivity;
import com.example.cheers.ui.fragments.CategoriesFragment;
import com.example.cheers.ui.fragments.CreationsFragment;
import com.example.cheers.ui.fragments.HomeFragment;
import com.example.cheers.ui.fragments.SearchFragment;
import com.example.cheers.workers.RandomDrinkWorker;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // O "toggle" é o ícone de menu (hambúrguer) que abre e fecha o drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        // Abre o fragment Home como tela inicial, se não houver estado salvo.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        requestNotificationPermission();

        // Agendamento do serviço em background
        scheduleRandomDrinkWorker();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
        } else if (itemId == R.id.nav_categories) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();
        } else if (itemId == R.id.navigation_creations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreationsFragment()).commit();
        } else if (itemId == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (itemId == R.id.nav_camera) {
            startActivity(new Intent(this, CameraActivity.class));
            testRandomDrinkWorkerImmediately();
        }

        // Fecha o menu após o clique.
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void scheduleRandomDrinkWorker() {
        // 1. Define a requisição para rodar periodicamente (ex: a cada 24 horas)
        PeriodicWorkRequest randomDrinkRequest =
                new PeriodicWorkRequest.Builder(RandomDrinkWorker.class, 24, TimeUnit.HOURS)
                        // Você pode adicionar restrições, como "rodar apenas com Wi-Fi"
                        // .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build())
                        .build();

        // 2. Agenda a tarefa usando um nome único.
        // A política REPLACE garante que, se já houver uma tarefa com esse nome, ela será substituída.
        // Isso evita criar tarefas duplicadas toda vez que o app abre.
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "randomDrinkSuggestion",
                ExistingPeriodicWorkPolicy.REPLACE,
                randomDrinkRequest
        );
    }

    private void requestNotificationPermission() {
        // A permissão só é necessária para Android 13 (TIRAMISU) ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verifica se a permissão AINDA NÃO foi concedida
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicita a permissão ao usuário
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    /** MÉTODO DE TESTE IMEDIATO DE NOTIFICAÇÃO */
    private void testRandomDrinkWorkerImmediately() {
        OneTimeWorkRequest testRequest = new OneTimeWorkRequest.Builder(RandomDrinkWorker.class).build();
        WorkManager.getInstance(this).enqueue(testRequest);
        Toast.makeText(this, "Tarefa de teste agendada!", Toast.LENGTH_SHORT).show();
    }
}
