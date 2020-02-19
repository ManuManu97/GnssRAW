package com.example.gnssraw;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.example.gnssraw.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int LOCATION_REQUEST_ID = 1;
    private SectionsPagerAdapter sectionsPagerAdapter;
    public boolean sensorflag = false;
    private SharedPreferences sharedPreferences;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.serversetterlogger:
                setServerLoggerStatus(item);
                return true;

            case R.id.sensorsetterlogger:
                setSensorLoggerStatus(item);
                return true;

            case R.id.settings_option:
                SettingsFrag sett = (SettingsFrag) sectionsPagerAdapter.getItem(2);
                /*startActivity(new Intent(this, SettingsActivity.class));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

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
                //Toast.makeText(this,"Permessi Garantiti ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setServerLoggerStatus(MenuItem item){
        RawFragment temp = (RawFragment) sectionsPagerAdapter.getItem(0);
        if(!item.isChecked()){
            if(temp.ServerConnect()){
                item.setChecked(true);
            }else{
                Toast.makeText(MainActivity.this, "Impossibile connettersi al server.", Toast.LENGTH_SHORT).show();
            }

        }else{
            temp.ServerDisconnect();
            item.setChecked(false);
        }
    }

    public void setSensorLoggerStatus(MenuItem item){
        RawFragment temp = (RawFragment) sectionsPagerAdapter.getItem(0);
        if(!item.isChecked()){
            sensorflag = true;
            item.setChecked(true);
        }else{
            sensorflag = false;
            item.setChecked(false);
        }
    }




}