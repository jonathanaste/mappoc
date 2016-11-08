package jonas.com.firegun.controllers;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import jonas.com.firegun.interfaces.DataListener;
import jonas.com.firegun.models.WeaponResponse;
import jonas.com.firegun.requests.BaseRequest;
import jonas.com.firegun.requests.GetWeaponsRequest;

/**
 * This class will manage queue of all requests.
 */
public class RequestManager {

    private static RequestQueue queue;
    private static RequestManager instance;

    private RequestManager(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    public void getWeaponsLocation(DataListener<WeaponResponse> listener){
        BaseRequest request = new GetWeaponsRequest(listener);
        queue.add(request);
    }
}