package com.example.gnssraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;

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
    private final String SENSOR_DIR_NAME = "Sensor Measurements";
    private final String F_NAME_RAW = "GNSS_log_SENSOR_";
    File myFileSensor;

    public void setMyFileWriterSensor(BufferedWriter myFileWriterSensor) {
        this.myFileWriterSensor = myFileWriterSensor;
    }

    private BufferedWriter myFileWriterSensor;
    private long last_update = 0;
    private Sensor myPressure;
    private Sensor myGyroscope;


    public SensorMonitor(Context myContext) {
        this.myContext = myContext;
        mySensorManager = (SensorManager) this.myContext.getSystemService(Context.SENSOR_SERVICE);
        assert mySensorManager != null;
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        String s = mySensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).toString();
        myGyroscope = mySensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public void writeSensorFile(String date){

        File baseDirectory = makeSensorDirectory(date);
        String fName =  String.format("%s_%s.txt", F_NAME_RAW , date);

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
        setMyFileWriterSensor(currentFileWriter);

    }

    private SensorEventListener mySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            long curTime = System.currentTimeMillis();

            if ((curTime - last_update) >= 1000) {
                last_update = curTime;
                StringBuilder builder = new StringBuilder("SensorEvent ");
                builder.append(sensorEvent.sensor.getName()+": ");
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
        mySensorManager.registerListener(mySensorListener, myAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mySensorManager.registerListener(mySensorListener,myGyroscope,SensorManager.SENSOR_DELAY_FASTEST);
       // mySensorManager.registerListener(mySensorListener, myPressure, 1000);

    }

    public void stopRegister(){
        last_update = 0;
        mySensorManager.unregisterListener(mySensorListener);
    }

    private File makeSensorDirectory(String date){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String path = myContext.getExternalFilesDir(null).getParent().split("Android")[0];
            File rootDirectory = new File(path, DIR_NAME);
            File recordDirectory = new File(rootDirectory.getAbsolutePath(), "Record "+ date);
            File sensorDirectory = new File(recordDirectory.getAbsolutePath(), SENSOR_DIR_NAME);
            if(sensorDirectory.mkdirs()){
                return sensorDirectory;
            }else
                return null;

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.e(this.getClass().getSimpleName(),"Cannot write to external storage.");
            return null;
        } else {
            Log.e(this.getClass().getSimpleName(),"Cannot read external storage.");
            return null ;
        }
    }


}
