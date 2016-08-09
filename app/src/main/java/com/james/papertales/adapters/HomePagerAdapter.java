package com.james.papertales.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.papertales.R;
import com.james.papertales.fragments.FeaturedFragment;
import com.james.papertales.fragments.RecentFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FeaturedFragment();
            case 1:
                return new RecentFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.section_tags);
            case 1:
                return context.getString(R.string.section_latest);
            default:
                return "";
        }
    }
}
