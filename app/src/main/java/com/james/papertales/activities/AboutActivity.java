package com.james.papertales.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.AboutAdapter;
import com.james.papertales.data.AuthorData;
import com.james.papertales.utils.StaticUtils;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    Supplier supplier;

    Toolbar toolbar;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        supplier = (Supplier) getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<AboutAdapter.Item> items = new ArrayList<>();

        if (getResources().getBoolean(R.bool.show_contributors)) {
            items.add(new AboutAdapter.HeaderItem(this, getString(R.string.contributors), null, true, null));
            for (AuthorData author : supplier.getAuthors()) {
                items.add(new AboutAdapter.TextItem(this, author.name, author.description, author.url));
            }
        }

        items.addAll(((Supplier) getApplicationContext()).getAdditionalInfo(this));

        String[] headers = getResources().getStringArray(R.array.namey);
        String[] contents = getResources().getStringArray(R.array.desc);
        String[] urls = getResources().getStringArray(R.array.uri);

        items.add(new AboutAdapter.HeaderItem(this, getString(R.string.libraries), null, true, null));

        for (int i = 0; i < headers.length; i++) {
            items.add(new AboutAdapter.TextItem(this, headers[i], contents[i], urls[i]));
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(new AboutAdapter(this, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_github:
                StaticUtils.launchCustomTabs(this, Uri.parse("https://github.com/TheAndroidMaster/Paper-Tales"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
