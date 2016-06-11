package pl.marczak.tauronrealityhack.networking;

import android.content.Context;

import pl.marczak.tauronrealityhack.model.User;
import pl.marczak.tauronrealityhack.model.UserData;
import pl.marczak.tauronrealityhack.util.GsonUtil;
import pl.marczak.tauronrealityhack.util.PreferencesUtil;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Admin on 2016-06-11.
 */
public class ApiClient {

    private static ApiClient instance = null;
    private ApiService apiService;
    private Context context;

    private static final String BASE_URL = "http://random.pl";

    private ApiClient(final Context context) {
        this.context = context;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(GsonUtil.getGson()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        UserData userData = PreferencesUtil.getUserData(context);
                        if (userData != null) {
                            User user = userData.getUser();
                            if (user != null) {
                                String accessToken = user.getAccessToken();
                                if (accessToken != null) {
                                    request.addHeader("X-Access-Token", accessToken);
                                }
                            }
                        }
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
}
