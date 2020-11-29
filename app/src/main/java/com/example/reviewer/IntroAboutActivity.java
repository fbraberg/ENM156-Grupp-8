package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/* Displays information about the app. */
public class IntroAboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_about);
    }

    public void onButtonClick(View v) {
        // Open map view
        Intent intent = new Intent(IntroAboutActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}