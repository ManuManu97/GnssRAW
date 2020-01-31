package com.example.gnssraw.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gnssraw.PlotFragment;
import com.example.gnssraw.R;
import com.example.gnssraw.RawFragment;
import com.example.gnssraw.SettingsFrag;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.raw_tab, R.string.plot_tab, R.string.settings_tab};
    private final Context mContext;
    private RawFragment myRawFragment = new RawFragment();
    private PlotFragment myPlotFragment = new PlotFragment();
    private SettingsFrag mySettingsFragment = new SettingsFrag();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return myRawFragment;
            case 1:
                return myPlotFragment;
            case 2:
                return mySettingsFragment;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}