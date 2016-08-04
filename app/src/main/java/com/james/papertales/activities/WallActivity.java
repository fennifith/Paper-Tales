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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.ImagePagerAdapter;
import com.james.papertales.data.AuthorData;
import com.james.papertales.data.WallData;
import com.james.papertales.utils.ImageUtils;
import com.james.papertales.views.CustomImageView;
import com.james.papertales.views.PageIndicator;


public class WallActivity extends AppCompatActivity {

    WallData data;
    Supplier supplier;

    Toolbar toolbar;
    ViewPager viewPager;
    PageIndicator indicator;

    TextView date, auth, desc;
    FlexboxLayout categories;
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
        indicator = (PageIndicator) findViewById(R.id.indicator);
        date = (TextView) findViewById(R.id.date);
        auth = (TextView) findViewById(R.id.auth);
        desc = (TextView) findViewById(R.id.description);
        categories = (FlexboxLayout) findViewById(R.id.categories);
        fav = (CustomImageView) findViewById(R.id.fav);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setAdapter(new ImagePagerAdapter(this, data));
        indicator.setViewPager(viewPager);

        if (data.categories.size() > 0) {
            categories.setVisibility(View.VISIBLE);

            for (String category : data.categories) {
                View v = LayoutInflater.from(this).inflate(R.layout.layout_category, null);
                ((TextView) v.findViewById(R.id.title)).setText(category);
                categories.addView(v);
            }
        }

        date.setText(data.date);
        auth.setText(data.authorName);
        desc.setText(Html.fromHtml(data.desc));
        desc.setMovementMethod(new LinkMovementMethod());

        AuthorData author = supplier.getAuthor(data.authorId);
        if (author != null) Glide.with(this).load(author.image).into((CustomImageView) findViewById(R.id.profile));

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
