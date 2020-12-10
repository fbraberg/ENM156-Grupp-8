package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.logging.Logger;

/* Displays information about the app. */
public class IntroAboutActivity extends AppCompatActivity {
    private static final String tag = "Testing: ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_about);

    }

    public void onButtonClick(View v) {
        // Open map view
        Intent intent = new Intent(IntroAboutActivity.this, MapsActivity.class);
        startActivity(intent);

        try {
            File file = new File(this.getFilesDir(),io.io.FILE);
            if(!file.exists())
                io.io.initBackend(this);

            JSONObject station = io.io.readStation("Kungsportsplatsen", this);
            //io.io.submitForm("Kungsportsplatsen", "opinion", "Trash", 1, "---", this);
            float f = io.io.getMeanRating(station);
            Log.v("STATIONERINO", "lol" + station.toString());
            Log.v("STATIONERINO", "lol" + f);
        } catch (Exception e) {
            Log.v("MEMEERROR_2", e.getMessage());
        }

        finish();

    }
}