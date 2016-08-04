package com.james.papertales.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.james.papertales.data.WallData;
import com.james.papertales.dialogs.ImageDialog;
import com.james.papertales.views.CustomImageView;

public class ImagePagerAdapter extends PagerAdapter {

    Activity activity;
    WallData data;

    public ImagePagerAdapter(Activity activity, WallData data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView image = new CustomImageView(activity);

        image.setBackgroundColor(Color.GRAY);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(activity).load(data.images.get(position)).into(image);

        image.setTag(position);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageDialog(activity).setImage(data.images.get((int) view.getTag())).setWallpaper(data).show();
            }
        });

        container.addView(image);

        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
