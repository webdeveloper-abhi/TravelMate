package com.abhicoder.journeymate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.abhicoder.journeymate.DatabaseConstant.Constants;
import com.abhicoder.journeymate.Fragments.AboutUsFragment;
import com.abhicoder.journeymate.Fragments.ContactandSupportFragment;
import com.abhicoder.journeymate.Fragments.EditProfileFragment;
import com.abhicoder.journeymate.Fragments.FeedbackFragment;
import com.abhicoder.journeymate.Fragments.Home;
import com.abhicoder.journeymate.Fragments.MapFragment;
import com.abhicoder.journeymate.Fragments.PrivacyPolicyFragment;
import com.abhicoder.journeymate.Fragments.ViewProfileFragment;
import com.abhicoder.journeymate.PreferenceManager.PreferenceManager;
import com.abhicoder.journeymate.R;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.RatingBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abhicoder.journeymate.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;


import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private float Rating=4;

    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);

        setSupportActionBar(binding.toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.openDrawer,
                R.string.closeDrawer
        );
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setuserdetails();

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_logout) {
                    signOut();
                }else if (id == R.id.menu_view_profile) {
                    loadFragment(new ViewProfileFragment(), 0);
                } else if (id == R.id.menu_edit_profile) {
                    loadFragment(new EditProfileFragment(), 0);
                } else if (id == R.id.menu_help_support) {
                    loadFragment(new ContactandSupportFragment(), 0);
                } else if (id == R.id.menu_rate_app) {
                    showratingdialog();
                } else if (id == R.id.menu_send_feedback) {
                    loadFragment(new FeedbackFragment(), 0);
                }else if(id==R.id.menu_privacy_security){
                    loadFragment(new PrivacyPolicyFragment(),0);
                }else {
                    loadFragment(new MapFragment(), 1);
                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        loadFragment(new MapFragment(), 1);
    }

    private void signOut() {
        showToast("Signed Out....");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_UserId));

        HashMap<String, Object> updates = new HashMap<>();
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        preferenceManager.clear();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Unable to SignOut...");
                    }
                });
    }

    private void setuserdetails() {
        View headerView = binding.navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.navigation_header_name);
        TextView email = headerView.findViewById(R.id.navigation_header_email);
        ImageView profileimage = headerView.findViewById(R.id.profileimage);

        name.setText(preferenceManager.getString(Constants.KEY_Name));
        email.setText(preferenceManager.getString(Constants.KEY_Email));

        String bitmapString = preferenceManager.getString(Constants.KEY_Image);

        Bitmap bitmap = StringToBitMap(bitmapString);

        if (bitmap != null) {
            profileimage.setImageBitmap(bitmap);
        } else {
            showToast("No Profile image Available");
        }
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadFragment(Fragment fragment, int flag) {
        if (flag == 1) {
            binding.searchView.setVisibility(View.VISIBLE);
        } else {
            binding.searchView.setVisibility(View.INVISIBLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showratingdialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.rateappdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView image = dialog.findViewById(R.id.imageview);
        Button ratelater = dialog.findViewById(R.id.ratelater);
        Button ratenow = dialog.findViewById(R.id.ratenow);
        RatingBar ratingBar = dialog.findViewById(R.id.ratebar);

        ratelater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1.0) {
                    image.setImageResource(R.drawable.onestarimages);
                } else if (rating > 1.0 && rating <= 2.0) {
                    image.setImageResource(R.drawable.twostaremoji);
                } else if (rating > 2.0 && rating <= 3.0) {
                    image.setImageResource(R.drawable.threestaremoji);
                } else if (rating > 3.0 && rating <= 4.0) {
                    image.setImageResource(R.drawable.fourstaremoji);
                } else {
                    image.setImageResource(R.drawable.fivestaremoji);
                }
                Rating = rating;
            }
        });

        ratenow.setOnClickListener(v -> {
            showToast("Thank You For Rating!");
            addRatingToDatabase();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addRatingToDatabase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = database.collection(Constants.KEY_COLLECTION_RATING);

        collectionReference.add(getRatingData())
                .addOnFailureListener(e -> {
                    showToast("Failed to add rating.");
                    e.printStackTrace();
                });
    }

    private Map<String, Object> getRatingData() {
        Map<String, Object> ratingData = new HashMap<>();

        ratingData.put("userId", preferenceManager.getString(Constants.KEY_UserId));
        ratingData.put("rating", Rating);

        return ratingData;
    }

}
