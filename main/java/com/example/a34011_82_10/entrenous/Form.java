package com.example.a34011_82_10.entrenous;

/**
 * This class gets the datas from the form to call Google map api
 *
 * @author: Anais Gueyte
 * @date:  22/12/2016
 *
 */

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;


public class Form extends AppCompatActivity implements PlaceSelectionListener {

    private  TextView address1;
    private TextView address2;
    private PlaceAutocompleteFragment autocompleteFragment;
    private PlaceAutocompleteFragment autocompleteFragment2;
    private TextView attributionsTextView;
    private TextView attributionsTextView2;
    private String addr1;
    private String addr2;

    private String location;
    private String location2;
    private LatLng latlong;
    private LatLng latlong2;
    private LatLng middlepoint;
    String sMiddlePoint;

    private TextView erreurbox;
    private CheckBox coffee;
    private CheckBox bar;
    private String barcoffee;

    private Intent intent;

    private static final String LOG_TAG = "PlaceSelectionListener";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Entre-nous: des bars et des cafés !");
        setContentView(R.layout.form);


        /** THE FIRST ENTRY **/
        address1 = (TextView) findViewById(R.id.autocomplete1);
        attributionsTextView = (TextView) findViewById(R.id.txt_attributions);


        // Method that gets the address via Google Places and set it into the textView
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("_ _ _ _ _ _");
        /** END OF THE FIRST ENTRY **/


        /** FOR THE SECOND ENTRY **/

        address2 = (TextView) findViewById(R.id.autocomplete2);
        attributionsTextView2 = (TextView) findViewById(R.id.txt_attributions2);

        // Method that gets the address via Google Places and set it into the textView
        autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment2);
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i(LOG_TAG, "Place Selected: " + place.getName());

                attributionsTextView2.setText(getString(R.string.place_autocomplete_search_hint, place
                        .getName(), place.getAddress(), place.getPhoneNumber(), place
                        .getWebsiteUri(), place.getRating(), place.getId()));

                addr2 = place.getAddress().toString();
                Log.i("info", "addr2 : " + addr2);

                if (!TextUtils.isEmpty(place.getAttributions())) {
                    attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
                }
            }

            @Override
            public void onError(Status status) {
            }
        });
        autocompleteFragment2.setHint("_ _ _ _ _ _");

        /** END OF  HE SECOND ENTRY **/


        erreurbox = (TextView) findViewById(R.id.erreurbox);
        Button launchMap = (Button) findViewById(R.id.launchSearch);

        /** WHEN PRESS THE BUTTON **/

        launchMap.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                try {
                    midPoint(addr1, addr2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                coffee = (CheckBox) findViewById(R.id.boxCoffee);
                bar = (CheckBox) findViewById(R.id.boxBar);

                // MUST RETHINKING THE FILTER METHOD //

                if ((coffee.isChecked() == false) && (bar.isChecked() == false)) {

                    erreurbox.setText("Vous devez choisir le type d'établissement recherché");
                }

                // Get the filter with checkbox (and it's working !)


                if ((coffee.isChecked() == true) && (bar.isChecked() == false)) {
                    barcoffee = " 'cafe' ";
                    erreurbox.setText("");
                    Log.i("cafe check", barcoffee);
                    /* sending the intent */

                    intent = new Intent(Form.this, MapsActivity.class);

                    Bundle mBundle = new Bundle();

                    mBundle.putParcelable("address1", latlong);
                    Log.i("info", "bundle address1 : " + latlong);
                    mBundle.putParcelable("address2", latlong2);
                    Log.i("info", "bundle address2 : " + latlong2);
                    mBundle.putParcelable("middlepoint", middlepoint);
                    Log.i("info", "bundle middlePoint : " + middlepoint);

                    intent.putExtras(mBundle);


                    startActivity(intent);
                }
                if ((bar.isChecked() == true) && (coffee.isChecked() == false)) {
                    barcoffee = " 'bar' ";
                    erreurbox.setText("");
                    Log.i("bar check", barcoffee);
                    /* sending the intent */

                    intent = new Intent(Form.this, MapsActivity.class);

                    Bundle mBundle = new Bundle();

                    mBundle.putParcelable("address1", latlong);
                    Log.i("info", "bundle address1 : " + latlong);
                    mBundle.putParcelable("address2", latlong2);
                    Log.i("info", "bundle address2 : " + latlong2);
                    mBundle.putParcelable("middlepoint", middlepoint);
                    Log.i("info", "bundle middlePoint : " + middlepoint);

                    intent.putExtras(mBundle);


                    startActivity(intent);
                }

                if ((coffee.isChecked() == true) && (bar.isChecked() == true)) {

                    barcoffee = " 'cafe' | 'bar' ";
                    erreurbox.setText("");
                    Log.i("cafe & bar check", barcoffee);

                    /* sending the intent */

                    intent = new Intent(Form.this, MapsActivity.class);

                    Bundle mBundle = new Bundle();

                    mBundle.putParcelable("address1", latlong);
                    Log.i("info", "bundle address1 : " + latlong);
                    mBundle.putParcelable("address2", latlong2);
                    Log.i("info", "bundle address2 : " + latlong2);
                    mBundle.putParcelable("middlepoint", middlepoint);
                    Log.i("info", "bundle middlePoint : " + middlepoint);

                    intent.putExtras(mBundle);


                    startActivity(intent);
                }


            }
        });

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        attributionsTextView.setText(getString(R.string.place_autocomplete_search_hint, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), place.getRating(), place.getId()));

        addr1 = place.getAddress().toString();

        if (!TextUtils.isEmpty(place.getAttributions())) {
            attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
        }

    }


    @Override
    public void onError(Status status) {
        Log.e("onError: Status = ", status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }


    public void midPoint(String addr1, String addr2) throws IOException {

        Geocoder geocoder = new Geocoder(Form.this);

        /*** first address ***/
        location = addr1;
        List<Address> list = geocoder.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();
        latlong = new LatLng(add.getLatitude(), add.getLongitude());

        /*** second address ***/
        location2 = addr2;
        List<Address> list2 = geocoder.getFromLocationName(location2, 1);
        Address add2 = list2.get(0);
        String locality2 = add2.getLocality();
        latlong2 = new LatLng(add2.getLatitude(), add2.getLongitude());

        double lat1 = latlong.latitude;
        double lon1 = latlong.longitude;
        double lat2 = latlong2.latitude;
        double lon2 = latlong2.longitude;


        double latiCentre = (lat1 + lat2) / 2;
        double longCentre = (lon1 + lon2) / 2;

        middlepoint = new LatLng(latiCentre, longCentre);

    }


}