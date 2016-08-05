package com.james.papertales.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.AboutAdapter;
import com.james.papertales.data.HeaderListData;
import com.james.papertales.data.TextListData;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Parcelable> items = new ArrayList<>();

        if (getResources().getBoolean(R.bool.show_contributors)) {
            items.add(new HeaderListData(getString(R.string.contributors), null, true, null));
            items.addAll(((Supplier) getApplicationContext()).getAuthors());
        }

        items.addAll(((Supplier) getApplicationContext()).getAdditionalInfo());

        String[] headers = getResources().getStringArray(R.array.namey);
        String[] contents = getResources().getStringArray(R.array.desc);
        String[] urls = getResources().getStringArray(R.array.uri);

        items.add(new HeaderListData(getString(R.string.libraries), null, true, null));

        for (int i = 0; i < headers.length; i++) {
            items.add(new TextListData(headers[i], contents[i], urls[i]));
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(new AboutAdapter(this, items));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
