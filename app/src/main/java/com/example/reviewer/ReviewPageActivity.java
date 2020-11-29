package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ReviewPageActivity extends AppCompatActivity {

    private RadioGroup rgKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("stop"));
        setContentView(R.layout.activity_reviewpage);

        rgKategori = findViewById(R.id.rgkategori);
        TextView textBild = findViewById(R.id.textBild);
        TextView textObl = findViewById(R.id.textObl);
        Button buttonBild = findViewById(R.id.buttonBild);


        rgKategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonSkadeanmalan) {
                    textBild.setVisibility(View.VISIBLE);
                    textObl.setVisibility(View.VISIBLE);
                    buttonBild.setVisibility(View.VISIBLE);
                } else {
                    buttonBild.setVisibility(View.INVISIBLE);
                    textObl.setVisibility(View.INVISIBLE);
                    textBild.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // NEEDS TO BE TESTED
    public void onBildClick(View view) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        startActivity(intent);
    }

    public void onSendClick(View view) {
        // TODO Check the if reviewer has filled in the form correctly (added image & commend etc..)

        // TODO Store all the data that has been filled in
    }
}
