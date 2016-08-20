package com.james.papertales.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.fragments.FeaturedFragment;
import com.james.papertales.fragments.RecentFragment;
import com.james.papertales.fragments.TagFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    Context context;
    Supplier supplier;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.supplier = (Supplier) context.getApplicationContext();
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                if (supplier.getSelectedTags().size() == 0) {
                    TagFragment fragment = new TagFragment();
                    fragment.setOnFinishListener(new TagFragment.OnFinishListener() {
                        @Override
                        public void onFinish() {
                            notifyDataSetChanged();
                        }
                    });
                    return fragment;
                } else return new FeaturedFragment();
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
