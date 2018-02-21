package com.nitesh.brill.saleslines.Splash;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nitesh.brill.saleslines.R;

/**
 * Created by nitesh on 12/1/18.
 */

public class Netwrok_View_Activity extends AppCompatActivity {


    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noconnection);
        findViewById(R.id.btn_Refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected(Netwrok_View_Activity.this)) {

                    finish();

                } else {
                    Toast.makeText(Netwrok_View_Activity.this, " No Internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
