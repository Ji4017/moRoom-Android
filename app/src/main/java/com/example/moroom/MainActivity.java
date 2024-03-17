package com.example.moroom;

import android.os.Bundle;

import com.example.moroom.databinding.ActivityMainBinding;
import com.example.moroom.navui.dashboard.DashboardFragment;
import com.example.moroom.navui.home.HomeFragment;
import com.example.moroom.navui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // activity_main.xml의 BottomNavigationView 가져오기
        BottomNavigationView navView = binding.navView;

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navView, navController);


        // Intent에서 선택한 프래그먼트 가져오기
        String selectedFragment;
        selectedFragment = getIntent().getStringExtra("selectedFragment");

        // SearchedActivity에서 사용자가 터치한 아이콘(홈,지도,프로필)에 따라 프래그먼트에 따라 이동
        if (selectedFragment != null) {
            switch (selectedFragment) {
                case "home":
                    navController.navigate(R.id.navigation_home);
//                    navigateToFragment(new HomeFragment());
                    break;
                case "dashboard":
                    navController.navigate(R.id.navigation_dashboard);
//                    navigateToFragment(new DashboardFragment());
                    break;
                case "profile":
                    navController.navigate(R.id.navigation_profile);
//                    navigateToFragment(new ProfileFragment());
                    break;
            }
        }

        // BottomNavigationView 리스너 설정
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
            }

            // 프래그먼트 이동 메서드 호출
            navigateToFragment(fragment);
            return true;
        });

    }

    // 프래그먼트로 이동하는 메서드
    private void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
        if (currentFragment instanceof ProfileFragment || currentFragment instanceof DashboardFragment) {
            // ProfileFragment에서 뒤로가기를 눌렀을 때 HomeFragment로 이동
            showHomeFragment();
        } else{
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                finishAffinity();
            }
        }
    }

    public void showHomeFragment() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}