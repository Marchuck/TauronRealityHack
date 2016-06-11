package pl.marczak.tauronrealityhack.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pl.marczak.tauronrealityhack.model.UserData;


public class PreferencesUtil {

    private static final String PREFERENCES_NAME = "co.woolet.PREFERENCES";

    private static final String USER_DATA = "co.woolet.USER_DATA";
    private static final String USER_PASSWORD = "co.woolet.USER_PASSWORD";


    public static boolean isUserLoggedIn(Context context) {
        return getSharedPreferences(context).contains(USER_DATA);
    }

    public static String getUserPassword(Context context) {
        return getSharedPreferences(context).getString(USER_PASSWORD, null);
    }

    public static boolean saveUserPassword(Context context, String password) {
        return getEditor(context).putString(USER_PASSWORD, password).commit();
    }

    public static boolean saveUserData(Context context, UserData userData) {
        Gson gson = GsonUtil.getGson();
        return getEditor(context).putString(USER_DATA, gson.toJson(userData, UserData.class)).commit();
    }

    public static UserData getUserData(Context context) {
        Gson gson = GsonUtil.getGson();
        return gson.fromJson(getSharedPreferences(context).getString(USER_DATA, null), UserData.class);
    }

    public static boolean removeUserData(Context context) {
        return getEditor(context).remove(USER_DATA).commit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

}
