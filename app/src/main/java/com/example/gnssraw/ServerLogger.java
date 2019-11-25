package com.example.gnssraw;


import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;

import org.jeromq.ZMQ;

import java.util.Date;


//to be done
public class ServerLogger implements IListener{


    private ZMQ.Context context;
    private ZMQ.Socket socket;
    String log_date;

    public ServerLogger(String now) {
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://192.168.1.187:5555");
        log_date = now;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {

        String Result = Build.MANUFACTURER + Build.MODEL +log_date.toString()+"#";
        GnssClock clock = eventArgs.getClock();
        String clockStream =
                String.format(
                        "Clk,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s*",
                        SystemClock.elapsedRealtime(),
                        clock.hasLeapSecond() ? clock.getLeapSecond() : "",
                        clock.getTimeNanos(),
                        clock.hasTimeUncertaintyNanos() ? clock.getTimeUncertaintyNanos() : "",
                        clock.getFullBiasNanos(),
                        clock.hasBiasNanos() ? clock.getBiasNanos() : "",
                        clock.hasBiasUncertaintyNanos() ? clock.getBiasUncertaintyNanos() : "",
                        clock.hasDriftNanosPerSecond() ? clock.getDriftNanosPerSecond() : "",
                        clock.hasDriftUncertaintyNanosPerSecond()
                                ? clock.getDriftUncertaintyNanosPerSecond()
                                : "",
                        clock.getHardwareClockDiscontinuityCount(),
                        SystemClock.elapsedRealtime());
        Result += clockStream;

        for (GnssMeasurement measurement : eventArgs.getMeasurements()) {
            String measurementStream =
                    String.format(
                            "Raw,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            measurement.getConstellationType(),
                            measurement.getSvid(),
                            measurement.hasCarrierFrequencyHz() ? measurement.getCarrierFrequencyHz() : "",
                            measurement.getTimeOffsetNanos(),
                            measurement.getState(),
                            measurement.getReceivedSvTimeNanos(),
                            measurement.getReceivedSvTimeUncertaintyNanos(),
                            measurement.getPseudorangeRateMetersPerSecond(),
                            measurement.getPseudorangeRateUncertaintyMetersPerSecond(),
                            measurement.getAccumulatedDeltaRangeState(),
                            measurement.getAccumulatedDeltaRangeMeters(),
                            measurement.getAccumulatedDeltaRangeUncertaintyMeters(),
                            measurement.getCn0DbHz(),
                            measurement.getMultipathIndicator(),
                            measurement.hasSnrInDb() ? measurement.getSnrInDb() : "",
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                    && measurement.hasAutomaticGainControlLevelDb()
                                    ? measurement.getAutomaticGainControlLevelDb()
                                    : "",
                            measurement.getSvid());
            Result += measurementStream;
        }
        socket.send(Result.getBytes(), 0);
        System.out.println(new String(socket.recv(0)));
    }

    @Override
    public void onStatusChanged(int status) {

    }

    @Override
    public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {

    }

    @Override
    public void onGnssNavigationMessageStatusChanged(int status) {

    }

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

    }
}
