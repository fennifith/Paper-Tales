package com.james.papertales.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.papertales.R;
import com.james.papertales.adapters.TagAdapter;

public class TagFragment extends Fragment {

    private GridLayoutManager manager;
    private OnFinishListener onFinishListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tags, container, false);

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);

        manager = new GridLayoutManager(getContext(), 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return manager.getSpanCount();
                else return 1;
            }
        });
        recycler.setLayoutManager(manager);

        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(new TagAdapter(getContext()));

        v.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFinishListener != null) onFinishListener.onFinish();
            }
        });

        return v;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public interface OnFinishListener {
        void onFinish();
    }
}
