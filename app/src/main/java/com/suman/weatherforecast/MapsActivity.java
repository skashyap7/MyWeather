package com.suman.weatherforecast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Long longitude;
    private Long latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        longitude = Long.valueOf(intent.getStringExtra("Longitude"));
        latitude = Long.valueOf(intent.getStringExtra("Latitude"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction ftransaction = fmanager.beginTransaction();
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("latitude",latitude.toString());
        bundle.putString("longitude",longitude.toString());
        fragment.setArguments(bundle);
        ftransaction.add(R.id.fragment_container, fragment);
        ftransaction.commit();
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
        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng mylocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(mylocation).title("Current Weather Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));*/
    }
}
