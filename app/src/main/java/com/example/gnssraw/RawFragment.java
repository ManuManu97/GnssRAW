package com.example.gnssraw;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class RawFragment extends Fragment {

    private TextView mRawTextView;
    private ScrollView mScrollTextView;
    private UILogger myUILogger;
    private FileLogger myFileLogger;
    private Monitor myMonitor;
    private ServerLogger myServerLogger;
    private SensorMonitor mySensorMonitor;
    private Context myContext;
    private MainActivity tempMainActivity;


    public RawFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView =  inflater.inflate(R.layout.fragment_raw, container,false);
        mRawTextView = newView.findViewById(R.id.RawTextView);
        mScrollTextView = newView.findViewById(R.id.RawscrollView);
        myUILogger = new UILogger(mRawTextView, this);
        myFileLogger = new FileLogger(this.getContext());
        myMonitor = new Monitor(Objects.requireNonNull(this.getContext()), myUILogger,myFileLogger);
        mySensorMonitor = new SensorMonitor(this.getContext());
        tempMainActivity = (MainActivity) getActivity();
        Button start = (Button) newView.findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMonitor.Register(tempMainActivity.sensorflag);
                myFileLogger.CreateRAWLoggerFile();
                myFileLogger.CreateFIXLoggerFile();
                myFileLogger.CreateNAVLoggerFile();
            }
        });
        Button stop = (Button) newView.findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMonitor.stopRegister();
            }
        });

        Button clear = (Button) newView.findViewById(R.id.clearButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRawTextView.setText("");
            }
        });
        return newView;
    }


    public synchronized void writeOnTextFragment(final String text) {
        final StringBuilder builder = new StringBuilder();
        builder.append(text).append("\n");

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mRawTextView.append(builder.toString());
                        mScrollTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                mScrollTextView.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                });
    }

    private Monitor getMonitor(){
        return myMonitor;
    }

    boolean ServerConnect(){

//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        Date now = new Date();
        myServerLogger = new ServerLogger();
        if(myServerLogger.checkConnection()) {
            myMonitor.addServerCommunication(myUILogger, myFileLogger, myServerLogger);
            return true;
        } else{
            return false;
        }
    }

    void ServerDisconnect(){
        myServerLogger = null;
        myMonitor.removeServerCommunication(myUILogger,myFileLogger);
    }
}
