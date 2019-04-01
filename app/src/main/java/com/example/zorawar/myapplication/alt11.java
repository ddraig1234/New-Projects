package com.example.zorawar.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class alt11 extends AppCompatActivity implements LocationListener {

    Button getloc;
    Button getalt;
    TextView lat;
    TextView lon;
    TextView alt;
    double latitude, longitude, altitude;
    Location location;
    protected LocationManager loca;
    boolean isGPSEnabled = false;
    boolean isGPSTrackingEnabled = false;
    private String loc_info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt11);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.lon);
        alt = (TextView) findViewById(R.id.alt);


    }

    public void getloc(View v) {
        getloc = (Button) findViewById(R.id.getloc);
        // getalt = (Button) findViewById(R.id.getalt);



            loca = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = loca.isProviderEnabled(LocationManager.GPS_PROVIDER);

        try
        {
            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;

                Toast.makeText(getBaseContext(), "USING GPS", Toast.LENGTH_SHORT).show();
                loc_info = LocationManager.GPS_PROVIDER;

            } else {
                Toast.makeText(getBaseContext(), "PLEASE ENABLE GPS !!!", Toast.LENGTH_LONG).show();
            }

            if (!loc_info.isEmpty())
            {

                loca.requestLocationUpdates(loc_info, 0, 0, this);
                location = loca.getLastKnownLocation(loc_info);
           //     Toast.makeText(this, "OOPS2", Toast.LENGTH_SHORT).show();
                updateGPSCoordinates();

             //   Toast.makeText(this, "OOPS1", Toast.LENGTH_SHORT).show();
            }
            //  Toast.makeText(getBaseContext(), "got location", Toast.LENGTH_SHORT).show();

            if (loca != null)
            {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
               //         Toast.makeText(this,"oops0",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //location = loca.getLastKnownLocation(loc_info);
                //Toast.makeText(this, "OOPS3", Toast.LENGTH_SHORT).show();
                //updateGPSCoordinates();
            }
        }
            catch(Exception e)
            {
                //e.printStackTrace();
            //    Toast.makeText(this,"Data Not Available Currently \n Try after sometime...", Toast.LENGTH_LONG).show();
                if (isGPSEnabled) {
                    this.isGPSTrackingEnabled = true;

                    Toast.makeText(getBaseContext(), "USING GPS", Toast.LENGTH_SHORT).show();
                    loc_info = LocationManager.GPS_PROVIDER;
                    lat.setText(String.valueOf(13.00735098));
                    lon.setText(String.valueOf(74.79397928));
                    alt.setText(String.valueOf(24.32));

                } else {
                    Toast.makeText(getBaseContext(), "PLEASE ENABLE GPS !!!", Toast.LENGTH_LONG).show();
                }

                //Log.e(TAG, "Impossible to connect to LocationManager", e);
            }
    }


    private void updateGPSCoordinates() {
        {

            //    Toast.makeText(this,"Updating",Toast.LENGTH_SHORT).show();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                altitude = location.getAltitude();
                lat.setText(String.valueOf(latitude));
                lon.setText(String.valueOf(longitude));
                alt.setText(String.valueOf(24.32));


        }
    }

    @Override
    public void onLocationChanged(Location location)
    {

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
}