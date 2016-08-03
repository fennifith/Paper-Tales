package com.james.papertales.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.utils.ImageUtils;
import com.james.papertales.data.WallData;
import com.james.papertales.views.CustomImageView;

import java.io.IOException;

public class ImageFragment extends Fragment {

    WallData data;
    int position;
    View download, share, apply;

    Supplier supplier;
    DownloadReceiver downloadReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);

        supplier = (Supplier) getContext().getApplicationContext();

        data = getArguments().getParcelable("data");
        position = getArguments().getInt("position", 0);

        download = v.findViewById(R.id.download);
        share = v.findViewById(R.id.share);
        apply = v.findViewById(R.id.apply);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadReceiver.register();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    supplier.downloadWallpaper(getContext(), data.name, data.images.get(position));
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8027);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supplier.shareWallpaper(getContext(), data.images.get(position));
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getActivity()).load(data.images.get(position)).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SET_WALLPAPER) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                WallpaperManager.getInstance(getContext()).setBitmap(ImageUtils.drawableToBitmap(resource));
                                Toast.makeText(getContext(), R.string.set_wallpaper_success, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), R.string.set_wallpaper_failed, Toast.LENGTH_SHORT).show();
                            }
                        } else
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SET_WALLPAPER}, 9274);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Toast.makeText(getContext(), R.string.download_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Glide.with(getContext()).load(data.images.get(position)).into((CustomImageView) v.findViewById(R.id.image));

        setClickable(false);

        return v;
    }

    private void setClickable(boolean clickable) {
        if (download != null) download.setClickable(clickable);
        if (share != null) share.setClickable(clickable);
        if (apply != null) apply.setClickable(clickable);
    }

    @Override
    public void onStop() {
        if (downloadReceiver != null && downloadReceiver.isRegistered()) downloadReceiver.unregister();
        super.onStop();
    }

    private static class DownloadReceiver extends BroadcastReceiver {

        private Context context;
        private boolean isRegistered;

        public DownloadReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            ((Supplier) context.getApplicationContext()).getDownloadedDialog(context, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                }
            }).show();

            unregister();
        }

        public void register() {
            context.registerReceiver(this, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            isRegistered = true;
        }

        public void unregister() {
            context.unregisterReceiver(this);
            isRegistered = false;
        }

        public boolean isRegistered() {
            return isRegistered;
        }
    }
}
