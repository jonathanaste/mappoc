package jonas.com.firegun;

import android.app.Application;

import jonas.com.firegun.controllers.RequestManager;

public class FireGunsApplication extends Application {

    public static RequestManager requestManager;

    @Override
    public void onCreate() {
        super.onCreate();
        requestManager = RequestManager.getInstance(this);
    }
}
