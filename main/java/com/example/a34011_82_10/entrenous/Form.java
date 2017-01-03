package com.example.a34011_82_10.entrenous;

/**
 * This class gets the datas from the form to call Google map api
 *
 * @author: Anais Gueyte
 * @date:  22/12/2016
 *
 */

import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLngBounds;


public class Form extends AppCompatActivity implements PlaceSelectionListener {

    TextView address1;
    TextView address2;
    TextView erreurbox;
    CheckBox coffee;
    CheckBox bar;
    String addr1;
    String addr2;
    String barcoffee;
    Intent intent;
    PlaceAutocompleteFragment autocompleteFragment;
    PlaceAutocompleteFragment autocompleteFragment2;

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private TextView attributionsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Entre-nous: des bars et des cafés !");
        setContentView(R.layout.form);


        /** THE FIRST ENTRY **/
        address1 = (TextView) findViewById(R.id.autocomplete1);
        addr1 = address1.toString();
        attributionsTextView = (TextView) findViewById(R.id.txt_attributions);


        // Method #1
         autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search the first Location");
        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);

        /** END OF THE FIRST ENTRY **/


        /** FOR THE SECOND ENTRY **/

        address2 = (TextView) findViewById(R.id.autocomplete2);
        addr2 = address1.toString();
        attributionsTextView = (TextView) findViewById(R.id.txt_attributions2);

        // Method #1
        autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment2);
        autocompleteFragment2.setOnPlaceSelectedListener(this);
        autocompleteFragment2.setHint("Search the second Location");
        autocompleteFragment2.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);

        /** END OF  HE SECOND ENTRY **/




    Button launchMap = (Button) findViewById(R.id.launchSearch);

    launchMap.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        // Get the filter with checkbox (and it's working !)

        coffee = (CheckBox) findViewById(R.id.boxCoffee);
        bar = (CheckBox) findViewById(R.id.boxBar);

        while ((coffee.isChecked()) && (bar.isChecked()) == false) {
                erreurbox = (TextView) findViewById(R.id.erreurbox);
                erreurbox.setText("Vous devez choisir le type d'établissement recherché");
        }

        if ((coffee.isChecked()) && (bar.isChecked()) == true) {

            barcoffee = " 'cafe' | 'bar' ";
            Log.i("cafe & bar check", barcoffee);
        }
        if ((coffee.isChecked() == true) && (bar.isChecked() == false)) {
            barcoffee = " 'cafe' ";
            Log.i("cafe check", barcoffee);
        }
        if ((bar.isChecked() == true) && (coffee.isChecked() == false)) {
            barcoffee = " 'bar' ";
            Log.i("bar check", barcoffee);
        }

            /* sending the intent */

            intent = new Intent(Form.this, MapsActivity.class);
            //intent.putExtra("autocomplete1", addr1);
            //intent.putExtra("autocomplete2", addr2);
            //intent.putExtra("type", barcoffee);

        startActivity(intent);
    }
    });

}

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());
        address1.setText(getString(R.string.place_autocomplete_search_hint, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), place.getRating(), place.getId()));
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
}