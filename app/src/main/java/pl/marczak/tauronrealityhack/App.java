package pl.marczak.tauronrealityhack;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;

import pl.marczak.tauronrealityhack.activity.MainActivity;

/**
 * @author Lukasz Marczak
 * @since 11.06.16.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private BeaconManager beaconManager;

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
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d(TAG, "onServiceReady: " + Thread.currentThread().getName());
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        13235, 56177));

                beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                    @Override
                    public void onEnteredRegion(Region region, List<Beacon> list) {
                        Log.d(TAG, "onEnteredRegion: " + Thread.currentThread().getName());
                        showNotification(
                                "onEnteredRegion",
                                "beacons:" + list.size(), android.R.drawable.ic_dialog_info);

                    }

                    @Override
                    public void onExitedRegion(Region region) {
                        // could add an "exit" notification too if you want (-:
                        showNotification(
                                "onExitedRegion",
                                "", android.R.drawable.ic_dialog_dialer);
                    }
                });
            }
        });

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
