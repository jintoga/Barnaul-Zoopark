package com.dat.barnaulzoopark.ui.photoalbumsdetail;

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
public class PhotoAlbumsDetailViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;

    public PhotoAlbumsDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final Photo photo) {
        thumbnail.setImageURI(Uri.parse(photo.getUrl()));
    }

}
