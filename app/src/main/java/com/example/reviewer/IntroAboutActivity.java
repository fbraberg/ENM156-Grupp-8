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
            File file = new File(this.getFilesDir(),"stations.json");
            if( !file.exists() ){
                file.createNewFile()){

                }
            }

            //JSONObject stat = io.io.readStation("Kungsportsplatsen", this);
            //Log.v("WORKSBOI", stat.getString("name"));
            //stat.put("hehexd", 1337);
            //io.io.writeStation(stat, this);
        } catch (Exception e) {
            Log.v("MEMEERROR", e.getMessage());
        }



    }
}