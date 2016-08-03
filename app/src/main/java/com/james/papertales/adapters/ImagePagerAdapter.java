package com.james.papertales.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.papertales.data.WallData;
import com.james.papertales.fragments.ImageFragment;

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    WallData data;

    public ImagePagerAdapter(WallData data, FragmentManager fm) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putParcelable("data", data);
        args.putInt("position", position);

        Fragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.images.size();
    }
}
