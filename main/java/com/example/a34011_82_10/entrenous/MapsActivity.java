package com.example.a34011_82_10.entrenous;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will show the map and coffee places requested
 *
 * @author: Anais Gueyte
 * @date:  22/12/2016
 * @date: 05/01/2016
 *
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    private Intent i;
    private Bundle b;

    private LatLng latlong;
    private LatLng latlong2;
    private LatLng middlePoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Entre-nous: des bars et des cafés !");

        setContentView(R.layout.activity_maps);


        /*** CODE TO GET THE INTENT ***/
        i = getIntent();
        b = i.getExtras();

        latlong = b.getParcelable("address1");
        latlong2 = b.getParcelable("address2");
        middlePoint = b.getParcelable("middlepoint");


        /*** CODE TO INITIALIZE THE MAP ***/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

      Log.i("info", "DANS METHODE ON MAP READY CALL BACK");
      mMap = googleMap;

      Log.i("info", "middlepoint dans on map ready: " + middlePoint);

      CameraUpdate update = CameraUpdateFactory.newLatLngZoom(middlePoint, 12);
      mMap.moveCamera(update);

      if(marker != null){
          marker.remove();}

      MarkerOptions markerOptions = new MarkerOptions()
              .title("Point 1")
              .position(latlong);

      MarkerOptions markerOptions2 = new MarkerOptions()
              .title("Point 2")
              .position(latlong2);

      MarkerOptions middleOptions = new MarkerOptions()
              .title("Mi-chemin")
              .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location))
              .position(middlePoint);

      marker = mMap.addMarker(markerOptions);
      marker = mMap.addMarker(markerOptions2);
      marker = mMap.addMarker(middleOptions);

      Log.i("info", "APRES ADD MARKER");

  }

}
