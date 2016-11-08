package jonas.com.firegun.presenters;

import jonas.com.firegun.FireGunsApplication;
import jonas.com.firegun.interfaces.DataListener;
import jonas.com.firegun.interfaces.view.MapView;
import jonas.com.firegun.models.WeaponResponse;

public class MapViewPresenter implements DataListener<WeaponResponse> {

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

}
