package com.rt.qpay99.activity.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;


public class AgentMap extends AppCompatActivity  implements OnMapReadyCallback {


    private String TAG = this.getClass().getName();

    LocationRequest mLocationRequest = new LocationRequest();
    Button btnRefresh;

    private  static int UPDATE_INTERVAL = 5000;
    private  static int FAST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    GeoQuery geoQuery;
     GeoFire geoFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                geoFire.removeLocation("", new GeoFire.CompletionListener() {
//                    @Override
//                    public void onComplete(String key, DatabaseError error) {
//
//                    }
//                });

                geoQuery.removeAllListeners();

            }
        });

        createLocationRequest();

        setUpdateLocation();

    }

    private void createLocationRequest() {
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void setUpdateLocation() {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        if(!Config.isLocationAvaiable){
            Config.Latitude = 3.1006089;
            Config.Longitude=101.5884858;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);
        LatLng sydney = new LatLng( Config.Latitude, Config.Longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("You"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofirelocation");
        GeoFire geoFire = new GeoFire(ref);


        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(Config.Latitude, Config.Longitude), 2000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                DLog.e(TAG,"Key "+key+" entered the search area at ["+location.latitude+","+location.longitude+"]");
                String mKey = key.substring(0,key.length()-5) + "*****";
                mKey = key;
                if(SharedPreferenceUtil.getsClientUserName().equalsIgnoreCase("60166572577")){
                    mKey = key;
                }
                googleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title(mKey));

            }

            @Override
            public void onKeyExited(String key) {
                DLog.d("test","Key %s is no longer in the search area"+key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                DLog.d("test", "Key %s moved within the search area to [%f,%f]" + key + location.latitude + location.longitude);
            }

            @Override
            public void onGeoQueryReady() {
                DLog.d("test","All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                DLog.d("test","There was an error with this query: " + error);
            }
        });



    }
}
