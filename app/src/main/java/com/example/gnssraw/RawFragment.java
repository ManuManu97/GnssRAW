package com.example.gnssraw;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class RawFragment extends Fragment {

    private TextView mRawTextView;
    private ScrollView mScrollTextView;
    private UILogger myUILogger;
    private FileLogger myFileLogger;
    private Monitor myMonitor;

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
        Button start = (Button) newView.findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMonitor.Register();
                myFileLogger.CreateLoggerFile();
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

    public Monitor getMonitor(){
        return myMonitor;
    }


}
