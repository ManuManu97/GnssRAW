package com.example.gnssraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GnssClock;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger implements IListener {


    File myFile;
    BufferedWriter myFileWriter;
    private Context myContext;
    private final String DIR_NAME = "GnssManu";
    private final String F_NAME = "RawManu";
    private static final String COMMENT = "# ";
    private static final char RECORD_DELIMITER = ',';
    private static final String VERSION_TAG = "Version: ";

    public FileLogger(Context context){
        myContext = context;
    }

    public void CreateLoggerFile(){
        File baseDirectory;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), DIR_NAME); // Non per forza SD, che android vede come esterna(non privata)
            baseDirectory.mkdirs(); // crea anche cartelle a cascata
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.e(this.getClass().getSimpleName(),"Cannot write to external storage.");
            return;
        } else {
            Log.e(this.getClass().getSimpleName(),"Cannot read external storage.");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fName =  String.format("%s_%s.csv", F_NAME , formatter.format(now));

        File curFile = new File(baseDirectory, fName);

        String currentFilePath = curFile.getAbsolutePath();
        BufferedWriter currentFileWriter;
        try {
            currentFileWriter = new BufferedWriter(new FileWriter(curFile));
        } catch (Exception e) {
            System.out.println("Could not open file: " + currentFilePath);
            return;
        }
        try {
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write("Header Description:");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(VERSION_TAG);
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String fileVersion =
                    myContext.getString(R.string.version_of_app)
                            + " Platform: "
                            + Build.VERSION.RELEASE
                            + " "
                            + "Manufacturer: "
                            + manufacturer
                            + " "
                            + "Model: "
                            + model;
            currentFileWriter.write(fileVersion);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(
                    "Raw,ElapsedRealtimeMillis,TimeNanos,LeapSecond,TimeUncertaintyNanos,FullBiasNanos,"
                            + "BiasNanos,BiasUncertaintyNanos,DriftNanosPerSecond,DriftUncertaintyNanosPerSecond,"
                            + "HardwareClockDiscontinuityCount,Svid,TimeOffsetNanos,State,ReceivedSvTimeNanos,"
                            + "ReceivedSvTimeUncertaintyNanos,Cn0DbHz,PseudorangeRateMetersPerSecond,"
                            + "PseudorangeRateUncertaintyMetersPerSecond,"
                            + "AccumulatedDeltaRangeState,AccumulatedDeltaRangeMeters,"
                            + "AccumulatedDeltaRangeUncertaintyMeters,CarrierFrequencyHz,CarrierCycles,"
                            + "CarrierPhase,CarrierPhaseUncertainty,MultipathIndicator,SnrInDb,"
                            + "ConstellationType,AgcDb,CarrierFrequencyHz");
            currentFileWriter.newLine();
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(
                    "Fix,Provider,Latitude,Longitude,Altitude,Speed,Accuracy,(UTC)TimeInMs");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write("Nav,Svid,Type,Status,MessageId,Sub-messageId,Data(Bytes)");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();

        myFile = curFile;
        myFileWriter = currentFileWriter;

            myFileWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(myContext, "File opened: " + currentFilePath, Toast.LENGTH_SHORT).show();

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
        if (myFileWriter == null) {
            return;
        }
        GnssClock gnssClock = eventArgs.getClock();
        for (GnssMeasurement measurement : eventArgs.getMeasurements()) {
            try {
                writeGnssMeasurementToFile(gnssClock, measurement);
            } catch (IOException e) {
            }
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

    private void writeGnssMeasurementToFile(GnssClock clock, GnssMeasurement measurement)
            throws IOException {
        String clockStream =
                String.format(
                        "Raw,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        SystemClock.elapsedRealtime(),
                        clock.getTimeNanos(),
                        clock.hasLeapSecond() ? clock.getLeapSecond() : "",
                        clock.hasTimeUncertaintyNanos() ? clock.getTimeUncertaintyNanos() : "",
                        clock.getFullBiasNanos(),
                        clock.hasBiasNanos() ? clock.getBiasNanos() : "",
                        clock.hasBiasUncertaintyNanos() ? clock.getBiasUncertaintyNanos() : "",
                        clock.hasDriftNanosPerSecond() ? clock.getDriftNanosPerSecond() : "",
                        clock.hasDriftUncertaintyNanosPerSecond()
                                ? clock.getDriftUncertaintyNanosPerSecond()
                                : "",
                        clock.getHardwareClockDiscontinuityCount() + ",");
        myFileWriter.write(clockStream);

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
        myFileWriter.write(measurementStream);
        myFileWriter.newLine();
        myFileWriter.flush();
    }

}
