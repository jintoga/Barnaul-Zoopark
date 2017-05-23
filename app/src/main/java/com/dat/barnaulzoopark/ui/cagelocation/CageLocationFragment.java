package com.dat.barnaulzoopark.ui.cagelocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by DAT on 5/23/2017.
 */

public class CageLocationFragment extends BaseFragment
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private static final int REQUEST_LOCATION_PERMISSIONS = 245;

    public static final int REQUEST_CHECK_SETTINGS = 4808;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
        UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private boolean isRequestingLocationUpdates;

    private Location currentLocation;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.startUpdateLocation)
    protected Button startUpdateLocation;
    @Bind(R.id.stopUpdateLocation)
    protected Button stopUpdateLocation;
    @Bind(R.id.latitudeText)
    protected TextView latitudeText;
    @Bind(R.id.longitudeText)
    protected TextView longitudeText;
    @Bind(R.id.container)
    protected View container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cage_location, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
            getString(R.string.cage_location));
        init();
        return view;
    }

    private void init() {
        isRequestingLocationUpdates = false;

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        updateLocationUI();
    }

    private boolean isRequiredPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSIONS);
            return true;
        }
        return false;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (currentLocation == null) {
            if (isRequiredPermissions()) {
                return;
            }
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            updateLocationUI();
        }
        if (isRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        switch (permsRequestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdate();
                } else {
                    showSnackBar(container, "Permission was not granted");
                    getActivity().onBackPressed();
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && isRequestingLocationUpdates) {
            startLocationUpdates();
        }
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            .setResultCallback(status -> {
                isRequestingLocationUpdates = false;
                setButtonsEnabledState();
            });
    }

    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest)
            .setResultCallback(locationSettingsResult -> {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (!isRequiredPermissions()) {
                            requestLocationUpdate();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            showSnackBar(container, e.getLocalizedMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be "
                            + "fixed here. Fix in Settings.";
                        isRequestingLocationUpdates = false;
                        showSnackBar(container, errorMessage);
                }
                updateUI();
            });
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
            CageLocationFragment.this);
    }

    private void setButtonsEnabledState() {
        startUpdateLocation.setEnabled(!isRequestingLocationUpdates);
        stopUpdateLocation.setEnabled(isRequestingLocationUpdates);
    }

    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    @SuppressLint("DefaultLocale")
    private void updateLocationUI() {
        if (currentLocation != null) {
            latitudeText.setText(String.format("%s: %f", "Lat", currentLocation.getLatitude()));
            longitudeText.setText(String.format("%s: %f", "Lng", currentLocation.getLongitude()));
        }
    }

    @OnClick(R.id.startUpdateLocation)
    protected void startUpdateLocationClicked() {
        if (!isRequestingLocationUpdates) {
            isRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    @OnClick(R.id.stopUpdateLocation)
    protected void stopUpdateLocationClicked() {
        stopLocationUpdates();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        isRequestingLocationUpdates = false;
                        updateUI();
                        showSnackBar(container, "No location access!");
                        break;
                }
                break;
        }
    }
}
