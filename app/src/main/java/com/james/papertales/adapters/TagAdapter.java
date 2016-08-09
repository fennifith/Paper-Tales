package com.james.papertales.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.papertales.R;
import com.james.papertales.Supplier;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    Context context;

    Supplier supplier;
    ArrayList<String> tags;

    public TagAdapter(Context context) {
        this.context = context;

        supplier = (Supplier) context.getApplicationContext();
        tags = supplier.getTags();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_large, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View selected = holder.v.findViewById(R.id.selected);

        if (supplier.isSelected(tags.get(position))) {
            animateView(selected, holder.v.getAlpha(), 1f);
        } else {
            animateView(selected, holder.v.getAlpha(), 0f);
        }

        ((TextView) holder.v.findViewById(R.id.letter)).setText(tags.get(position).substring(0, 1).toUpperCase());
        ((TextView) holder.v.findViewById(R.id.title)).setText(tags.get(position).toLowerCase());

        holder.v.setTag(tags.get(position));
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String) view.getTag();
                View selected = view.findViewById(R.id.selected);

                if (supplier.isSelected(tag)) {
                    supplier.deselectTag(tag);
                    animateView(selected, 1f, 0f);
                } else {
                    supplier.selectTag(tag);
                    animateView(selected, 0f, 1f);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    private void animateView(final View v, float... alpha) {
        ValueAnimator animator = ValueAnimator.ofFloat(alpha);
        animator.setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float) valueAnimator.getAnimatedValue();
                v.setAlpha(alpha);

                if (alpha == 0f) v.setVisibility(View.GONE);
                else v.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
