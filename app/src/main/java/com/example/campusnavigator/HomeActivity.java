package com.example.campusnavigator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView studentNameTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView welcomeTextView;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeTextView = findViewById(R.id.student_name);

        // Get the current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Retrieve the user's name from Firebase Database
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = snapshot.child("name").getValue(String.class);
                        if (userName != null) {
                            welcomeTextView.setText("Hello " + userName);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }


        // Set listener for bottom navigation items
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_notice) {
                loadFragment(new NoticeFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_chat) {
                loadFragment(new ChatFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_profile) {
                // Handle profile menu item
                return true;
            } else if (item.getItemId() == R.id.menu_settings) {
                loadFragment(new SettingFragment());
                return true;
            }
            return false;
        });

        // Load the HomeFragment by default
        loadFragment(new HomeFragment());
    }


    // Method to load fragments into the FrameLayout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    // Method to set the welcome text with the fetched username
    public void setWelcomeText(TextView textView) {
        if (userName != null) {
            textView.setText("Hello " + userName);
        }
    }
}
