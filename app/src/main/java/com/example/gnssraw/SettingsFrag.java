package com.example.gnssraw;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gnssraw.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.Objects;

public class SettingsFrag extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final int[] SENSOR_TITLES = new int[]{R.string.sensor_one, R.string.sensor_two};
    private SharedPreferences sharedPreferences;
    private EditTextPreference ipAddress;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.settings_preference, rootKey);
        ipAddress = findPreference("ip_address");
        ipAddress.setText("0.0.0.0");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity().getApplicationContext()));
    }


    public CharSequence [] getSensorsTitles() {
        CharSequence [] result = new CharSequence[SENSOR_TITLES.length];
         for (int i = 0; i <SENSOR_TITLES.length; i++)
             result[i] = getString(SENSOR_TITLES[i]);
         return result;
    }

    public String getValues(String x){
        return this.sharedPreferences.getString(x,"non c'èèèèèèèèè xp");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
