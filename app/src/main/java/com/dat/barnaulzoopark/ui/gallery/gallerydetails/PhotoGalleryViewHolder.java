package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dat.barnaulzoopark.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.thumbnail)
    public ImageView thumbnail;

    public PhotoGalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static PhotoGalleryViewHolder createInParent(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_gallery_item, parent, false);
        return new PhotoGalleryViewHolder(view);
    }
}
