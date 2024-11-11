package com.abhicoder.journeymate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.abhicoder.journeymate.R;

public class Budgets extends AppCompatActivity {

    private LinearLayout buttonCheap, buttonModerate, buttonLuxury;
    private boolean isOptionSelected = false;
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgets);

        // Get the UI elements
        buttonCheap = findViewById(R.id.button_cheap);
        buttonModerate = findViewById(R.id.button_moderate);
        buttonLuxury = findViewById(R.id.button_luxury);

        // Get intent data from the previous activity
        Intent intent = getIntent();
        String startDate = intent.getStringExtra("startDate");
        String location=intent.getStringExtra("location");
        String endDate = intent.getStringExtra("endDate");
        String selectedOptionFromPrevious = intent.getStringExtra("selectedOption");



        // Handle button clicks
        buttonCheap.setOnClickListener(v -> {
            // Change background color for selection
            buttonCheap.setBackgroundColor(getResources().getColor(R.color.selected_option));
            buttonModerate.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            buttonLuxury.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            selectedOption = "Cheap";
            isOptionSelected = true;
        });

        buttonModerate.setOnClickListener(v -> {
            // Change background color for selection
            buttonModerate.setBackgroundColor(getResources().getColor(R.color.selected_option));
            buttonCheap.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            buttonLuxury.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            selectedOption = "Moderate";
            isOptionSelected = true;
        });

        buttonLuxury.setOnClickListener(v -> {
            // Change background color for selection
            buttonLuxury.setBackgroundColor(getResources().getColor(R.color.selected_option));
            buttonCheap.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            buttonModerate.setBackgroundColor(getResources().getColor(R.color.unselected_option));
            selectedOption = "Luxury";
            isOptionSelected = true;
        });

        // Continue button logic
        findViewById(R.id.continueButton).setOnClickListener(v -> {
            if (!isOptionSelected) {
                // Show error toast if no option is selected
                Toast.makeText(Budgets.this, "Please select a spending habit", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to next activity with selected data
                Intent reviewIntent = new Intent(Budgets.this, Review.class);

                // Pass the selected option, start and end dates from the previous activity
                reviewIntent.putExtra("startDate", startDate);
                reviewIntent.putExtra("endDate", endDate);
                reviewIntent.putExtra("selectedOption", selectedOption);
                reviewIntent.putExtra("location", location);
                reviewIntent.putExtra("selectedOptionFromPrevious",selectedOptionFromPrevious);

                // Start the Review activity
                startActivity(reviewIntent);
            }
        });
    }
}
