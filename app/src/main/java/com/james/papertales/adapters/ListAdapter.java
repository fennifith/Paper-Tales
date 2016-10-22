package com.james.papertales.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.james.papertales.R;
import com.james.papertales.Supplier;
import com.james.papertales.activities.WallActivity;
import com.james.papertales.data.WallData;
import com.james.papertales.utils.ImageUtils;
import com.james.papertales.views.CustomImageView;
import com.james.papertales.views.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<WallData> walls;
    private Activity activity;
    private int layoutMode = -1;
    private Integer artistId;
    public final static int LAYOUT_MODE_HORIZONTAL = 1, LAYOUT_MODE_COMPLEX = 2;

    private Supplier supplier;

    public ListAdapter(Activity activity, ArrayList<WallData> walls) {
        this.activity = activity;
        this.walls = walls;
        supplier = (Supplier) activity.getApplicationContext();
    }

    public void setLayoutMode(int mode) {
        layoutMode = mode;
    }

    public void setArtist(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < walls.size()) return 0;
        else return 1;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutMode == LAYOUT_MODE_COMPLEX ? R.layout.layout_item_complex : R.layout.layout_item, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                CustomImageView image = (CustomImageView) holder.v.findViewById(R.id.image);
                image.setImageBitmap(null);

                ((TextView) holder.v.findViewById(R.id.title)).setText(walls.get(position).name);
                ((TextView) holder.v.findViewById(R.id.author)).setText(layoutMode == LAYOUT_MODE_COMPLEX ? walls.get(position).authorName : walls.get(position).date);

                if (layoutMode != LAYOUT_MODE_COMPLEX)
                    holder.v.setBackgroundColor(ImageUtils.muteColor(Color.WHITE, position));

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, WallActivity.class);
                        intent.putExtra("wall", walls.get(holder.getAdapterPosition()));
                        intent.putExtra("up", "Flat");

                        CustomImageView image = (CustomImageView) holder.v.findViewById(R.id.image);

                        Bitmap bitmap = null;
                        if (image.getDrawable() != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            Drawable prev = image.getDrawable();
                            if (prev instanceof TransitionDrawable)
                                prev = ((TransitionDrawable) image.getDrawable()).getDrawable(1);

                            try {
                                bitmap = ImageUtils.drawableToBitmap(prev);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            } catch (Exception e) {
                                e.printStackTrace();
                                activity.startActivity(intent);
                                return;
                            }
                            byte[] b = baos.toByteArray();
                            intent.putExtra("preload", b);
                        }

                        ActivityOptionsCompat options;
                        if (bitmap != null)
                            options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, bitmap, 5, 5);
                        else
                            options = ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                        activity.startActivity(intent, options.toBundle());
                    }
                });

                if (layoutMode == LAYOUT_MODE_HORIZONTAL && image instanceof SquareImageView)
                    ((SquareImageView) image).setOrientation(SquareImageView.HORIZONTAL);

                WallData data = walls.get(holder.getAdapterPosition());
                if (data.images.size() > 0) {
                    Glide.with(activity).load(data.images.get(0)).thumbnail(0.2f).into(image);
                }

                break;
            case 1:
                if (artistId != null) {
                    supplier.getWallpapers(artistId, new Supplier.AsyncListener<ArrayList<WallData>>() {

                        @Override
                        public void onTaskComplete(ArrayList<WallData> value) {
                            walls.addAll(value);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {
                            artistId = null;
                            notifyDataSetChanged();

                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return walls.size() + (artistId == null ? 0 : 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
