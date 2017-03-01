package com.igorbelogubov.weather.tools;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;

public class InfoWindowRefresher implements Callback {
    private Marker markerToRefresh = null;

    public InfoWindowRefresher(Marker markerToRefresh) {
        this.markerToRefresh = markerToRefresh;
    }

    @Override
    public void onSuccess() {
        if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {
            markerToRefresh.hideInfoWindow();
            markerToRefresh.showInfoWindow();
        }
    }

    @Override
    public void onError() {
    }
}

