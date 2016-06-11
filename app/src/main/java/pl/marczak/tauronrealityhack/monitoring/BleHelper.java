package pl.marczak.tauronrealityhack.monitoring;

import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;



/**
 * @author Lukasz Marczak
 * @since 11.06.16.
 */
public class BleHelper {
    public static final String TAG = BleHelper.class.getSimpleName();
    Region region;



    public interface SectorCallback {
        void onSectorChanged(String major);
    }

    @Nullable
    public SectorCallback sectorCallback;


    public void stop(BeaconManager beaconManager) {
        beaconManager.stopRanging(region);
    }

    public void start(final BeaconManager beaconManager) {
        Log.d(TAG, "start: ");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d(TAG, "onServiceReady: ");
                onConnected(beaconManager);
            }
        });
    }

    private void onConnected(BeaconManager beaconManager) {
        Log.d(TAG, "onConnected: ");
        region = new Region("all beacons region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                Log.d(TAG, "onBeaconsDiscovered: ");
                String mostCommon = pollMostCommon(list);
                if (sectorCallback != null) sectorCallback.onSectorChanged(mostCommon);
//                onDiscovered(list);
            }
        });
        beaconManager.startRanging(region);



    }

    private void onDiscovered(List<Beacon> list) {
        Log.d(TAG, "onDiscovered: ");
        String mostCommon = pollMostCommon(list);
        if (sectorCallback != null) sectorCallback.onSectorChanged(mostCommon);
    }


    public static synchronized String pollMostCommon(List<Beacon> beacons) {
        if (beacons.size() == 0) return "empty sections";
        List<Integer> list = new ArrayList<>();
        Set<Integer> majors = new HashSet<>();

        String[] collection = new String[beacons.size()];
        for (int k = 0; k < beacons.size(); k++) {
            Beacon b = beacons.get(k);
            int major = b.getMajor();
//            Log.i(TAG, "major: " + major);
            collection[k] = String.valueOf(major);
//            majors.add(major);
            list.add(major);
        }
        Map<Integer, Integer> ids = new HashMap<>();

        for (Integer i : list) {
            if (ids.containsKey(i)) {
                ids.put(i, ids.get(i) + 1);
            } else {
                ids.put(i, 1);
            }
        }
        StringBuilder sb = new StringBuilder();

        int max = -1;
        String maxMajor = "";
        for (Integer k : ids.keySet()) {
            sb.append(k).append(":").append(ids.get(k)).append(",");
            if (ids.get(k) > max) {
                max = ids.get(k);
                maxMajor = String.valueOf(k);
            }
        }

        return maxMajor;
    }
}
