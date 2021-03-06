package com.example.gnssraw;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
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
    private int REQUEST_TIME = 1000;
    private SensorMonitor mySensorMonitor;
    private List<IListener> RawLoggers;
    boolean flag = false;
    boolean sensorflag = false;
    boolean serverflag = false;

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
            if(!flag && sensorflag) {
                mySensorMonitor.Register();
                flag = true;
            }
            ServerLogger temp = (ServerLogger) getServerLogger();
            if(temp != null && !serverflag){
                temp.setDate();
                serverflag = true;
            }
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

    public Monitor(Context context, SensorMonitor mySensorMonitor, IListener... Loggers) {
        myContext = context;
        this.RawLoggers = Arrays.asList(Loggers);
        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.mySensorMonitor = mySensorMonitor;
    }

    public void Register(boolean flag) {

        if (ActivityCompat
                .checkSelfPermission(myContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                    .checkSelfPermission(myContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.sensorflag = flag;

        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REQUEST_TIME, 0.0f, myLocationListener);
        myLocationManager.registerGnssMeasurementsCallback(GnssListener);
        myLocationManager.registerGnssNavigationMessageCallback(GnssNavigationMessageListener);
    }

    public void stopRegister(){

        myLocationManager.removeUpdates(myLocationListener);
        myLocationManager.unregisterGnssMeasurementsCallback(GnssListener);
        myLocationManager.unregisterGnssNavigationMessageCallback(GnssNavigationMessageListener);
        mySensorMonitor.stopRegister();
        flag = false;
        serverflag = false;
    }

    public void addServerCommunication(IListener... loggers){

        this.RawLoggers = Arrays.asList(loggers);
    }

    public void removeServerCommunication(IListener... loggers){
        this.RawLoggers = Arrays.asList(loggers);
    }

    public IListener getServerLogger(){
//        for (IListener list: RawLoggers) {
//            if(list.equals("ServerLogger")){
//                return list;
//            }
//        }
        if(RawLoggers.size() == 3){
            return RawLoggers.get(2);
        }
        return null;
    }

}
