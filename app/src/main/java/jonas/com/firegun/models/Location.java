package jonas.com.firegun.models;

import java.io.Serializable;

public class Location implements Serializable {

    private float latitude;
    private float longitude;

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

}
