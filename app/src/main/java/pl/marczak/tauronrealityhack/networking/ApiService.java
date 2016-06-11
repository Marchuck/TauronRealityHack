package pl.marczak.tauronrealityhack.networking;

import pl.marczak.tauronrealityhack.model.SectorResponse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Admin on 2016-06-11.
 */
public interface ApiService {

    @GET(":8084/SystemKurierski/sectors")
    void getSensor( Callback<SectorResponse> callback);
    ///SystemKurierski/sectors
}
