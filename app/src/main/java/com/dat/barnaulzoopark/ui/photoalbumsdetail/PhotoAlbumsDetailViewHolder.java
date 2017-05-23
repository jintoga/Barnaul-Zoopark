package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by DAT on 10-Apr-16.
 */
class PhotoAlbumsDetailViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    protected SimpleDraweeView thumbnail;

    PhotoAlbumsDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final String photoUrl) {
        thumbnail.setImageURI(photoUrl);
    }
}
