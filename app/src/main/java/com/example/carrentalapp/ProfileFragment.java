package com.example.carrentalapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView fullNameLabel , usernameLabel;
    String customFirebaseDatabaseUrl ="https://carloan-e0bbd-default-rtdb.europe-west1.firebasedatabase.app";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout containerLayout = view.findViewById(R.id.containerLayout);
        usernameLabel = view.findViewById(R.id.username);
        fullNameLabel = view.findViewById(R.id.fullName);
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // Get the reference to the users node in the database
            DatabaseReference usersRef = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl).getReference().child("users");
            DatabaseReference rentedCarsRef = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl).getReference().child("rentals");
            // Get the current user's ID
            String userId = currentUser.getUid();

            // Construct the reference to the current user's data in the database
            DatabaseReference currentUserRef = usersRef.child(userId);
            DatabaseReference userRentedCarsRef = rentedCarsRef.child(userId);

            // Attach a ValueEventListener to get the user data
            currentUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data from the dataSnapshot
                        User user = dataSnapshot.getValue(User.class);

                        // Update the TextViews with user data
                        if (user != null) {
                            usernameLabel.setText(user.getUsername());
                            fullNameLabel.setText(user.getName());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
            // Attach a ValueEventListener to get the rented cars data
            rentedCarsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    containerLayout.removeAllViews(); // Clear existing views

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot rentalSnapshot : dataSnapshot.getChildren()) {
                            String userId = rentalSnapshot.child("userId").getValue(String.class);

                            // Check if the user ID matches the current user's ID
                            if (userId.equals(currentUser.getUid())) {
                                String carNumber = rentalSnapshot.child("carNumber").getValue(String.class);

                                // Fetch car details from the cars reference
                                DatabaseReference carDetailsRef = FirebaseDatabase.getInstance(customFirebaseDatabaseUrl)
                                        .getReference().child("cars").child(carNumber);

                                carDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot carDetailsSnapshot) {
                                        if (carDetailsSnapshot.exists()) {
                                            // Get car details
                                            CarData rentedCar = carDetailsSnapshot.getValue(CarData.class);

                                            // Create and add card view
                                            LinearLayout cardView = createCardView(rentedCar);
                                            containerLayout.addView(cardView);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Handle errors here
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });

        }


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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, marginVertical, 0, marginVertical);
        linearLayout.setLayoutParams(layoutParams);

        // Find views inside the layout
        ImageView imageView = new ImageView(getContext());
        TextView modelTextView = new TextView(getContext());
        TextView fuelTextView = new TextView(getContext());
        TextView priceTextView = new TextView(getContext());
        TextView numberTextView = new TextView(getContext());


        // Set up your views with car data
        // imageView.setImageResource(car.getCarImageResource()); // Replace with your logic
        modelTextView.setText("Model: " + car.getModel());
        fuelTextView.setText("Fuel: " + car.getFuel());
        priceTextView.setText("Price: " + car.getPrice());
        numberTextView.setText("Number: " + car.getNumber());

        int textSize = 20; // Change this value as needed
        modelTextView.setTextSize(textSize);
        fuelTextView.setTextSize(textSize);
        priceTextView.setTextSize(textSize);
        numberTextView.setTextSize(textSize);



        // Add views to the layout
        linearLayout.addView(imageView);
        linearLayout.addView(modelTextView);
        linearLayout.addView(fuelTextView);
        linearLayout.addView(priceTextView);
        linearLayout.addView(numberTextView);


        return linearLayout;
    }

}
