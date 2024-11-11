package com.abhicoder.journeymate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;

import com.abhicoder.journeymate.R;

public class Travelling extends AppCompatActivity {

    // Declare layout variables for each option
    private LinearLayout buttonJustMe, buttonCouple, buttonFamily, buttonFriends;
    private AppCompatButton continueButton;

    // Variable to track the selected option
    private String selectedOption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelling);

        // Initialize the layouts
        buttonJustMe = findViewById(R.id.button_just_me);
        buttonCouple = findViewById(R.id.button_couple);
        buttonFamily = findViewById(R.id.button_family);
        buttonFriends = findViewById(R.id.button_friends);
        continueButton = findViewById(R.id.continueButton);

        // Set click listeners for each option
        buttonJustMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption("Just Me", buttonJustMe);
            }
        });

        buttonCouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption("Couple", buttonCouple);
            }
        });

        buttonFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption("Family", buttonFamily);
            }
        });

        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOption("Friends", buttonFriends);
            }
        });

        // Set click listener for continue button
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOption.isEmpty()) {
                    // If no option is selected, show a toast message
                    Toast.makeText(Travelling.this, "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    // If an option is selected, move to the next activity and pass the selected option
                    Intent intent = new Intent(Travelling.this, Traveldate.class);
                    intent.putExtra("selectedOption", selectedOption);
                    startActivity(intent);
                }
            }
        });
    }

    // Method to handle selection of an option
    private void selectOption(String option, LinearLayout selectedLayout) {
        // Reset background of all layouts
        resetLayoutBackground();

        // Set the background of the selected layout to indicate it has been selected
        selectedLayout.setBackgroundColor(getResources().getColor(R.color.selected_option));

        // Store the selected option
        selectedOption = option;
    }

    // Method to reset the background of all layouts
    private void resetLayoutBackground() {
        buttonJustMe.setBackgroundColor(getResources().getColor(R.color.unselected_option));
        buttonCouple.setBackgroundColor(getResources().getColor(R.color.unselected_option));
        buttonFamily.setBackgroundColor(getResources().getColor(R.color.unselected_option));
        buttonFriends.setBackgroundColor(getResources().getColor(R.color.unselected_option));
    }
}
