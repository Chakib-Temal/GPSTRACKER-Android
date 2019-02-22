package com.temal.chakib.fr.gpstrackerandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * TAPE YOU KEY OF API GOOGLE MAPS PLEASE
     */
    public final static String KEY = "XXXXXXXX";

    private LocationManager mLocationManager = null;
    private LocationC locationEvent          = null;
    private TextView latitude                = null;
    private TextView longitude               = null;
    private WebView mWebview                 = null;
    private int alreadyGetPosition           = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.latitude  = (TextView) findViewById(R.id.latitude);
        this.longitude = (TextView) findViewById(R.id.longitude);
        this.mWebview = (WebView) findViewById(R.id.googlemap_webView);
        this.mWebview.getSettings().setJavaScriptEnabled(true);

        this.mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mLocationManager.requestLocationUpdates
                (LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);



        final Activity activity = this;
        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        /**
         * Reception of event for the first GetLocationEvent
         * PUT YOUR KEY API GOOGLE IN THE LAST LINE OF onGetLocation -> VARIABLE PUBLIC
         */
        this.locationEvent = LocationC.getInstance();
        this.locationEvent.setListner(new LocationC.OnLocationListner() {
            @Override
            public void onGetLocation(Location location) {
                mWebview .loadUrl("https://www.google.com/maps/search/?api=1&query"+location.getLatitude()+","
                        +location.getLongitude()+"=&key="+KEY);
            }
        });
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            String latitudeText = Double.toString(location.getLatitude());
            String longitudeText = Double.toString(location.getLongitude());

            latitude.setText(latitudeText);
            longitude.setText(longitudeText);

            /**
             * if = 0 -> event
             */
            if (alreadyGetPosition == 0){
                locationEvent.getListner().onGetLocation(location);
                alreadyGetPosition++;
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        alreadyGetPosition = 0;
    }

}
