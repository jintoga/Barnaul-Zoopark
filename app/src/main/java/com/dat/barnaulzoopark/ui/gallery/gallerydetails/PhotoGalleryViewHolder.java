package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;
    private Context context;

    public PhotoGalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    public void bindData(final Photo photo) {
        Picasso.with(context)
                .load(photo.getUrl())
                .fit().centerCrop()
                .into(thumbnail);
    }

}
