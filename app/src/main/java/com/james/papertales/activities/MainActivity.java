package com.james.papertales.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.james.papertales.BuildConfig;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.data.AuthorData;
import com.james.papertales.fragments.FavFragment;
import com.james.papertales.fragments.HomeFragment;
import com.james.papertales.fragments.ListFragment;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    View fragmentView;
    Fragment fragment;
    Drawer drawer;

    Supplier supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supplier = (Supplier) getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentView = findViewById(R.id.fragment);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_toggle);

        ArrayList<AuthorData> authors = supplier.getAuthors();

        IDrawerItem[] items = new IDrawerItem[authors.size()];

        for (int i = 0; i < authors.size(); i++) {
            items[i] = new SecondaryDrawerItem().withName(authors.get(i).name).withIdentifier(authors.get(i).id).withIcon(R.drawable.ic_image);
        }

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(this)
                        .withTranslucentStatusBar(true)
                        .withCompactStyle(false)
                        .withHeaderBackground(R.mipmap.wpicon)
                        .withProfileImagesClickable(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .addProfiles(
                                new ProfileDrawerItem().withName(getString(R.string.app_name)).withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(ContextCompat.getDrawable(this, R.mipmap.wpicon))
                        )
                        .build())
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(getString(R.string.title_home)).withIdentifier(items.length + 1).withIcon(R.drawable.ic_home),
                        new SecondaryDrawerItem().withName(getString(R.string.title_favorites)).withIdentifier(items.length + 2).withIcon(R.drawable.ic_fav),
                        new DividerDrawerItem()
                )
                .addDrawerItems(items)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(getString(R.string.title_about)).withIdentifier(items.length + 3).withSelectable(false).withIcon(R.drawable.ic_info)
                )
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int max = supplier.getAuthors().size(), id = (int) drawerItem.getIdentifier();

                        if (id > max) {
                            switch (id - max) {
                                case 1:
                                    fragment = new HomeFragment();
                                    toolbar.setTitle(getString(R.string.title_home));
                                    break;
                                case 2:
                                    fragment = new FavFragment();
                                    toolbar.setTitle(getString(R.string.title_favorites));
                                    break;
                                case 3:
                                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                    break;
                            }
                        } else {
                            Bundle args = new Bundle();
                            args.putInt("authorId", id);
                            fragment = new ListFragment();
                            fragment.setArguments(args);

                            toolbar.setTitle(supplier.getAuthors().get(id).name);
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
                        drawer.closeDrawer();
                        return false;
                    }
                }).build();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                setSelection(fragment);
            }
        });

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (fragment != null) {
                setSelection(fragment);
                return;
            }
        }

        fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
    }

    private void setSelection(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            drawer.setSelection(-1);
        } else if (fragment instanceof FavFragment) {
            drawer.setSelection(-2);
        } else if (fragment instanceof ListFragment) {
            drawer.setSelection(fragment.getArguments().getInt("authorId", -1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer();
                break;
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
