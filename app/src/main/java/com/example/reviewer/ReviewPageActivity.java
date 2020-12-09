package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewPageActivity extends AppCompatActivity {

    private RadioGroup rgKategori;
    private RadioButton skadeAnmalan;
    private RatingBar ratingBar;
    ImageView imageViewPhoto;
    private boolean imageTaken = false;
    String currentPhotoPath;

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
        imageViewPhoto = findViewById(R.id.imageViewPhoto);

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

    public void onBildClick(View view) {
        imageTaken = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageViewPhoto.setImageBitmap(bitmap);

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File photoFile = createImageFile();
            Log.v("FILENAME: ", "" + photoFile);
            FileOutputStream out = new FileOutputStream(photoFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            // PNG is a lossless format, the compression factor (100) is ignored
         } catch (IOException e) {
            // Error occurred while creating the File
            e.printStackTrace();
        }
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}