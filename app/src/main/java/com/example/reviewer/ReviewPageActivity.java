package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

public class ReviewPageActivity extends AppCompatActivity {

    private RadioGroup rgKategori;
    private RadioButton skadeAnmalan;
    private RatingBar ratingBar;

    private boolean imageTaken = false;
    private File output=null;
    private static final int CONTENT_REQUEST=1337;

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

        ratingBar = findViewById(R.id.ratingBar);
        skadeAnmalan = findViewById(R.id.radioButtonSkadeanmalan);

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
        imageTaken = true;
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {

        }
        //onActivityResult(1, RESULT_OK, intent);
        Bundle extras = intent.getExtras();
        //Bitmap imageBitmap = (Bitmap) extras.get("intent");
        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");
        File dir = Environment.getExternalStoragePublicDirectory("..\\res\\stations\\images");
        output=new File(dir, "CameraContentDemo.jpeg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, CONTENT_REQUEST);
    }

    public void onSendClick(View view) {
        // TODO Check the if reviewer has filled in the form correctly (added image & commend etc..)
        if(ratingBar.getRating() < 0.5f) {
            Log.v("ID:", "" + rgKategori.getCheckedRadioButtonId());
            if(rgKategori.getCheckedRadioButtonId() == skadeAnmalan.getId())
                if(imageTaken)
                    Log.v("STARS", "MR yes");
        }

        // TODO Store all the data that has been filled in
        imageTaken = false;
    }
}
