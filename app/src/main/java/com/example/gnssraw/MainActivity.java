package com.example.gnssraw;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gnssraw.ui.main.SectionsPagerAdapter;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int LOCATION_REQUEST_ID = 1;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        checkPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.serversetterlogger){
            setServerLoggerStatus(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            switch (requestCode) {
                case LOCATION_REQUEST_ID:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    }
            }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    public void checkPermissions(){

        for(String permission : REQUIRED_PERMISSIONS){
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);

            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                    showExplanation("Permessi Negati", "Permessi Negati", permission, LOCATION_REQUEST_ID);
                }else{
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, LOCATION_REQUEST_ID);
                }
            }else{
                Toast.makeText(this,"Permessi Garantiti ", Toast.LENGTH_SHORT).show();
            }
        }
    }

  /*  private void showPopup(final Activity context) {
        int popupWidth = 500;
        int popupHeight = 500;

        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.acquisition_rate_popup, viewGroup);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);
        popup.showAtLocation(layout, Gravity.CENTER, 0,  0);
        Button close = (Button) layout.findViewById(R.id.set_ar_button);
        final TextView setterAR = layout.findViewById(R.id.number_for_AR);
        setterAR.setText("");
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RawFragment temp = (RawFragment) sectionsPagerAdapter.getItem(0);
                Monitor aux = temp.getMonitor();
                try {
                    int i = Integer.parseInt(setterAR.getText().toString());
                    aux.setRequestTime(i);
                    Toast.makeText(getApplicationContext(),""+aux.PrintReQuestTime()/1000+" Seconds", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"ERROR "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                popup.dismiss();
            }
        });
    }*/

    public void setServerLoggerStatus(MenuItem item){
        RawFragment temp = (RawFragment) sectionsPagerAdapter.getItem(0);
        if(!item.isChecked()){
            temp.ServerConnect();
            item.setChecked(true);
        }else{
            temp.ServerDisconnect();
            item.setChecked(false);
        }
    }
}