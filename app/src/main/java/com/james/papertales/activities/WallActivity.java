package com.james.papertales.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.ImagePagerAdapter;
import com.james.papertales.data.WallData;
import com.james.papertales.utils.ImageUtils;
import com.james.papertales.views.CustomImageView;


public class WallActivity extends AppCompatActivity {

    WallData data;
    Supplier supplier;

    Toolbar toolbar;
    ViewPager viewPager;
    TextView name, auth, desc;
    LinearLayout bg;
    CustomImageView fav;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        supplier = (Supplier) getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        data = getIntent().getParcelableExtra("wall");
        setTitle(data.name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        name = (TextView) findViewById(R.id.wall);
        auth = (TextView) findViewById(R.id.auth);
        desc = (TextView) findViewById(R.id.description);
        bg = (LinearLayout) findViewById(R.id.back);
        fav = (CustomImageView) findViewById(R.id.fav);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setAdapter(new ImagePagerAdapter(data, getSupportFragmentManager()));

        name.setText(getTitle());
        auth.setText(data.authorName);
        desc.setText(Html.fromHtml(data.desc));
        desc.setMovementMethod(new LinkMovementMethod());

        findViewById(R.id.person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(supplier.getAuthors().get(data.authorId).url)));
            }
        });

        fav.setImageDrawable(ImageUtils.getVectorDrawable(this, prefs.getBoolean(data.name + data.authorId, false) ? R.drawable.fav_added : R.drawable.fav_add));
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFav = prefs.getBoolean(data.name + data.authorId, false);
                fav.setImageDrawable(ImageUtils.getVectorDrawable(WallActivity.this, !isFav ? R.drawable.fav_added : R.drawable.fav_add));
                prefs.edit().putBoolean(data.name + data.authorId, !isFav).apply();
            }
        });
    }
}
