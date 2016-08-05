package com.james.papertales.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.ListAdapter;
import com.james.papertales.data.AuthorData;
import com.james.papertales.data.WallData;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private Supplier supplier;
    private AuthorData author;
    private ArrayList<WallData> walls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallpapers, container, false);
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);

        supplier = (Supplier) getContext().getApplicationContext();

        author = supplier.getAuthor(getArguments().getInt("authorId"));
        if (author == null) return null;

        walls = supplier.getWallpapers(author.id);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), metrics.widthPixels > metrics.heightPixels ? 3 : 2));
        recycler.setAdapter(new ListAdapter(getActivity(), walls));

        return v;
    }
}
