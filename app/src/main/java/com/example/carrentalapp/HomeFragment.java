package com.example.carrentalapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    DatabaseReference carsRef;
    private static final int REQUEST_CODE_RENT_CAR = 1; // You can use any integer value

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String databaseUrl = "https://carloan-e0bbd-default-rtdb.europe-west1.firebasedatabase.app";
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(databaseUrl);
        carsRef = firebaseDatabase.getReference("cars");
        Log.d("car ref",carsRef.toString());
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout containerLayout = view.findViewById(R.id.containerLayout);
        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                    CarData car = carSnapshot.getValue(CarData.class); // Assuming you have a Car class
                    LinearLayout linearLayout = createCardView(car);

                    // Add the LinearLayout to your main layout
                    containerLayout.addView(linearLayout);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private LinearLayout createCardView(CarData car) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        int padding = 16; // Change this value as needed
        int marginVertical = 20; // Change this value as needed
        linearLayout.setPadding(padding, padding, padding, padding);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.setMargins(0, marginVertical, 0, marginVertical);
        linearLayout.setLayoutParams(layoutParams);




        // Find views inside the layout
        ImageView imageView = new ImageView(getContext());
        TextView modelTextView = new TextView(getContext());
        TextView fuelTextView = new TextView(getContext());
        TextView priceTextView = new TextView(getContext());
        TextView numberTextView = new TextView(getContext());
        Button rentButton = new Button(getContext());

        //imageView.setImageResource(car.getCarImageResource()); // Replace with your logic
        modelTextView.setText("Model: " + car.getModel());
        fuelTextView.setText("Fuel: " + car.getFuel());
        priceTextView.setText("Price: " + car.getPrice());
        numberTextView.setText("Number: " + car.getNumber());

        int textSize = 20; // Change this value as needed
        modelTextView.setTextSize(textSize);
        fuelTextView.setTextSize(textSize);
        priceTextView.setTextSize(textSize);
        numberTextView.setTextSize(textSize);

        rentButton.setText("Rent Car");
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rentIntent = new Intent(getContext(), RentCarActivity.class);

                // Add the car number as an extra
                rentIntent.putExtra("carModel", car.getModel());
                rentIntent.putExtra("carFuel", car.getFuel());
                rentIntent.putExtra("carPrice", car.getPrice());
                rentIntent.putExtra("carNumber", car.getNumber());

                // Start the activity for result
                startActivity(rentIntent);
            }
        });

        // Add the layout to the CardView
        linearLayout.addView(imageView);
        linearLayout.addView(modelTextView);
        linearLayout.addView(fuelTextView);
        linearLayout.addView(priceTextView);
        linearLayout.addView(numberTextView);
        linearLayout.addView(rentButton);


        return linearLayout;
    }

}