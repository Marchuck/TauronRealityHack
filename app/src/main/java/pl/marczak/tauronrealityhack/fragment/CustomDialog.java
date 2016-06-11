package pl.marczak.tauronrealityhack.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by pliszka on 26.01.16.
 */
public class CustomDialog extends DialogFragment {

    // code here

    @Override
    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }
}