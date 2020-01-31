package com.example.gnssraw;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gnssraw.R;

import org.xmlpull.v1.XmlPullParser;

public class SettingsFrag extends PreferenceFragmentCompat {

    private static final int[] SENSOR_TITLES = new int[]{R.string.sensor_one, R.string.sensor_two};
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.settings_preference, rootKey);
       /* MultiSelectListPreference multiSelectListPreference = new MultiSelectListPreference(this.getContext());
        multiSelectListPreference.setEntries(getSensorsTitles());
        multiSelectListPreference.setEntryValues(getSensorsTitles());*/
    }

    public CharSequence [] getSensorsTitles() {
        CharSequence [] result = new CharSequence[SENSOR_TITLES.length];
         for (int i = 0; i <SENSOR_TITLES.length; i++)
             result[i] = getString(SENSOR_TITLES[i]);
         return result;
    }

}
