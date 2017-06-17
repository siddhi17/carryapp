package com.carryapp.Fragments;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carryapp.Activities.HomeActivity;
import com.carryapp.R;
import com.carryapp.helper.CommonUtils;
import com.carryapp.helper.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Location mLastLocation;
    LocationManager mLocationManager = null;
    public static GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private LatLng mLatLang;
    private Marker mMarker;
    public static String TAG = "Location";
    public static Boolean mapConnected = false;
    private final static int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 10;

    private GoogleApiClient mGoogleApiClient;

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.NETWORK_PROVIDER),
            new LocationListener(LocationManager.GPS_PROVIDER)

    };

    public PaymentsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payments, container, false);


        final Toolbar toolbar = (Toolbar) ((HomeActivity) getActivity()).findViewById(R.id.toolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView mLogo = (ImageView)getActivity().findViewById(R.id.imgLogo);
        mLogo.setVisibility(View.GONE);

        TextView title = (TextView) getActivity().findViewById(R.id.textTitle);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.paymentsTitle);

        mGoogleMap = null;

        setUpMapIfNeeded();

        return view;
    }
    private void setUpMapIfNeeded() {
        if (mGoogleMap == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
            mapFragment.getMapAsync(this);

        }
    }
    public void setMap() {

        try {

            //get location of a user
            mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            //show marker of user's location

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mLatLang);
            markerOptions.title(getString(R.string.position));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMarker = mGoogleMap.addMarker(markerOptions);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLang).zoom(14).build();

            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);

            //remove location updates, get location only once.

            mLocationManager.removeUpdates(mLocationListeners[0]);
            mLocationManager.removeUpdates(mLocationListeners[1]);


        } catch (SecurityException e) {

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        mapConnected = true;

        if (mapConnected) {

            //check for granted permissions, request permissions if not granted

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

           /*     new android.app.AlertDialog.Builder(getActivity())
                        .setMessage(getResources().getString(R.string.alertLocation))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //grant permission
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

                            }

                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

            } else {
                //   ((HomeActivity) getActivity()).showAlert(getResources().getString(R.string.locationAlert));
                initializeLocationManager();
                requestLocation();
            }
        } else {

            Toast.makeText(getActivity(),R.string.check_network,Toast.LENGTH_LONG).show();

                    /*    CommonUtils.showAlert(getActivity(), getResources().getString(R.string.check_network), "Check Network");*/
        }
    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Toast.makeText(getActivity(),"onConnected",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        //  Toast.makeText(getActivity(),"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //    Toast.makeText(getActivity(),"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    private void initializeLocationManager() {
        // Log.e(Application.TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

        }

    }

    public class LocationListener implements android.location.LocationListener {

        public LocationListener() {
        }

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);

            //get current location

            if(mLastLocation != null && !mLastLocation.equals("")) {
                mLastLocation.set(location);

                if (mapConnected)
                    setMap();
            }
            else {

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    //request for location, first by network, then by gps

    public void requestLocation() {

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    //get location when permissions are allowed

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    initializeLocationManager();
                    requestLocation();
                } else {
                    //   ((HomeActivity) getActivity()).showAlert(getResources().getString(R.string.locationAlert));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.


                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
}
