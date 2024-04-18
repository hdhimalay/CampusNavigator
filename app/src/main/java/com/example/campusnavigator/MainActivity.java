package com.example.campusnavigator;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Redirect to SplashActivity
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish(); // Close MainActivity so that it doesn't appear in the back stack
    }
}
