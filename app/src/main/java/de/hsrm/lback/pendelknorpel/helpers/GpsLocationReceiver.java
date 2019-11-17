package de.hsrm.lback.pendelknorpel.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsLocationReceiver extends BroadcastReceiver implements LocationListener {

    public GpsLocationReceiver(GpsChangeListener listener) {
        this.listener = listener;
    }

    public interface GpsChangeListener {
        void onGpsChanged(boolean on);
    }

    private GpsChangeListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        listener.onGpsChanged(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        listener.onGpsChanged(false);
    }
}
