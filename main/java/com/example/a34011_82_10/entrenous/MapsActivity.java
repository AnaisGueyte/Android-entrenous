package com.example.a34011_82_10.entrenous;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
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

    protected GoogleMap mMap;
    private Marker marker;
    private Intent i;
    private Bundle b;

    private LatLng latlong;
    private LatLng latlong2;
    protected LatLng middlePoint;
    protected String barcoffee;


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
        barcoffee = b.getString("type");
        Log.i("info", "bundle dans MAP ACTIVITY bar coffee : " + barcoffee);



        StringBuilder sbValue = new StringBuilder(sbMethod(barcoffee));
        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sbValue.toString());



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
              .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_outline_black_24dp))
              .position(latlong);

      MarkerOptions markerOptions2 = new MarkerOptions()
              .title("Point 2")
              .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_outline_black_24dp))
              .position(latlong2);

      MarkerOptions middleOptions = new MarkerOptions()
              .title("Mi-chemin")
              .position(middlePoint);



      marker = mMap.addMarker(markerOptions);
      marker = mMap.addMarker(markerOptions2);
      marker = mMap.addMarker(middleOptions);

      Log.i("info", "APRES ADD MARKER");

  }



    /********************************************************************/
    /******************** PRIVATE CLASSES  ******************************/
    /********************************************************************/

    public StringBuilder sbMethod(String barcoffee) {

        //use your current location here
        double mLatitude = middlePoint.latitude;
        double mLongitude = middlePoint.longitude;

        Log.i("info", " bar coffee dans sbMethod: " + barcoffee);

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + barcoffee);
        sb.append("&sensor=true");
        sb.append("&key=«YOUR KEY»);

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.19.254.1", 8080));

            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection(proxy);

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
                Log.d("Exception download url", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

            JSONObject jObject;

            // Invoked by execute() method of this object
            @Override
            protected List<HashMap<String, String>> doInBackground(String... jsonData) {

                List<HashMap<String, String>> places = null;
                Place_JSON placeJson = new Place_JSON();

                try {
                    jObject = new JSONObject(jsonData[0]);

                    places = placeJson.parse(jObject);

                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
                return places;
            }

            // Executed after the complete execution of doInBackground() method
            @Override
            protected void onPostExecute(List<HashMap<String, String>> list) {

                Log.d("Map", "list size: " + list.size());
                // Clears all the existing markers;
               /* mMap.clear();*/

                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);


                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("place_name");

                    Log.d("Map", "place: " + name);

                    // Getting vicinity
                    String vicinity = hmPlace.get("vicinity");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    markerOptions.title(name + " : " + vicinity);

                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                    // Placing a marker on the touched position
                    Marker m = mMap.addMarker(markerOptions);

                }
            }
        }

    }

}
