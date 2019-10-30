package com.example.gnssraw;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.List;

public class Monitor {

    public static final String TAG = "Monitor";
    private Context myContext;
    public static int REQUEST_TIME = 1000;

    private List<IListener> RawLoggers;

    LocationManager myLocationManager;
    LocationListener myLocationListener =
            new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    for (IListener Logger : RawLoggers) {
                        Logger.onLocationChanged(location);
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    for (IListener Logger : RawLoggers) {
                        Logger.onStatusChanged(s, i, bundle);
                    }
                }

                @Override
                public void onProviderEnabled(String s) {
                    for (IListener Logger : RawLoggers) {
                        Logger.onProviderEnabled(s);
                    }
                }

                @Override
                public void onProviderDisabled(String s) {
                    for (IListener Logger : RawLoggers) {
                        Logger.onProviderDisabled(s);
                    }
                }
            };

    private final GnssMeasurementsEvent.Callback GnssListener = new GnssMeasurementsEvent.Callback() {
        @Override
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
            for (IListener Logger : RawLoggers) {
                Logger.onGnssMeasurementsReceived(eventArgs);
            }
        }

        @Override
        public void onStatusChanged(int status) {
            for (IListener Logger : RawLoggers) {
                Logger.onStatusChanged(status);
            }
        }
    };

    private final GnssNavigationMessage.Callback GnssNavigationMessageListener = new GnssNavigationMessage.Callback() {
        @Override
        public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
            for (IListener Logger : RawLoggers)
                Logger.onGnssNavigationMessageReceived(event);
        }

        @Override
        public void onStatusChanged(int status) {
            for (IListener Logger : RawLoggers)
                Logger.onGnssNavigationMessageStatusChanged(status);
        }
    };

    private final GnssStatus.Callback GnssStatusListener = new GnssStatus.Callback() {
        @Override
        public void onStarted() {

        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onFirstFix(int ttffMillis) {
        }

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            for (IListener Logger : RawLoggers) {
                Logger.onSatelliteStatusChanged(status);
            }
        }
    };

    public Monitor(Context context, IListener... Loggers) {

        this.RawLoggers = Arrays.asList(Loggers);
        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        myContext = context;
    }

    public LocationManager getMyLocationManager() {
        return myLocationManager;
    }

    public void Register() {

        if (ActivityCompat
                .checkSelfPermission(myContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                    .checkSelfPermission(myContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REQUEST_TIME, 0.0f, myLocationListener);
        myLocationManager.registerGnssMeasurementsCallback(GnssListener);
    }

    public void stopRegister(){


        myLocationManager.removeUpdates(myLocationListener);
        myLocationManager.unregisterGnssMeasurementsCallback(GnssListener);
    }
}
