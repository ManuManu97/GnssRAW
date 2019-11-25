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


    private File myFileRaw, myFileFix, myFileNav;
    private BufferedWriter myFileWriterRaw, myFileWriterFix, myFileWriterNav;
    private Context myContext;
    private final String DIR_NAME = "GnssRAW";
    private final String F_NAME_RAW = "GNSS_log_RAW_";
    private final String F_NAME_FIX = "GNSS_log_FIX_";
    private final String F_NAME_NAV = "GNSS_log_NAV_";
    private static final String COMMENT = "# ";
    private static final String VERSION_TAG = "Version: ";

    public FileLogger(Context context){
        myContext = context;
    }

    public void CreateRAWLoggerFile(){
        File baseDirectory;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), DIR_NAME);
            baseDirectory.mkdirs();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.e(this.getClass().getSimpleName(),"Cannot write to external storage.");
            return;
        } else {
            Log.e(this.getClass().getSimpleName(),"Cannot read external storage.");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fName =  String.format("%s_%s.txt", F_NAME_RAW , formatter.format(now));

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
            currentFileWriter.write("Header: GnssRAW.apk");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(VERSION_TAG + "0.1");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String platform =" Platform: "
                            + Build.VERSION.RELEASE
                            + " "
                            + "Manufacturer: "
                            + manufacturer
                            + " "
                            + "Model: "
                            + model;
            currentFileWriter.write(platform);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(
                    "Clk,ElapsedRealtimeMillis,LeapSecond,TimeNanos,TimeUncertaintyNanos,FullBiasNanos,"
                            + "BiasNanos,BiasUncertaintyNanos,DriftNanosPerSecond,DriftUncertaintyNanosPerSecond,"
                            + "HardwareClockDiscontinuityCount,ElapsedRealtimeMillis");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write("Raw,ConstellationType,Svid,CarrierFrequencyHz,TimeOffsetNanos,State,ReceivedSvTimeNanos,"
                                    +"ReceivedSvTimeUncertaintyNanos,PseudorangeRateMetersPerSecond,PseudorangeRateUncertaintyMetersPerSecond,"
                                    +"AccumulatedDeltaRangeState,AccumulatedDeltaRangeMeters,AccumulatedDeltaRangeUncertaintyMeters,Cn0DbHz,MultipathIndicator,SnrInDb,Svid");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();

            myFileRaw = curFile;
            myFileWriterRaw = currentFileWriter;

            myFileWriterRaw.newLine();
            myFileWriterRaw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(myContext, "File opened: " + currentFilePath, Toast.LENGTH_SHORT).show();

    }

    public void CreateFIXLoggerFile(){
        File baseDirectory;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), DIR_NAME);
            baseDirectory.mkdirs();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.e(this.getClass().getSimpleName(),"Cannot write to external storage.");
            return;
        } else {
            Log.e(this.getClass().getSimpleName(),"Cannot read external storage.");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fName =  String.format("%s_%s.txt", F_NAME_FIX , formatter.format(now));

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
            currentFileWriter.write("Header: GnssRAW.apk");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(VERSION_TAG + "0.1");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String platform =" Platform: "
                    + Build.VERSION.RELEASE
                    + " "
                    + "Manufacturer: "
                    + manufacturer
                    + " "
                    + "Model: "
                    + model;
            currentFileWriter.write(platform);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(
                    "Fix,Provider,Latitude,Longitude,Altitude,Speed,Accuracy,(UTC)TimeInMs");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();

            myFileFix = curFile;
            myFileWriterFix = currentFileWriter;

            myFileWriterFix.newLine();
            myFileWriterFix.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(myContext, "File opened: " + currentFilePath, Toast.LENGTH_SHORT).show();

    }

    public void CreateNAVLoggerFile(){
        File baseDirectory;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            baseDirectory = new File(Environment.getExternalStorageDirectory(), DIR_NAME);
            baseDirectory.mkdirs();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.e(this.getClass().getSimpleName(),"Cannot write to external storage.");
            return;
        } else {
            Log.e(this.getClass().getSimpleName(),"Cannot read external storage.");
            return;
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fName =  String.format("%s_%s.txt", F_NAME_NAV , formatter.format(now));

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
            currentFileWriter.write("Header: GnssRAW.apk");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write(VERSION_TAG + "0.1");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String platform =" Platform: "
                    + Build.VERSION.RELEASE
                    + " "
                    + "Manufacturer: "
                    + manufacturer
                    + " "
                    + "Model: "
                    + model;
            currentFileWriter.write(platform);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.write("Nav,Svid,Type,Status,MessageId,Sub-messageId,Data(Bytes)");
            currentFileWriter.newLine();
            currentFileWriter.write(COMMENT);
            currentFileWriter.newLine();

            myFileNav = curFile;
            myFileWriterNav = currentFileWriter;

            myFileWriterNav.newLine();
            myFileWriterNav.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
       // Toast.makeText(myContext, "File opened: " + currentFilePath, Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onLocationChanged(Location location) {
        if (myFileWriterRaw == null) {
            return;
        }
        String fixStream;
        fixStream = String.format(
                "Fix,%s,%f,%f,%f,%f,%f,%d",
                location.getProvider(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getSpeed(),
                location.getAccuracy(),
                location.getTime());
        try {
            myFileWriterFix.write(fixStream);
            myFileWriterFix.newLine();
            myFileWriterFix.flush();
        } catch (IOException e) {
        }

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
        if (myFileWriterRaw == null) {
            return;
        }
        GnssClock gnssClock = eventArgs.getClock();
            try {
                writeGnssMeasurementToFile(gnssClock, eventArgs);
            } catch (IOException e) {
            }
    }

    @Override
    public void onStatusChanged(int status) {

    }

    @Override
    public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
        if (myFileWriterNav == null) {
            return;
        }
        StringBuilder builder = new StringBuilder("Nav");
        builder.append(",");
        builder.append(event.getSvid());
        builder.append(",");
        builder.append(event.getType());
        builder.append(",");

        int status = event.getStatus();
        builder.append(status);
        builder.append(",");
        builder.append(event.getMessageId());
        builder.append(",");
        builder.append(event.getSubmessageId());
        byte[] data = event.getData();
        for (byte word : data) {
            builder.append(",");
            builder.append(word);
        }
        try {
            myFileWriterNav.write(builder.toString());
            myFileWriterNav.newLine();
            myFileWriterNav.flush();
        } catch (IOException e) {
        }
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

    private void writeGnssMeasurementToFile(GnssClock clock, GnssMeasurementsEvent measurements)
            throws IOException {
        String clockStream =
                String.format(
                        "Clk,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
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
        myFileWriterRaw.write(clockStream);
        myFileWriterRaw.newLine();

        for (GnssMeasurement measurement : measurements.getMeasurements()) {
            String measurementStream =
                    String.format(
                            "Raw,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
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
            myFileWriterRaw.write(measurementStream);
            myFileWriterRaw.newLine();
        }

        myFileWriterRaw.flush();
    }

}
