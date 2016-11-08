package jonas.com.firegun.models;

import java.io.Serializable;

public class Weapon implements Serializable {

    private Location location;
    private int radius;
    private String code;
    private int radiusInMeter;
    private String kind;

    public Location getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public String getCode() {
        return code;
    }

    public int getRadiusInMeter() {
        return radiusInMeter;
    }

    public String getKind() {
        return kind;
    }
}
