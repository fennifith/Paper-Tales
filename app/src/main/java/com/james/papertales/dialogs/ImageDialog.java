package com.james.papertales.dialogs;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.data.WallData;
import com.james.papertales.utils.ImageUtils;
import com.james.papertales.views.CustomImageView;

import java.io.IOException;

public class ImageDialog extends AppCompatDialog {

    private Activity activity;
    private Supplier supplier;
    private DownloadReceiver downloadReceiver;

    private String url;
    private WallData data;

    private CustomImageView image;

    public ImageDialog(Activity activity) {
        super(activity, R.style.AppTheme_Dialog);
        this.activity = activity;
        supplier = (Supplier) activity.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);

        image = (CustomImageView) findViewById(R.id.image);

        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data == null || url == null) return;

                downloadReceiver.register();

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    supplier.downloadWallpaper(getContext(), data.name, url);
                else
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8027);
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url != null) supplier.shareWallpaper(getContext(), url);
            }
        });

        findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url == null) return;
                Glide.with(activity).load(url).into(new SimpleTarget<GlideDrawable>() {
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
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SET_WALLPAPER}, 9274);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Toast.makeText(getContext(), R.string.download_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (url != null) Glide.with(getContext()).load(url).into(image);
    }

    public ImageDialog setImage(String url) {
        this.url = url;
        if (image != null) Glide.with(getContext()).load(url).into(image);
        return this;
    }

    public ImageDialog setWallpaper(WallData data) {
        this.data = data;
        return this;
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
