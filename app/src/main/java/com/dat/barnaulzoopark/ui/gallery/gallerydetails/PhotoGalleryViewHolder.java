package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import com.squareup.picasso.Picasso;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    public ImageView thumbnail;
    private Context context;

    public PhotoGalleryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
    }

    public void bindData(final Photo photo) {
        thumbnail.post(new Runnable() {
            @Override public void run() {
                Picasso.with(context)
                        .load(photo.getUrl())
                        .resize(thumbnail.getWidth(), thumbnail.getMaxHeight())
                        .into(thumbnail);
            }
        });
    }

    public static PhotoGalleryViewHolder createInParent(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_gallery_item, parent, false);
        return new PhotoGalleryViewHolder(view);
    }
}
