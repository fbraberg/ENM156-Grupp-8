package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


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
            io.io.initBackend(this);
            Log.v("JSONOBJECT",io.io.readJSON(this).get(0).toString());
        } catch (Exception e) {
            Log.v("MEMEERROR", e.getMessage());
        }



    }
}