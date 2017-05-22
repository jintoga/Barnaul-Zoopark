package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 10-Apr-16.
 */
class PhotoAlbumsDetailViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    protected ImageView thumbnail;

    PhotoAlbumsDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final String photoUrl) {
        thumbnail.setImageURI(Uri.parse(photoUrl));
    }
}
