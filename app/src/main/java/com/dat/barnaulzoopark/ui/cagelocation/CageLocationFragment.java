package com.dat.barnaulzoopark.ui.cagelocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.NinePatchDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsDetailActivity;
import com.dat.barnaulzoopark.ui.cagelocation.adapters.CageLocationAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

public class CageLocationFragment
    extends BaseMvpFragment<CageLocationContract.View, CageLocationContract.UserActionListener>
    implements CageLocationContract.View, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener,
    CageLocationAdapter.AnimalClickListener {

    private static final float DISTANCE_TO_CAGE = 20; //20 meters
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
    @Bind(R.id.closeAnimals)
    protected RecyclerView closeAnimals;
    private CageLocationAdapter locationAdapter;

    private List<Animal> animals;

    private MaterialDialog progressDialog;

    @Override
    public void onAnimalClicked(int position) {
        AnimalsDetailActivity.start(getActivity(), locationAdapter.getAnimals(), position);
    }

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

    @Override
    public void onLoadAnimalsError(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(container, localizedMessage);
    }

    @Override
    public void onLoadAnimalsSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(getContext(),
                getString(R.string.loading));
        }
        progressDialog.setContent(getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> animals) {
        this.animals = animals;
    }

    private void init() {
        initRecyclerView();

        isRequestingLocationUpdates = false;

        initGoogleApiClient();
        initLocationRequest();
        initLocationSettingsRequest();
    }

    private void initRecyclerView() {
        closeAnimals.setLayoutManager(new LinearLayoutManager(getContext()));
        closeAnimals.addItemDecoration(new SimpleListDividerDecorator(
            ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material),
            true));
        closeAnimals.addItemDecoration(new ItemShadowDecorator(
            (NinePatchDrawable) ContextCompat.getDrawable(getContext(),
                R.drawable.material_shadow_z1)));
        locationAdapter = new CageLocationAdapter(this);
        closeAnimals.setAdapter(locationAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadAnimals();
    }

    protected synchronized void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    protected void initLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void initLocationSettingsRequest() {
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

    @NonNull
    @Override
    public CageLocationContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new CageLocationPresenter(database);
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
            filterAnimalsLocation(currentLocation);
        }
    }

    private void filterAnimalsLocation(@NonNull Location currentLocation) {
        if (animals == null) {
            return;
        }
        List<Animal> closeAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            float[] result = new float[3];
            Location.distanceBetween(animal.getLat(), animal.getLng(),
                currentLocation.getLatitude(), currentLocation.getLongitude(), result);
            if (result[0] < DISTANCE_TO_CAGE) {
                closeAnimals.add(animal);
            }
            Log.d("DIST", animal.getName() + " is " + result[0] + " meters away");
        }
        locationAdapter.setData(closeAnimals, currentLocation);
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
