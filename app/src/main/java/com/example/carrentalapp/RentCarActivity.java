package com.example.carrentalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RentCarActivity extends AppCompatActivity {
    private String selectedDate1 = "";
    private String selectedDate2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        String carModel = getIntent().getStringExtra("carModel");
        String carFuel = getIntent().getStringExtra("carFuel");
        String carPrice = getIntent().getStringExtra("carPrice");
        String carNumber = getIntent().getStringExtra("carNumber");


        // Retrieve user ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : "";
        //



        // Assuming you have a TextView in your layout to display the car number
        Button rentButton = findViewById(R.id.rentBtn);
        TextInputEditText datePickerEditText = findViewById(R.id.datePickerEditText);
        TextInputEditText datePickerEditText1 = findViewById(R.id.datePickerEditText1);
        TextView carModelTextView = findViewById(R.id.carModelTextView);
        TextView carFuelTextView = findViewById(R.id.carFuelTextView);
        TextView carPriceTextView = findViewById(R.id.carPriceTextView);
        carModelTextView.setText("Car model: " + carModel);
        carFuelTextView.setText("Car fuel: " + carFuel);
        carPriceTextView.setText("Car Price: " + carPrice);

        //first date
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Dates")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build();

        datePickerEditText.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), datePicker.toString()));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDate1 = formatDate(selection);

        });

        //second date
        MaterialDatePicker<Long> datePicker1 = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Dates")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build();

        datePickerEditText1.setOnClickListener(v -> datePicker1.show(getSupportFragmentManager(), datePicker1.toString()));

        datePicker1.addOnPositiveButtonClickListener(selection -> {
            selectedDate2 = formatDate(selection);

        });
        rentButton.setOnClickListener(v -> {
            if (!selectedDate1.isEmpty() && !selectedDate2.isEmpty()) {
                // Store data in Firebase Realtime Database
                storeDataInFirebase(carNumber, userId, selectedDate1, selectedDate2);
                Toast.makeText(this, "stored succes", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where one or both dates are not selected
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String formatDate(Long selection) {
        // Implement your own logic to format the date
        // For example:
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(selection));
    }
    private void storeDataInFirebase(String carNumber, String userId, String date1, String date2) {
        String customFirebaseDatabaseUrl ="https://carloan-e0bbd-default-rtdb.europe-west1.firebasedatabase.app";
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl);
        DatabaseReference databaseReference = database.getReference("rentals");
        RentalInfo rentalInfo = new RentalInfo(carNumber, userId, date1, date2);
        databaseReference.push().setValue(rentalInfo);
    }
}