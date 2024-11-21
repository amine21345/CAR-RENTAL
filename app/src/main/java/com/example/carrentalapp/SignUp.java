package com.example.carrentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class SignUp extends AppCompatActivity {
    String name , email , password, phoneNo , username;
    TextInputLayout regName , regUsername , regEmail , regPhoneNo , regPassword;
    Button regBtn , regToLogin;
    String customFirebaseDatabaseUrl ="https://carloan-e0bbd-default-rtdb.europe-west1.firebasedatabase.app";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        regName = findViewById(R.id.fullName);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        regPhoneNo = findViewById(R.id.phone);
        regBtn = findViewById(R.id.signUpButton);
        regToLogin = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl);
        myRef = database.getReference("users");
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                name = regName.getEditText().getText().toString().trim();
                username = regUsername.getEditText().getText().toString().trim();
                email = regEmail.getEditText().getText().toString().trim();
                password = regPassword.getEditText().getText().toString().trim();
                phoneNo = regPhoneNo.getEditText().getText().toString().trim();
                if (isValidName(name) && isValidUsername(username) && isValidEmail(email) && isValidPassword(password) && isValidPhoneNo(phoneNo)){
                    // Create a User object
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    String uid = mAuth.getCurrentUser().getUid();
                                    User user = new User(name, username, email, password, phoneNo);

                                    // Save the user data to Firebase
                                    myRef.child(uid).setValue(user);
                                    Intent intent = new Intent(SignUp.this,Login.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                }else {
                    // At least one input value is not valid, show an error Toast
                    Toast.makeText(getApplicationContext(), "Invalid input. Please check your input values.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
        public boolean isValidName(String name) {
            // Add your validation logic for name (e.g., length, special characters, etc.)
            return !name.isEmpty();
        }

        public boolean isValidUsername(String username) {
            // Add your validation logic for username (e.g., length, allowed characters, etc.)
            return !username.isEmpty();
        }

        public boolean isValidEmail(String email) {

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            return email.matches(emailRegex);
        }

        public boolean isValidPassword(String password) {
            // Add your validation logic for password (e.g., length, complexity, etc.)
            return !password.isEmpty();
        }

        public boolean isValidPhoneNo(String phoneNo) {
            // Add your validation logic for phone number (e.g., length, numeric characters, etc.)
            return !phoneNo.isEmpty() && phoneNo.matches("\\d{8}"); // Assuming a 10-digit phone number
        }
}