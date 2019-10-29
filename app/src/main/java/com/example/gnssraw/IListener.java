package com.example.gnssraw;

import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Bundle;

public interface IListener {


    //LocationListener
    void onLocationChanged(Location location);


    void onStatusChanged(String s, int i, Bundle bundle);


    void onProviderEnabled(String s);


     void onProviderDisabled(String s);

    //GnssMeasurementEvent.Callback
    void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs);


    void onStatusChanged(int status);

    //GnssNavigaionMessage.Callback
    void onGnssNavigationMessageReceived(GnssNavigationMessage event);

    void onGnssNavigationMessageStatusChanged(int status);

    //GnssStatus.Callback
    void onStarted();


    void onStopped();


    void onFirstFix(int ttffMillis);


    void onSatelliteStatusChanged(GnssStatus status);
}
