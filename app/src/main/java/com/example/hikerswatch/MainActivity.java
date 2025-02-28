package com.example.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;
    TextView city;
    TextView country;
    TextView longitude;
    TextView latitude;
    TextView altitude;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Ethiopia", "permission granted");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0.2f, locationListener);
            }
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                city = findViewById(R.id.city);
                country = findViewById(R.id.country);
                altitude = findViewById(R.id.altitude);
                longitude = findViewById(R.id.longitude);
                latitude = findViewById(R.id.latitude);
                runOnUiThread(()->{
                    longitude.setText("Longitude:" + String.format("%.2f", location.getLongitude()));
                    latitude.setText("Latitude:" + String.format("%.2f", location.getLatitude()));
                    altitude.setText("Altitude:" +String.format("%.2f", location.getAltitude()));

                });


                try {
                    Log.i("Ethiopia", "arrived at the try");
                    List<Address> placeInfo = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                    Log.i("Ethiopia", "Arrived in the try section");
                    if(placeInfo != null && !placeInfo.isEmpty()) {
                        Address address = placeInfo.get(0);
                        Log.i("Ethiopia", address.toString());
                        runOnUiThread(()-> {
                            city.setText("City: " + address.getLocality());
                            country.setText("Country: " + address.getCountryName().toString());

                        });
                    }
                    else{
                        runOnUiThread(()->{
                            city.setText("Uknown");
                            country.setText("Uknown");

                        });
                    }

                } catch (IOException e) {
                    Log.i("Ethiopia", "GeoCoder failed no intenet connection");
                    runOnUiThread(()->{
                        city.setText("City: No Internet");
                        country.setText("Country: No Internet");
                    });
                }


            }
        };
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            Log.i("Ethiopia", "arrived at the else");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0.2f,locationListener);
        }

    }
}