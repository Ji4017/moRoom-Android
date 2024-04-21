package com.moroom.android;

import android.os.Bundle;
import android.widget.Toast;

import com.moroom.android.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBottomNavigationView();
        navigateToDestination();
    }

    private void setupBottomNavigationView() {
        // NavController 설정
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        // NavController와 BottomNavigationView 연결
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void navigateToDestination() {
        int destination = getIntent().getIntExtra("nav_destination", -1);
        if(destination != -1) {
            binding.navView.setSelectedItemId(destination);
        }
    }

    @Override
    public void onBackPressed() {
        if (navController.popBackStack()) {
            return;
        }

        if (System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), R.string.shut_down, Toast.LENGTH_SHORT).show();
        } else {
            finishAffinity();
        }
    }
}