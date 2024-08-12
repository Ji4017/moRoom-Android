package com.moroom.android.presentation.nav;

import android.os.Bundle;
import android.widget.Toast;

import com.moroom.android.R;
import com.moroom.android.databinding.ActivityMainBinding;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
        setupBottomNavigation();
        navigateToDestination();
    }

    private void setupBottomNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void navigateToDestination() {
        int destination = getIntent().getIntExtra("nav_destination", -1);
        if(destination != -1) {
            binding.navView.setSelectedItemId(destination);
        }
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if(navController.getCurrentDestination().getId() == R.id.navigation_home) {
                if (System.currentTimeMillis() > backPressedTime + 2000) {
                    backPressedTime = System.currentTimeMillis();
                    Toast.makeText(getApplicationContext(), R.string.shut_down, Toast.LENGTH_SHORT).show();
                } else {
                    finishAffinity();
                }
            } else {
                navController.popBackStack();
            }
        }
    };
}