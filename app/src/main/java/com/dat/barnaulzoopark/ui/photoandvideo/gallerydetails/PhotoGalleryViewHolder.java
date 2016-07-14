package com.dat.barnaulzoopark.ui.photoandvideo.gallerydetails;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Photo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;

    public PhotoGalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final Photo photo) {
        thumbnail.setImageURI(Uri.parse(photo.getUrl()));
    }

}
