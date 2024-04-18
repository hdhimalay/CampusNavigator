package com.example.campusnavigator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private TextView openLoginTextView;
    private CheckBox acceptTermsCheckbox;
    private Spinner courseSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        userNameEditText = findViewById(R.id.sign_up_user_name);
        emailEditText = findViewById(R.id.sign_up_user_email);
        passwordEditText = findViewById(R.id.sign_up_user_password);
        confirmPasswordEditText = findViewById(R.id.sign_up_confirm_password);
        signUpButton = findViewById(R.id.sign_up_btn);
        openLoginTextView=findViewById(R.id.openLogin);
        acceptTermsCheckbox = findViewById(R.id.accept_all_terms_checkbox);
        courseSpinner = findViewById(R.id.spinner_courses);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String userName = userNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                String selectedCourse = courseSpinner.getSelectedItem().toString();


                // Validate input fields
                if (TextUtils.isEmpty(userName)) {
                    userNameEditText.setError("Please enter your name");
                    return;
                }
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Please enter a valid email address");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    passwordEditText.setError("Password must be at least 6 characters long");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    confirmPasswordEditText.setError("Passwords do not match");
                    return;
                }
                if (!acceptTermsCheckbox.isChecked()) {
                    Toast.makeText(SignUpActivity.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

// Perform sign up with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success
                                    Toast.makeText(SignUpActivity.this, "Sign up Successful", Toast.LENGTH_SHORT).show();

                                    // Store additional user data in Firebase Database
                                    String userId = mAuth.getCurrentUser().getUid();
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", userName);
                                    userMap.put("email", email);
                                    userMap.put("phone", "");
                                    userMap.put("password", password);
                                    userMap.put("course",selectedCourse );

                                    // Set the user data in the database
                                    usersRef.child(userId).setValue(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Data successfully stored in the database
                                                        // Proceed to login activity or desired destination
                                                        // For example:
                                                        // startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                                        // finish();
                                                    } else {
                                                        // Error occurred while storing data
                                                        Toast.makeText(SignUpActivity.this, "Error storing user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Sign up failed
                                    Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });



        openLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }
}
