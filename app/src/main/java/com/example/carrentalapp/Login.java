package com.example.carrentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    String regEmail , regPassword;
    Button loginButton , callSignUp;
    ImageView image;
    TextView logoText;
    TextInputLayout email ,password;
    String customFirebaseDatabaseUrl ="https://carloan-e0bbd-default-rtdb.europe-west1.firebasedatabase.app";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            checkUserInDatabase(uid);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginButton = findViewById(R.id.loginButton);
        callSignUp = findViewById(R.id.signUp);
        image = findViewById(R.id.logoImage);
        logoText = findViewById(R.id.logoName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl);
        myRef = database.getReference("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regEmail = email.getEditText().getText().toString().trim();
                regPassword = password.getEditText().getText().toString().trim();
                if (isValidEmail(regEmail) && isValidPassword(regPassword)) {

                    mAuth.signInWithEmailAndPassword(regEmail, regPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Login Successful.",
                                                Toast.LENGTH_SHORT).show();
                                        String uid = mAuth.getCurrentUser().getUid();
                                        checkUserInDatabase(uid);
                                    } else {
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View , String>(image, "logo_image");
                pairs[1] = new Pair<View , String>(logoText, "logo_text");
                pairs[2] = new Pair<View , String>(email, "username_trans");
                pairs[3] = new Pair<View , String>(password, "password_trans");
                pairs[4] = new Pair<View , String>(loginButton, "button_tran");
                pairs[5] = new Pair<View , String>(callSignUp, "login_signup_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });

    }

    private void checkUserInDatabase(String uid) {
        Query checkUser = myRef.child(uid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String nameFromDB = snapshot.child("name").getValue(String.class);
                    String usernameFromDB = snapshot.child("username").getValue(String.class);
                    String phoneFromDB = snapshot.child("phoneNo").getValue(String.class);
                    String passwordFromDB = snapshot.child("password").getValue(String.class);
                    String emailFromDB = snapshot.child("email").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("phone", phoneFromDB);
                    intent.putExtra("password", passwordFromDB);
                    intent.putExtra("email", emailFromDB);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoginActivity", "Error retrieving user data from database: " + error.getMessage());
                Toast.makeText(Login.this, "Error retrieving user data from database", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isValidEmail(String email) {
        // Add your validation logic for email (e.g., using regular expressions)
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    public boolean isValidPassword(String password) {
        // Add your validation logic for password (e.g., length, complexity, etc.)
        return !password.isEmpty();
    }
}