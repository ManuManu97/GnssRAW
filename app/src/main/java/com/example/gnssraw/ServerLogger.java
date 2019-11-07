package com.example.gnssraw;


import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;


//to be done
public class ServerLogger implements IListener{
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
        try (ZContext context = new ZContext()) {
           // System.out.println("Connecting to hello world server");

            //  Socket to talk to server
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5555");

                for (GnssMeasurement measurement: eventArgs.getMeasurements()) {
                    String measurementStream =
                            String.format(
                                    "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
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
                    socket.send(measurementStream.getBytes(ZMQ.CHARSET), 0);
                }


               /* byte[] reply = socket.recv(0);
                System.out.println(
                        "Received " + new String(reply, ZMQ.CHARSET) + " " +
                                requestNbr
                );*/
        }
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
