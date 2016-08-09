package com.james.papertales.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.ListAdapter;
import com.james.papertales.data.WallData;
import com.james.papertales.dialogs.TagDialog;

import java.util.ArrayList;

public class FeaturedFragment extends Fragment {

    View v;
    Supplier supplier;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_featured, container, false);

        supplier = (Supplier) getContext().getApplicationContext();

        if (!loadWallpapers()) {
            v.findViewById(R.id.empty).setVisibility(View.VISIBLE);

            showDialog();
        }

        v.findViewById(R.id.categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return v;
    }

    private boolean loadWallpapers() {
        ArrayList<WallData> walls = new ArrayList<>();

        for (WallData wall : supplier.getWallpapers()) {
            for (String tag : supplier.getSelectedTags()) {
                if (wall.categories.contains(tag)) {
                    walls.add(wall);
                    break;
                }
            }
        }

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycler.setNestedScrollingEnabled(false);

        ListAdapter adapter = new ListAdapter(getActivity(), walls);
        adapter.setLayoutMode(ListAdapter.LAYOUT_MODE_COMPLEX);
        recycler.setAdapter(adapter);

        return walls.size() > 0;
    }

    private void showDialog() {
        Dialog dialog = new TagDialog(getContext());
        dialog.setTitle(R.string.categories);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (loadWallpapers()) v.findViewById(R.id.empty).setVisibility(View.GONE);
            }
        });
        dialog.show();
    }
}
