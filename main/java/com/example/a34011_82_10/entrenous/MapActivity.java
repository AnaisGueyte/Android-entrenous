    package com.example.a34011_82_10.entrenous;

    /**
     * Created by 34011-82-10 on 05/01/2017.
     */

            import android.Manifest;
            import android.content.Intent;
            import android.content.pm.PackageManager;
            import android.location.Address;
            import android.location.Geocoder;
            import android.location.Location;
            import android.os.Build;
            import android.support.v4.app.ActivityCompat;
            import android.support.v4.app.FragmentActivity;
            import android.os.Bundle;
            import android.support.v4.content.ContextCompat;
            import android.util.Log;
            import android.view.View;
            import android.widget.Toast;

            import com.google.android.gms.common.ConnectionResult;
            import com.google.android.gms.common.api.GoogleApiClient;
            import com.google.android.gms.location.LocationListener;
            import com.google.android.gms.location.LocationRequest;
            import com.google.android.gms.location.LocationServices;
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
            import java.util.List;

    public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    LocationListener {

                private GoogleMap mMap;
                GoogleApiClient mGoogleApiClient;
                Location mLastLocation;
                Marker mCurrLocationMarker;
                LocationRequest mLocationRequest;


        Intent i;
        Bundle b;

        String address1;
        String location;

        LatLng latlong;

                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_maps);

                    /*** CODE TO GET THE INTENT ***/
                    i = getIntent();
                    b = i.getExtras();
                    address1 = b.getString("address1");
                    Log.i("info", "address1 dans map activity on create :" + address1);


                    /*** CODE TO GET THE ADDRESS AND ADD MARKER ***/
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    List<Address> list;

                    try {

                        location = address1;
                        list = geocoder.getFromLocationName(location, 1);
                        Log.i("info", "dans TRY location: " + location);

                        Address add = list.get(0);
                        String locality = add.getLocality();

                        latlong = new LatLng(add.getLatitude(), add.getLongitude());
                        Log.i("info", "valeur de latlong dans try: " + latlong);

                    } catch (IOException e) {
                        return;
                    }

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkLocationPermission();
                    }
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(this);



                }

                public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
                public boolean checkLocationPermission(){
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Asking user if explanation is needed
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);


                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                protected void onResume() {
                    super.onResume();


                }

                @Override
                protected void onPause() {
                    super.onPause();

                }

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                    //Initialize Google Play Services
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiClient();
                            mMap.setMyLocationEnabled(true);
                        }
                    }
                    else {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
                protected synchronized void buildGoogleApiClient() {
                    mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .build();
                    mGoogleApiClient.connect();
                }

                @Override
                public void onConnected(Bundle bundle) {

                    mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(1000);
                    mLocationRequest.setFastestInterval(1000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {

                }

                @Override
                public void onLocationChanged(Location location) {

                    mLastLocation = location;
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    //stop location updates
                    if (mGoogleApiClient != null) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    }

                }

                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {

                }
                @Override
                public void onRequestPermissionsResult(int requestCode,
                                                       String permissions[], int[] grantResults) {
                    switch (requestCode) {
                        case MY_PERMISSIONS_REQUEST_LOCATION: {
                            // If request is cancelled, the result arrays are empty.
                            if (grantResults.length > 0
                                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                                // permission was granted. Do the
                                // contacts-related task you need to do.
                                if (ContextCompat.checkSelfPermission(this,
                                        Manifest.permission.ACCESS_FINE_LOCATION)
                                        == PackageManager.PERMISSION_GRANTED) {

                                    if (mGoogleApiClient == null) {
                                        buildGoogleApiClient();
                                    }
                                    mMap.setMyLocationEnabled(true);
                                }

                            } else {

                                // Permission denied, Disable the functionality that depends on this permission.
                                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                            }
                            return;
                        }

                        // other 'case' lines to check for other permissions this app might request.
                        // You can add here other case statements according to your requirement.
                    }
                }

            }
