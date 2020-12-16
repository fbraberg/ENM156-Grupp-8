package com.example.reviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView autoCompleteTextView;
    private ConstraintLayout card;
    ArrayList<LatLng> rawStopCoords = new ArrayList<>();
    ArrayList<String> stopNames = new ArrayList<>();
    HashMap<String,LatLng> stopCoords = new HashMap<>();
    Marker previousMarker;
    float MARKER_HUE_ACTIVE;
    float MARKER_HUE_INACTIVE;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Prepare marker colors
        float[] hsv = new float[3];
        Color.colorToHSV(getResources().getColor(R.color.marker_active), hsv);
        MARKER_HUE_ACTIVE = hsv[0];
        Color.colorToHSV(getResources().getColor(R.color.västtrafik_blue), hsv);
        MARKER_HUE_INACTIVE = hsv[0];

        // Prepare hard-coded bus stops
        busStations();

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        card = findViewById(R.id.card);

        // NOTE: If the content of stopNames is updated, call adapter.notifyDataSetChanged();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stopNames);
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
                LatLng location = stopCoords.get(stopName);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));

                // TODO: Use bus stop data
                openCard(stopName,
                        (float) 3.5,
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
        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

        // Process hard-coded bus stop coordinates
        for (int i = 0; i < rawStopCoords.size(); i++) {
            LatLng location = rawStopCoords.get(i);
            String locationName = geoLocateToName(location.latitude, location.longitude);
            MarkerOptions options = new MarkerOptions().position(location).title(locationName);
            options.icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE_INACTIVE));
            mMap.addMarker(options);
            stopNames.add(locationName);
            stopCoords.put(locationName, location);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearFocus();
                previousMarker = marker;
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE_ACTIVE));
                // TODO: use values from data structure
                String stopName = marker.getTitle();
                LatLng location = stopCoords.get(stopName);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                openCard(stopName,
                        (float) 3.5,
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


        // Move camera to current location if permitted to, else default to Gothenburg
        LatLng coords = new LatLng(57.7, 11.966667);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                coords = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords,12));

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current location", Toast.LENGTH_SHORT).show();
        clearFocus();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        }
        else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show();
    }

    // Hard-coded coordinates
    public void busStations(){
        LatLng centralstation= new LatLng(57.70898611081937, 11.972313501612282);
        // buss 18
        LatLng korkarlens = new LatLng(57.75776700181472, 11.987768767718672);
        LatLng akkasGata = new LatLng(57.75423547557376, 11.980694006368749);
        LatLng selma = new LatLng(57.750959116253675, 11.98155493171513);
        LatLng sagensgatan = new LatLng(57.7459602856516, 11.977436318602667);
        LatLng kyrkogata = new LatLng(57.74315511118166, 11.974759748397986);
        LatLng bjorkRis = new LatLng(57.739016423628, 11.973734264982832);
        LatLng balladgatan = new LatLng(57.73162046108551, 11.975846258632135);
        LatLng brunnsBotorget = new LatLng(57.727478532206945, 11.9705199106782);
        LatLng brantinspl = new LatLng(57.7208366861881, 11.953674485475872);
        LatLng lillaBommen = new LatLng(57.711821776968414, 11.966031658214296);
        LatLng brunnsparken= new LatLng(57.707066682264276, 11.96718118326053);
        LatLng kungsPortsPlatsen= new LatLng(57.70417128936519, 11.969736269767381);
        LatLng valand= new LatLng(57.700342591223695, 11.974516512775526);
        LatLng vidblicksgatan= new LatLng(57.692388867573214, 11.979910269782106);
        LatLng spaldingsGatan= new LatLng(57.689649551935865, 11.983324712366983);
        LatLng bergsPrangare= new LatLng(57.68640478574476, 11.985556157668622);
        LatLng pilBagsGatan= new LatLng(57.684513046057695, 11.984646840085333);
        rawStopCoords.add(spaldingsGatan);
        rawStopCoords.add(vidblicksgatan);
        rawStopCoords.add(valand);
        rawStopCoords.add(kungsPortsPlatsen);
        rawStopCoords.add(brunnsparken);
        rawStopCoords.add(centralstation);
        rawStopCoords.add(korkarlens);
        rawStopCoords.add(akkasGata);
        rawStopCoords.add(selma);
        rawStopCoords.add(sagensgatan);
        rawStopCoords.add(kyrkogata);
        rawStopCoords.add(bjorkRis);
        rawStopCoords.add(balladgatan);
        rawStopCoords.add(brunnsBotorget);
        rawStopCoords.add(brantinspl);
        rawStopCoords.add(lillaBommen);
        rawStopCoords.add(bergsPrangare);
        rawStopCoords.add(pilBagsGatan);
    }

    public void clearFocus() {
        // Hide keyboard and remove focus from search bar
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
        autoCompleteTextView.clearFocus();
        card.setVisibility(View.INVISIBLE);

        // Mark current marker as inactive (previous if called via onMarkerClick)
        if (previousMarker != null) {
            previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE_INACTIVE));
        }

    }

    private String geoLocateToName(double la, double lo) {
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocation(la, lo, 1);
        } catch (IOException e) {
        }
        if (list.size() > 0) {
            Address address = list.get(0);

            return address.getFeatureName();
        }
        return null;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //we can show user a dialog of why this permisson is necessary
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
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
     * @param indicatorSymbol   = R.string.status_ok OR R.string.status_problem
     * @param statusDescription = R.string.status_description_ok OR String
     * @param drawableId        = R.drawable.{file_name}
     */
    public void openCard(String stopName, float rating, int statusColorId, int indicatorSymbol, String statusDescription, int drawableId) {
        int statusColor = getResources().getColor(statusColorId);
        TextView stop = findViewById(R.id.card_stop_name); // Displays the name of the bus stop
        RatingBar ratingBar = findViewById(R.id.card_rating_bar);
        TextView indicator = findViewById(R.id.card_status_indicator); // @string/status_ok or @string/status_problem
        TextView description = findViewById(R.id.card_status_description); // e.g are there broken windows?
        ImageView image = findViewById(R.id.card_image); // Displays image of bus stop

        stop.setText(stopName);
        ratingBar.setRating(rating);
        indicator.setText(indicatorSymbol);
        indicator.setTextColor(statusColor);
        description.setText(statusDescription);
        description.setTextColor(statusColor);

        image.setImageDrawable(getResources().getDrawable(drawableId));

        card.setVisibility(View.VISIBLE);
    }
}