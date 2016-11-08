package jonas.com.firegun.requests;


import jonas.com.firegun.interfaces.DataListener;
import jonas.com.firegun.models.WeaponResponse;

public class GetWeaponsRequest extends BaseRequest<WeaponResponse> {

    private static final String url = "https://redarmyserver.appspot.com/_ah/api/myApi/v1/torretinfocollection";

    public GetWeaponsRequest(DataListener<WeaponResponse> dataListener) {
        super(Method.GET,
                url,
                WeaponResponse.class,
                dataListener);
    }
}
