package com.example.campusnavigator;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private TextView adminLoginTextView;
    private TextView loginPageTextTextView;
    private TextView forgotPasswordTextView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference userTokensRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        userTokensRef = FirebaseDatabase.getInstance().getReference().child("user_tokens");


        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        signUpTextView = findViewById(R.id.openSignUp);
        loginPageTextTextView=findViewById(R.id.loginPageText);
        adminLoginTextView = findViewById(R.id.adminLogin);
        forgotPasswordTextView=findViewById(R.id.forgotPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Please enter a valid email address");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Please enter your password");
                    return;
                }

                // Check if the login attempt is from admin login section
                boolean isAdminLogin = loginPageTextTextView.getText().toString().equals("Admin!");

                // Perform login with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Check if the logged-in user is admin
                                        boolean isAdmin = email.equals("admin@igdtuw.ac.in");

                                        // Proceed with login logic based on isAdmin
                                        if (isAdmin) {
                                            // Admin login success
                                            Toast.makeText(LoginActivity.this, "Admin login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, NoticeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Student login success
                                            Toast.makeText(LoginActivity.this, "Student login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        // Generate and store FCM token
                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                            return;
                                                        }

                                                        // Get new FCM registration token
                                                        String token = task.getResult();
                                                        Log.i("myToken", token);

                                                        // Store the token in Firebase Realtime Database
                                                        if (!isAdmin) {
                                                            userTokensRef.child(user.getUid()).setValue(token);
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Open sign-up activity
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error opening sign-up activity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adminLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentLoginType = loginPageTextTextView.getText().toString();
                if (currentLoginType.equals("Admin!")) {
                    loginPageTextTextView.setText("Students, Welcome to");
                    signUpTextView.setVisibility(View.VISIBLE);
                    adminLoginTextView.setText("Admin Login");
                } else {
                    loginPageTextTextView.setText("Admin!");
                    signUpTextView.setVisibility(View.GONE);
                    adminLoginTextView.setText("Student LogIn");
                }
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Please enter a valid email address");
                    return;
                }

                // Initiate password reset process
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Password reset email sent. Check your email inbox.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send password reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Firebase AuthStateListener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.getEmail().equals("admin@igdtuw.ac.in")) {
                        // Admin is signed in
                        Intent intent = new Intent(LoginActivity.this, NoticeActivity.class);
                        startActivity(intent);
                        finish(); // Finish LoginActivity to prevent the user from going back
                    } else {
                        // Student is signed in
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Add listener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove listener
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
