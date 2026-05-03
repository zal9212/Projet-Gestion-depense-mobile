package com.tp.gestiondepenses.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.tp.gestiondepenses.R;
import com.tp.gestiondepenses.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static boolean isChangingTheme = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences prefs = getSharedPreferences("FinanceTrackPrefs", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", true);
        int targetMode = darkMode ? androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES : androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
        
        if (androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode() != targetMode) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(targetMode);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // --- Robust Startup Logic ---
            android.view.Menu menu = binding.bottomNavigation.getMenu();
            android.content.res.Resources res = getResources();

            androidx.navigation.NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
            
            String savedPin = prefs.getString("user_pin", null);
            boolean onboardingDone = prefs.getBoolean("onboarding_done", false);
            String freq = prefs.getString("pin_frequency", "À chaque lancement");
            long lastLogin = prefs.getLong("last_pin_timestamp", 0);

            if (!onboardingDone) {
                navGraph.setStartDestination(R.id.navigation_welcome);
            } else if (savedPin != null) {
                // Check PIN frequency skip
                long diff = System.currentTimeMillis() - lastLogin;
                boolean skip = false;
                
                // If we are recreating because of a theme change, skip PIN
                if (isChangingTheme) {
                    skip = true;
                    isChangingTheme = false;
                } else if (diff < 15 * 1000) {
                    // Short grace period for general recreations (rotation, etc.)
                    skip = true;
                } else {
                    if (freq.equals("Une fois par jour") && diff < 24L * 3600 * 1000) skip = true;
                    else if (freq.equals("Une fois par semaine") && diff < 7L * 24 * 3600 * 1000) skip = true;
                    else if (freq.equals("Jamais")) skip = true;
                }

                if (skip) {
                    navGraph.setStartDestination(R.id.navigation_home);
                } else {
                    navGraph.setStartDestination(R.id.navigation_auth);
                }
            } else {
                navGraph.setStartDestination(R.id.navigation_home);
            }
            
            navController.setGraph(navGraph);

            // --- Explicit Navigation Handling ---
            // This ensures 100% reliable redirection even with dynamic start destinations
            binding.bottomNavigation.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                
                // Clear backstack up to Home to avoid nesting issues
                navController.popBackStack(R.id.navigation_home, false);
                
                if (itemId == R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                } else if (itemId == R.id.navigation_depenses) {
                    navController.navigate(R.id.navigation_depenses);
                    return true;
                } else if (itemId == R.id.navigation_revenus) {
                    navController.navigate(R.id.navigation_revenus);
                    return true;
                } else if (itemId == R.id.navigation_budgets) {
                    navController.navigate(R.id.navigation_budgets);
                    return true;
                }
                return false;
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.navigation_welcome ||
                    id == R.id.navigation_auth ||
                    id == R.id.navigation_add_depense || 
                    id == R.id.navigation_add_revenu ||
                    id == R.id.navigation_add_budget ||
                    id == R.id.navigation_add_categorie ||
                    id == R.id.navigation_categories ||
                    id == R.id.navigation_settings ||
                    id == R.id.navigation_transaction_detail) {
                    binding.bottomNavigation.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigation.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
