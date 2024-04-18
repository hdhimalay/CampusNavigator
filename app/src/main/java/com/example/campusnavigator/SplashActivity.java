package com.example.campusnavigator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // Splash screen delay time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay execution of the next activity using a Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the MainActivity after the splash delay
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish(); // Close the splash activity
            }
        }, SPLASH_DELAY);
    }
}

