package com.example.gnssraw;


import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import org.jeromq.ZMQ;


//to be done
public class ServerLogger implements IListener{


    private ZMQ.Context context;
    private ZMQ.Socket socket;

    public ServerLogger() {
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://130.251.250.204:5555");
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

                for (GnssMeasurement measurement: eventArgs.getMeasurements()) {
                    String measurementStream =
                            String.format(
                                    "Raw,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                                    measurement.getSvid(),
                                    measurement.getTimeOffsetNanos(),
                                    measurement.getState(),
                                    measurement.getReceivedSvTimeNanos(),
                                    measurement.getReceivedSvTimeUncertaintyNanos(),
                                    measurement.getCn0DbHz(),
                                    measurement.getPseudorangeRateMetersPerSecond(),
                                    measurement.getPseudorangeRateUncertaintyMetersPerSecond(),
                                    measurement.getAccumulatedDeltaRangeState(),
                                    measurement.getAccumulatedDeltaRangeMeters(),
                                    measurement.getAccumulatedDeltaRangeUncertaintyMeters(),
                                    measurement.hasCarrierFrequencyHz() ? measurement.getCarrierFrequencyHz() : "",
                                    measurement.hasCarrierCycles() ? measurement.getCarrierCycles() : "",
                                    measurement.hasCarrierPhase() ? measurement.getCarrierPhase() : "",
                                    // se ce l'ha lo inserisce altrimenti inserisce "" in questo campo
                                    measurement.hasCarrierPhaseUncertainty()
                                            ? measurement.getCarrierPhaseUncertainty()
                                            : "",
                                    measurement.getMultipathIndicator(),
                                    measurement.hasSnrInDb() ? measurement.getSnrInDb() : "",
                                    measurement.getConstellationType(),
                                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                            && measurement.hasAutomaticGainControlLevelDb()
                                            ? measurement.getAutomaticGainControlLevelDb()
                                            : "",
                                    measurement.hasCarrierFrequencyHz() ? measurement.getCarrierFrequencyHz() : "");
                    socket.send(measurementStream.getBytes(), 0);
                    System.out.println(new String(socket.recv(0)));
                }
        //socket.close();
        //context.term();
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
