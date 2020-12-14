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
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {
    LatLng gothenburg = new LatLng(57.72635680726805, 11.963851620625237);
    ArrayList<LatLng> demo= new ArrayList<>();
    Dialog dialog;
    float hue= 197;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;
    private GoogleMap map;

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

        busStations();
    }
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
        demo.add(spaldingsGatan);
        demo.add(vidblicksgatan);
        demo.add(valand);
        demo.add(kungsPortsPlatsen);
        demo.add(brunnsparken);
        demo.add(centralstation);
        demo.add(korkarlens);
        demo.add(akkasGata);
        demo.add(selma);
        demo.add(sagensgatan);
        demo.add(kyrkogata);
        demo.add(bjorkRis);
        demo.add(balladgatan);
        demo.add(brunnsBotorget);
        demo.add(brantinspl);
        demo.add(lillaBommen);
        demo.add(bergsPrangare);
        demo.add(pilBagsGatan);
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
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();
        //init();
        for(int i = 0;i<demo.size();i++){
            LatLng location= demo.get(i);
            String locationName= geoLocateToName(location.latitude,location.longitude);
            MarkerOptions options= new MarkerOptions().position(location).title(locationName);
            options.icon(BitmapDescriptorFactory.defaultMarker(hue));
            map.addMarker(options);

        }
        float zoomLevel = 11.0f; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(gothenburg,zoomLevel));
        map.setOnMarkerClickListener(this);
    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
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



    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current location", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
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
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {


        String locationName= geoLocateToName(marker.getPosition().latitude,marker.getPosition().longitude);
        openDialog(locationName);

        return false;
    }

    /**
     * Notes that row 272 to 276 should be changed to suit our final disgned layout.
     * @Aladdin
     */
    private void openDialog(String n) {
        dialog.setContentView(R.layout.dialog_test);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img = dialog.findViewById(R.id.imageView2);
        Button btn= dialog.findViewById(R.id.rate_b);
        TextView placeName= dialog.findViewById(R.id.place_name);
        placeName.setText(n);
        dialog.show();
    }
    // take lagLoT of a location and return the most possible place name as string.
    private String  geoLocateToName(double la, double lo) {
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