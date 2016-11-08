package jonas.com.firegun.models;

import java.io.Serializable;
import java.util.List;

public class WeaponResponse implements Serializable {

    private List<Weapon> items;
    private String kind;
    private String etag;

    public List<Weapon> getItems() {
        return items;
    }

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

}
