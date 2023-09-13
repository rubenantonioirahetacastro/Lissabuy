package lissabuy.com.Core.GoogleMaps.Remot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApi {
    @GET
    Call<String> getDatafromGoogleApi(@Url String url);
}
