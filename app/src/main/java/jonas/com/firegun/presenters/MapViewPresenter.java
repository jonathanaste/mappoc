package jonas.com.firegun.presenters;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import jonas.com.firegun.FireGunsApplication;
import jonas.com.firegun.interfaces.DataListener;
import jonas.com.firegun.interfaces.view.MapView;
import jonas.com.firegun.models.WeaponResponse;

public class MapViewPresenter implements DataListener<WeaponResponse>,LocationListener {

    private MapView view;

    public MapViewPresenter(MapView view){
        this.view = view;
    }

    public void getWeaponsLocation(){
        FireGunsApplication.requestManager.getWeaponsLocation(this);
    }

    @Override
    public void onError(String msg) {
        view.onError();
    }

    @Override
    public void onDataUpdate(WeaponResponse response) {
        if(response == null || response.getItems().isEmpty()){
            view.onError();
        }else {
            view.populateData(response);
        }
    }

    // Location map updated
    @Override
    public void onLocationChanged(Location location) {
        view.updateLocation(location);
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
