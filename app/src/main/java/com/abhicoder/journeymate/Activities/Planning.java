package com.abhicoder.journeymate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.abhicoder.journeymate.R;

import java.util.ArrayList;
import java.util.Map;

public class Planning extends AppCompatActivity {

    private String startDate, endDate, location;
    private FirebaseFirestore db;
    private ArrayList<PlaceOrHotel> hotelList;
    private ArrayList<PlaceOrHotel> placeList;

    private View loadingScreen;
    private View contentLayout;
    private LinearLayout horizontalSliderHotels;
    private LinearLayout horizontalSliderPlaces;
    private TextView headingLocation, startEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_budget);
        FirebaseApp.initializeApp(this);

        // Initialize views
        loadingScreen = findViewById(R.id.loading_screen);
        contentLayout = findViewById(R.id.content_layout);
        horizontalSliderHotels = findViewById(R.id.horizontal_slider);
        horizontalSliderPlaces = findViewById(R.id.places_slider);
        headingLocation = findViewById(R.id.heading_location);
        startEndDate = findViewById(R.id.start_end_date);

        // Retrieve data from the intent
        Intent intent = getIntent();
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        location = intent.getStringExtra("location");

        // Set location, start date, and end date in TextViews
        headingLocation.setText(location);
        startEndDate.setText("Start Date: " + startDate + " - End Date: " + endDate);

        db = FirebaseFirestore.getInstance();
        hotelList = new ArrayList<>();
        placeList = new ArrayList<>();

        searchLocationInDatabase(location);
    }

    private void searchLocationInDatabase(String location) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String locationLowerCase = location.trim().toLowerCase();

        database.collection("cities")
                .document(locationLowerCase)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            ArrayList<Map<String, Object>> hotels = (ArrayList<Map<String, Object>>) documentSnapshot.get("hotels");
                            ArrayList<Map<String, Object>> places = (ArrayList<Map<String, Object>>) documentSnapshot.get("places");

                            // Populate hotel list
                            if (hotels != null && !hotels.isEmpty()) {
                                for (Map<String, Object> hotelData : hotels) {
                                    String hotelName = (String) hotelData.get("name");
                                    Double hotelRating = parseRating(hotelData.get("rating"));
                                    hotelList.add(new PlaceOrHotel(hotelName, hotelRating, "Hotel"));
                                }
                            }

                            // Populate place list
                            if (places != null && !places.isEmpty()) {
                                for (Map<String, Object> placeData : places) {
                                    String placeName = (String) placeData.get("name");
                                    Double placeRating = parseRating(placeData.get("rating"));
                                    placeList.add(new PlaceOrHotel(placeName, placeRating, "Place"));
                                }
                            }

                            hotelList.sort((item1, item2) -> Double.compare(item2.getRating(), item1.getRating()));
                            placeList.sort((item1, item2) -> Double.compare(item2.getRating(), item1.getRating()));

                            // Update UI once data is loaded
                            updateUI();
                        } else {
                            showToast("City not found in the database");
                        }
                    }
                });
    }

    private Double parseRating(Object ratingObj) {
        if (ratingObj instanceof String) {
            try {
                return Double.parseDouble((String) ratingObj);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private void updateUI() {
        loadingScreen.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);

        populateHorizontalSlider(horizontalSliderHotels, hotelList, new int[]{R.drawable.hotel1, R.drawable.hotel2, R.drawable.hotel3});
        populateHorizontalSlider(horizontalSliderPlaces, placeList, new int[]{R.drawable.place1, R.drawable.place2, R.drawable.place3});
    }

    private void populateHorizontalSlider(LinearLayout slider, ArrayList<PlaceOrHotel> list, int[] images) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < list.size(); i++) {
            PlaceOrHotel item = list.get(i);

            View itemView = inflater.inflate(R.layout.item_hotel, slider, false);
            ImageView imageView = itemView.findViewById(R.id.hotel_image);
            TextView nameText = itemView.findViewById(R.id.hotel_name);

            imageView.setImageResource(images[i % images.length]);
            nameText.setText(item.getName());

            slider.addView(itemView);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public class PlaceOrHotel {
        private String name;
        private Double rating;
        private String type;

        public PlaceOrHotel(String name, Double rating, String type) {
            this.name = name;
            this.rating = rating;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Double getRating() {
            return rating;
        }

        public String getType() {
            return type;
        }
    }
}
