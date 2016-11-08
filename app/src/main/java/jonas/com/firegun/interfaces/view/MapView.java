package jonas.com.firegun.interfaces.view;

import android.location.Location;

import jonas.com.firegun.models.WeaponResponse;


public interface MapView {

    void populateData(WeaponResponse weaponResponse);

    void onError();

    void updateLocation(Location location);
}
