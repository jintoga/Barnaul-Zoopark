package com.dat.barnaulzoopark.Gallery.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dat.barnaulzoopark.Gallery.DynamicHeightImageView;
import com.dat.barnaulzoopark.Gallery.Model.GalleryAlbum;
import com.dat.barnaulzoopark.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DAT on 07-Feb-16.
 */
public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.ViewHolder> {
    private Context context;
    private String[] data;

    public AlbumItemAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_album_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null && data.length > 0) {
            /*int[] dimens = {(int) context.getResources().getDimension(R.dimen.big_item),
                    (int) context.getResources().getDimension(R.dimen.norm_item),
                    (int) context.getResources().getDimension(R.dimen.small_item)};
            Random random = new Random();
            int pos = random.nextInt(dimens.length);
            int randomWidth = dimens[pos];

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(randomWidth, (int) context.getResources().getDimension(R.dimen.norm_item));
            holder.imageViewPhoto.setLayoutParams(layoutParams);*/
           /* holder.imageViewPhoto.setMaxWidth(600);
            holder.imageViewPhoto.setMinimumWidth(400);*/
            Picasso.with(context).load(data[position]).fit().centerCrop().into(holder.imageViewPhoto);
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.length;
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements Target {
        @InjectView(R.id.imageViewPhoto)
        DynamicHeightImageView imageViewPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap
            /*float ratio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
            // Set the ratio for the image
            imageViewPhoto.setWidthRatio(1);
            // Load the image into the view
            imageViewPhoto.setMaxWidth(300);
            imageViewPhoto.setMaxWidth(200);
            imageViewPhoto.setImageBitmap(bitmap);*/
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
