package com.nitesh.brill.saleslines.Common_Files;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.nitesh.brill.saleslines.Splash.Netwrok_View_Activity;

/**
 * Created by nitesh on 12/1/18.
 */

public class NetworkConnectivityChange extends BroadcastReceiver {


    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (isConnected(context)) {

          //  Toast.makeText(context, " Internet Working", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(context, " No Internet Connection", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, Netwrok_View_Activity.class);
            context.startActivity(i);

        }

    }
}
