package lissabuy.com.Core.GoogleMaps;

import lissabuy.com.Core.GoogleMaps.Remot.IGoogleApi;
import lissabuy.com.Core.GoogleMaps.Remot.RetrofitClient;

public class Common {
    public static final  String baseURL="https://googleapis.com";
    public  static IGoogleApi getGoogleApi()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
