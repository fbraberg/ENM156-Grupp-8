package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/* Displays app logo */
public class IntroSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_splash);

        // Wait 3 seconds, then move to About page
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(IntroSplashActivity.this, IntroAboutActivity.class);
            startActivity(intent);
        }, 3000);
    }


}