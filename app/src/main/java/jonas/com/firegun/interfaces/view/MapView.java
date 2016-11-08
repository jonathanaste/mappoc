package jonas.com.firegun.interfaces.view;

import jonas.com.firegun.models.WeaponResponse;


public interface MapView {

    void populateData(WeaponResponse weaponResponse);

    void onError();

}
