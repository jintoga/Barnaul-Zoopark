package com.dat.barnaulzoopark.ui.animaleditor;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by DAT on 5/17/2017.
 */

class LocationDetectHelper
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    static final int REQUEST_LOCATION_PERMISSIONS = 242;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private double currentLatitude;
    private double currentLongitude;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private Activity activity;
    private LocationDetectListener locationDetectListener;

    LocationDetectHelper(@NonNull Activity activity,
        @NonNull LocationDetectListener locationDetectListener) {
        this.activity = activity;
        this.locationDetectListener = locationDetectListener;

        googleApiClient = new GoogleApiClient.Builder(activity)
            // The next two lines tell the new client that “this” current class will handle connection stuff
            .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
            //fourth line adds the LocationServices API endpoint from GooglePlayServices
            .addApi(LocationServices.API).build();

        // Create the LocationRequest object
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1000); // 1 second, in milliseconds
    }

    void connectGoogleApiClient() {
        //Now lets connect to the API
        if (googleApiClient != null
            && !googleApiClient.isConnected()
            && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    void reconnectGoogleApiClient() {
        //Reconnect to the API
        if (googleApiClient != null) {
            googleApiClient.reconnect();
        }
    }

    void disconnectGoogleApiClient() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkForPermissions()) {
                return;
            }
        }

        startDetecting();
    }

    private boolean checkForPermissions() {
        if (ActivityCompat.checkSelfPermission(this.activity,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.activity,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSIONS);
            return true;
        }
        return false;
    }

    @SuppressWarnings("MissingPermission")
    void startDetecting() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest, this);
        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            locationDetectListener.onLocationChanged(currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this.activity,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                locationDetectListener.onNoResolutionForConnectionFailed(e.getLocalizedMessage());
            }
        } else {
            String error;
            if (connectionResult.getErrorMessage() != null) {
                error = connectionResult.getErrorMessage();
            } else {
                error = "Connection Failed Error:" + connectionResult.getErrorCode();
            }
            locationDetectListener.onNoResolutionForConnectionFailed(error);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        locationDetectListener.onLocationChanged(currentLatitude, currentLongitude);
    }

    interface LocationDetectListener {
        void onLocationChanged(double currentLatitude, double currentLongitude);

        void onNoResolutionForConnectionFailed(@NonNull String msg);
    }
}
