package com.tp.gestiondepenses.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tp.gestiondepenses.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.content.SharedPreferences prefs = getSharedPreferences("FinanceTrackPrefs", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", true);
        int targetMode = darkMode ? androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES : androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
        if (androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode() != targetMode) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(targetMode);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}
