package com.abhicoder.journeymate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import com.abhicoder.journeymate.R;

import java.util.Calendar;

public class Traveldate extends AppCompatActivity {

    // UI elements
    private CalendarView calendarView;
    private AppCompatButton continueButton;
    private SearchView searchView;

    // Variables to store selected start and end dates
    private long startDate;
    private long endDate;

    // Variable to hold the selected travel option from the previous activity
    private String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveldate);

        // Initialize the UI elements
        calendarView = findViewById(R.id.calendarView);
        continueButton = findViewById(R.id.continueButton);
        searchView = findViewById(R.id.searchView);

        // Get the selected option from the previous activity
        Intent intent = getIntent();
        selectedOption = intent.getStringExtra("selectedOption");

        // Set the initial date as the start date
        calendarView.setDate(System.currentTimeMillis(), false, true);

        // Set an onDateChangeListener for the calendar
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // If startDate is not selected, set it as the start date
            if (startDate == 0) {
                startDate = getDateInMillis(year, month, dayOfMonth);
                Toast.makeText(Traveldate.this, "Selected Start Date: " + formatDate(startDate), Toast.LENGTH_SHORT).show();
            }
            // If startDate is selected, set the endDate as the selected date
            else {
                endDate = getDateInMillis(year, month, dayOfMonth);
                Toast.makeText(Traveldate.this, "Selected End Date: " + formatDate(endDate), Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the "Continue" button
        continueButton.setOnClickListener(v -> {
            // Check if location is empty or dates are not selected
            String location = searchView.getQuery().toString();
            if (location.isEmpty()) {
                Toast.makeText(Traveldate.this, "Please enter a location", Toast.LENGTH_SHORT).show();
            } else if (startDate == 0 || endDate == 0) {
                Toast.makeText(Traveldate.this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
            } else {
                // Format the start and end dates as strings
                String formattedStartDate = formatDate(startDate);
                String formattedEndDate = formatDate(endDate);

                // Pass the selected travel option, dates, and location to the next activity
                Intent budgetIntent = new Intent(Traveldate.this, Budgets.class);
                budgetIntent.putExtra("selectedOption", selectedOption);
                budgetIntent.putExtra("startDate", formattedStartDate);
                budgetIntent.putExtra("endDate", formattedEndDate);
                budgetIntent.putExtra("location", location);

                // Start the Budget activity
                startActivity(budgetIntent);
            }
        });
    }

    // Method to convert date to milliseconds
    private long getDateInMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    // Method to format date as a String
    private String formatDate(long dateMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillis);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Month is 0-based
        int year = calendar.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }
}
