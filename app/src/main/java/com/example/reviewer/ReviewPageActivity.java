package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewPageActivity extends AppCompatActivity {

    private RadioGroup rgKategori;
    private RadioButton skadeAnmalan;
    private RatingBar ratingBar;
    private EditText kommentar;
    ImageView imageViewPhoto;

    private boolean imageTaken = false;
    String currentPhotoPath;
    String comment;
    File photoFile;
    String busStop;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO REMOVE!!!!
        File file = new File(this.getFilesDir(),io.io.FILE);
        if(!file.exists())
            io.io.initBackend(this);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setTitle(intent.getStringExtra("stop"));
        busStop = intent.getStringExtra("stop");
        setContentView(R.layout.activity_reviewpage);

        rgKategori = findViewById(R.id.rgkategori);
        TextView textBild = findViewById(R.id.textBild);
        TextView textObl = findViewById(R.id.textObl);
        Button buttonBild = findViewById(R.id.buttonBild);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        kommentar = findViewById(R.id.kommentar);

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
        Log.v("BILDCLICK", "yes");
        // Images are stored on the device under /storage/emulated/0/Android/data/com.example.reviewer/files/Pictures
        imageTaken = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageViewPhoto.setImageBitmap(bitmap);
        try {
            photoFile = createImageFile();
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

        // Check if the reviewer has given a star rating and taken an image
        if (ratingBar.getRating() < 0.5f) {
            Toast.makeText(ReviewPageActivity.this, "Betyg måste ges!", Toast.LENGTH_SHORT).show();
        } else if(((rgKategori.getCheckedRadioButtonId() != skadeAnmalan.getId())) ||
                ((rgKategori.getCheckedRadioButtonId() == skadeAnmalan.getId()) && imageTaken &&
                        (ratingBar.getRating() > 0.5f))) {
            Toast.makeText(ReviewPageActivity.this, "Tack för din anmälan!", Toast.LENGTH_LONG).show();

            // Store comment
            comment = kommentar.getText().toString();
            // Store rating
            rating = ratingBar.getRating();
            // Photo file path can be found in photoFile

            Log.v("YOYO", busStop);

            if(imageTaken)
                io.io.submitForm(busStop, readCategory(), comment, ratingBar.getRating(), photoFile.getPath(), this);
            else
                io.io.submitForm(busStop, readCategory(), comment, ratingBar.getRating(), "", this);

            imageTaken = false;
            // Send the user back to the map view
            Intent intent = new Intent(ReviewPageActivity.this, MapsActivity.class);
            startActivity(intent);
        }
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

    private String readCategory () {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgkategori);

        int radioButtonID = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);

        return (String) radioButton.getText();
    }

}