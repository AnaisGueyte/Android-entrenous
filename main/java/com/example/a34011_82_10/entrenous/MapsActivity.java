package com.example.a34011_82_10.entrenous;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class will show the map and coffee places requested
 *
 * @author: Anais Gueyte
 * @date:  22/12/2016
 *
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

   /* public static MapsActivity newInstance() {
        MapsActivity fragment = new MapsActivity();
        return fragment;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Entre-nous: des bars et des cafés !");
        setContentView(R.layout.activity_maps);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return inflater.inflate(R.layout.activity_maps, null, false);
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

        Log.i("info", "onMapReady()");

        // Add a marker in Sydney and move the camera
        LatLng paris = new LatLng(48.866667,  2.333333);

        googleMap.addMarker(new MarkerOptions().position(paris).title("Ici, c'est Paris !"));
        Log.i("info", "dans la methode apres add marker");

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
        Log.i("info", "dans la methode apres move camera");
    }


}
