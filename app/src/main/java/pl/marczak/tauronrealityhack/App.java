package pl.marczak.tauronrealityhack;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import pl.marczak.tauronrealityhack.activity.MainActivity;
import pl.marczak.tauronrealityhack.monitoring.BleHelper;

/**
 * @author Lukasz Marczak
 * @since 11.06.16.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private BeaconManager beaconManager;
    private BleHelper ble;

    @Override
    public void onCreate() {
        super.onCreate();
        //  App ID & App Token can be taken from App section of Estimote Cloud.
//        EstimoteSDK.initialize(getApplicationContext(), appId, appToken);
// Optional, debug logging.
        EstimoteSDK.enableDebugLogging(true);
        startMonitoring();

        initFb();

    }

    private void startMonitoring() {
        beaconManager = new BeaconManager(getApplicationContext());
        // add this below:
        ble = new BleHelper();
        ble.sectorCallback = new BleHelper.SectorCallback() {
            @Override
            public void onSectorChanged(String major) {
                Log.e(TAG, "__________________________");
                Log.e(TAG, "onSectorChanged: " + major);
            }
        };

    }


    public void startScan() {
        Log.d(TAG, "startScan: ");
        ble.start(beaconManager);
    }

    public void stopScan() {
        Log.d(TAG, "stopScan: ");
        ble.stop(beaconManager);
    }

    public void showNotification(String title, String message, @DrawableRes int resource) {
        Log.d(TAG, "showNotification: ");
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(resource)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    private void initFb() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

}
