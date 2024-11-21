package com.example.carrentalapp;

public class RentalInfo {public String carNumber;
    public String userId;
    public String selectedDate1;
    public String selectedDate2;

    public RentalInfo() {
        // Default constructor required for Firebase
    }

    public RentalInfo(String carNumber, String userId, String selectedDate1, String selectedDate2) {
        this.carNumber = carNumber;
        this.userId = userId;
        this.selectedDate1 = selectedDate1;
        this.selectedDate2 = selectedDate2;
    }

}
