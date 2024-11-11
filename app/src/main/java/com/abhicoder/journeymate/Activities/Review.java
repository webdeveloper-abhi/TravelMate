package com.abhicoder.journeymate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.abhicoder.journeymate.R;
import com.abhicoder.journeymate.databinding.ActivityLoginBinding;
import com.abhicoder.journeymate.databinding.ActivityReviewBinding;

public class Review extends AppCompatActivity {

    private String startDate, endDate, location, selectedOption, selectedOptionFromPrevious;

    private ActivityReviewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve data from the intent
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        location = getIntent().getStringExtra("location");
        selectedOption = getIntent().getStringExtra("selectedOption");
        selectedOptionFromPrevious = getIntent().getStringExtra("selectedOptionFromPrevious");

        binding.destination.setText(location);
        binding.calendar.setText(startDate +" - "+ endDate);
        binding.travel.setText(selectedOptionFromPrevious);
        binding.mybudget.setText(selectedOption);

        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(Review.this, Planning.class);

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
