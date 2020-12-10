package com.example.reviewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView autoCompleteTextView;
    private ConstraintLayout card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        card = findViewById(R.id.card);

        // TODO: Replace with bus stop data
        ArrayList<String> items = new ArrayList<>();
        items.add("Test1");
        items.add("Test12");
        items.add("Test123");
        items.add("Another Test");
        items.add("LastTest");

        // NOTE: If the content of items is updated, call adapter.notifyDataSetChanged();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        autoCompleteTextView.setAdapter(adapter);

        // When a search result is selected
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Set card values
                String stopName = parent.getItemAtPosition(position).toString();
                TextView stop = findViewById(R.id.card_stop_name);
                stop.setText(stopName);
                clearFocus();

                // TODO: Select marker based on bus stop data
                LatLng gothenburg = new LatLng(57.7, 11.966667);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gothenburg,12));

                // TODO: Use bus stop data
                openCard(stopName,
                        (float)3.5,
                         R.color.status_ok,
                         R.string.status_ok,
                         getResources().getString(R.string.status_description_ok),
                         R.drawable.test_bus_stop);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO: use values from data structure
                openCard(marker.getTitle(),
                        (float)3.5,
                         R.color.status_ok,
                         R.string.status_ok,
                         getResources().getString(R.string.status_description_ok),
                         R.drawable.test_bus_stop);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clearFocus();
            }
        });

        // Add a marker in Gothenburg and move the camera
        LatLng gothenburg = new LatLng(57.7, 11.966667);
        mMap.addMarker(new MarkerOptions().position(gothenburg).title("TestStop"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gothenburg,12));
    }

    public void clearFocus() {
        // Hide keyboard and remove focus from search bar
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
        autoCompleteTextView.clearFocus();
        card.setVisibility(View.INVISIBLE);
    }

    public void leaveFeedback(View v) {
        // Get bus stop
        TextView mTextView = findViewById(R.id.card_stop_name);
        String stop = mTextView.getText().toString();

        Intent intent = new Intent(MapsActivity.this, ReviewPageActivity.class);
        intent.putExtra("stop", stop);
        startActivity(intent);
    }

    /**
     * @param stopName          = marker.getTitle()
     * @param rating            = float
     * @param statusColorId     = R.color.status_ok OR R.color.status_problem
     * @param indicatorSymbol   = R.string.status_ok OR R.string.status_ok
     * @param statusDescription = R.string.status_description_ok OR String
     * @param drawableId        = R.drawable.{file_name}
     */
    public void openCard(String stopName, float rating, int statusColorId, int indicatorSymbol, String statusDescription, int drawableId) {
        int statusColor = getResources().getColor(statusColorId);
        TextView stop = findViewById(R.id.card_stop_name); // Displays the name of the bus stop
        stop.setText(stopName);
        RatingBar ratingBar = findViewById(R.id.card_rating_bar);
        ratingBar.setRating(rating);
        TextView indicator = findViewById(R.id.card_status_indicator); // @string/status_ok or @string/status_problem
        indicator.setText(indicatorSymbol);
        indicator.setTextColor(statusColor);
        TextView description = findViewById(R.id.card_status_description); // e.g are there broken windows?
        description.setText(statusDescription);
        description.setTextColor(statusColor);
        ImageView image = findViewById(R.id.card_image); // Image of bus stop
        image.setImageDrawable(getResources().getDrawable(drawableId));

        card.setVisibility(View.VISIBLE);
    }

}