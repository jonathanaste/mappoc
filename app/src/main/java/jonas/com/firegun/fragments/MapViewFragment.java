package jonas.com.firegun.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import jonas.com.firegun.R;
import jonas.com.firegun.interfaces.view.MapView;
import jonas.com.firegun.models.Weapon;
import jonas.com.firegun.models.WeaponResponse;
import jonas.com.firegun.presenters.MapViewPresenter;

public class MapViewFragment extends SupportMapFragment implements OnMapReadyCallback, MapView,
        LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap map;
    private LatLng myLocation;
    private MapViewPresenter presenter;
    private List<Circle> circles;
    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new MapViewPresenter(this);
        circles = new ArrayList<>();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Getting the map instance.
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Keeping an instance of our map.
        this.map = googleMap;

        // If permission are granted, we are going to set our location and add the listener.
        if (areLocationPermissionGranted()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            setLocationMarker(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        } else { // If permissions are not granted, we request them.
            requestLocationPermissions();
        }

        // Getting weapons data
        presenter.getWeaponsLocation();
    }

    @Override
    public void populateData(WeaponResponse weaponResponse) {
        //For each weapon, we are going to add a circle with the reach of the weapon.
        for (Weapon w : weaponResponse.getItems()) {
            LatLng latLng = new LatLng(w.getLocation().getLatitude(), w.getLocation().getLongitude());
            circles.add(map.addCircle(new CircleOptions().center(latLng)
                    .strokeWidth(2)
                    .radius(w.getRadiusInMeter())
                    .fillColor(ContextCompat.getColor(getContext(), R.color.radius_fill))
                    .strokeColor(ContextCompat.getColor(getContext(), R.color.radius_stroke))));

        }

        isInDangerZone();
    }

    @Override
    public void onError() {
        //// TODO: 11/7/2016  
    }

    /**
     * Use this method to request permission for location.
     */
    private void requestLocationPermissions() {
        // If permissions are not granted we need to request them in order to use Location.
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    /**
     * Use this method to check Location permissions.
     */
    private boolean areLocationPermissionGranted() {

        return (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void setLocationMarker(Location location) {
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions()
                .position(myLocation)
                .title(getString(R.string.current_position)));
        //Moving camera map to my current location.
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                myLocation, 15));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permissions", " populateData(weaponList)");
                    setLocationMarker(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                } else {
                    //TODO empty sttate
                }
            }
        }
    }

    private void isInDangerZone() {
        if (myLocation != null) {
            for (Circle circle : circles) {
                float[] distance = new float[1];
                Location.distanceBetween(circle.getCenter().latitude, circle.getCenter().longitude,
                        myLocation.latitude, myLocation.longitude, distance);
                if (distance[0] < circle.getRadius()) {
                    Toast.makeText(getContext(), "DANGER ZONE!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // In case the user didn't grant access we check it again
        if (areLocationPermissionGranted()) {
            // updating location
            setLocationMarker(location);
            // checking if we are on a danger zone
            isInDangerZone();
        } else {
            // requesting permission
            requestLocationPermissions();
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

    @Override
    public void onPause() {
        super.onPause();
        // Removing listener in case we go onPause()
        locationManager.removeUpdates(this);
    }
}
