package com.james.papertales.utils;

import android.app.Activity;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.james.papertales.R;

public class StaticUtils {

    public static void launchCustomTabs(Activity activity, Uri uri) {
        new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .build()
                .launchUrl(activity, uri);
    }

}
