package com.james.papertales.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.adapters.ListAdapter;
import com.james.papertales.data.AuthorData;
import com.james.papertales.data.WallData;
import com.james.papertales.views.CustomImageView;

import java.util.ArrayList;
import java.util.Random;

public class ListFragment extends Fragment {

    private Supplier supplier;
    private AuthorData author;
    private ArrayList<WallData> walls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallpapers, container, false);
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        CustomImageView header = (CustomImageView) v.findViewById(R.id.header);
        CustomImageView icon = (CustomImageView) v.findViewById(R.id.headerIcon);


        supplier = (Supplier) getContext().getApplicationContext();

        author = supplier.getAuthor(getArguments().getInt("authorId"));
        if (author == null) return null;

        walls = supplier.getWallpapers(author.id);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), metrics.widthPixels > metrics.heightPixels ? 3 : 2));
        recycler.setAdapter(new ListAdapter(getActivity(), walls));

        Random rand = new Random();
        WallData wall = walls.get(rand.nextInt(walls.size()));
        if (wall.images.size() > 0) Glide.with(getContext()).load(wall.images.get(rand.nextInt(wall.images.size()))).into(header);
        if (author.image != null) Glide.with(getContext()).load(author.image).into(icon);



        return v;
    }
}
