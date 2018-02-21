package com.nitesh.brill.saleslines.Splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nitesh on 13/12/17.
 */

public class UninstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Detetct", "ACTION_DELETE");

        if (intent.getData().getSchemeSpecificPart() == "com.nitesh.brill.saleslines")
            Log.e("Detetct", "Uninstall");
    }
}
