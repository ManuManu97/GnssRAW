package com.example.gnssraw;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorAdditionalInfo;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorMonitor {

    public static final String TAG = "SensorMonitor";
    private Context myContext;
    private SensorManager mySensorManager;
    private Sensor myAccelerometer;
    private final String DIR_NAME = "GnssRAW";
    private final String F_NAME_RAW = "GNSS_log_SENSOR_";
    File myFileSensor;
    BufferedWriter myFileWriterSensor;
    private long last_update = 0;


    public SensorMonitor(Context myContext) {
        this.myContext = myContext;
        mySensorManager = (SensorManager) this.myContext.getSystemService(Context.SENSOR_SERVICE);
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void writeSensorFile(){
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
            currentFileWriter.write("#SensorEvent:timestamp, accuracy, A-Gx(x-axis), A-Gx(y-axis), A-Gx(z-axis)");
            currentFileWriter.newLine();
            currentFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myFileSensor = curFile;
        myFileWriterSensor = currentFileWriter;


    }

    SensorEventListener mySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            long curTime = System.currentTimeMillis();

            if ((curTime - last_update) >= 1000) {
                last_update = curTime;
                StringBuilder builder = new StringBuilder("SensorEvent:");
                builder.append(sensorEvent.timestamp);
                builder.append(",");
                builder.append(sensorEvent.accuracy);
                for (float value : sensorEvent.values) {
                    builder.append(",");
                    builder.append(value);
                }

                try {
                    myFileWriterSensor.write(builder.toString());
                    myFileWriterSensor.newLine();
                    myFileWriterSensor.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public void Register() {
        writeSensorFile();
        mySensorManager.registerListener(mySensorListener, myAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopRegister(){
        last_update = 0;
        mySensorManager.unregisterListener(mySensorListener);
    }

}
