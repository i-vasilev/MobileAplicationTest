package ru.vasilev.testtaskvasilev.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.vasilev.testtaskvasilev.R;
import ru.vasilev.testtaskvasilev.data.IO.IOType;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    public final List<Fragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment fragment = AlbumsFragment.newInstance(IOType.Network);
                fragments.add(fragment);
                return fragment;
            case 1:
                fragment = AlbumsFragment.newInstance(IOType.DB);
                fragments.add(fragment);
                return fragment;
            case 2:
                fragment = new GpsFragment();
                fragments.add(fragment);
                return fragment;

        }
        return new GpsFragment();
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