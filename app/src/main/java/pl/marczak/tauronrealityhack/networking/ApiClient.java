package pl.marczak.tauronrealityhack.networking;

import android.content.Context;

import pl.marczak.tauronrealityhack.model.SectorResponse;
import pl.marczak.tauronrealityhack.util.GsonUtil;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Admin on 2016-06-11.
 */
public class ApiClient {

    private static ApiClient instance = null;
    private ApiService apiService;
    private Context context;

    private static final String BASE_URL = "http://10.112.27.101:8084";

    private ApiClient(final Context context) {
        this.context = context;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(GsonUtil.getGson()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .build();

        apiService = restAdapter.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public void getSensor(final Callback<SectorResponse> callback) {
        apiService.getSensor(new Callback<SectorResponse>() {
            @Override
            public void success(SectorResponse sectorResponse, Response response) {
                if (response.getStatus() >= 200 && response.getStatus() < 400 && sectorResponse != null) {
                    callback.success(sectorResponse,response);
                } else {
                    callback.failure(null);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }
}
