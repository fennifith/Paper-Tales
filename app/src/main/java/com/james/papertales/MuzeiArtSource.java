package com.james.papertales;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.apps.muzei.api.Artwork;
import com.james.papertales.data.WallData;

public class MuzeiArtSource extends com.google.android.apps.muzei.api.MuzeiArtSource {

    private Supplier supplier;

    public MuzeiArtSource() {
        super("MuzeiArtSource");
    }

    protected void onUpdate(int reason) {
        if (supplier != null) supplier = (Supplier) getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            new Thread() {
                @Override
                public void run() {
                    if (!supplier.getNetworkResources()) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MuzeiArtSource.this, R.string.download_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            for (WallData data : supplier.getWallpapers()) {
                                for (String url : data.images) {
                                    publishArtwork(new Artwork.Builder()
                                            .imageUri(Uri.parse(url))
                                            .title(data.name)
                                            .byline(data.authorName)
                                            .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                            .build());
                                }
                            }
                        }
                    });
                }
            }.start();
        }
    }
}
