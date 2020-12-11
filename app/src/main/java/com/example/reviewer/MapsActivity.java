package com.example.reviewer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

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
        dialog= new Dialog(this);
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

        return null;
    }
}