package com.nitesh.brill.saleslines._Manager_Classes.Manager_Location;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

/**
 * Created by Paras-Android on 07-02-2018.
 */

public class InfoWindowRefresher implements Callback {
    private Marker markerToRefresh;

    public InfoWindowRefresher(Marker markerToRefresh) {
        this.markerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {
        markerToRefresh.showInfoWindow();
    }

    @Override
    public void onError() {}
}
