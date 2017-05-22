package com.dat.barnaulzoopark.ui.photoandvideo.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.photoalbumsdetail.PhotoAlbumsDetailActivity;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 22-Feb-16.
 */
public class PhotoAlbumsAdapter
    extends FirebaseRecyclerAdapter<PhotoAlbum, PhotoAlbumsAdapter.ViewHolder> {

    private Activity activity;

    /**
     * @param modelClass Firebase will marshall the data at a location into an instance of a class
     * that
     * you provide
     * @param modelLayout This is the layout used to represent a single item in the list. You will
     * be
     * responsible for populating an
     * instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance
     * modelLayout.
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a
     * location,
     * using some
     * combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public PhotoAlbumsAdapter(Class<PhotoAlbum> modelClass, int modelLayout,
        Class<ViewHolder> viewHolderClass, Query ref, Activity activity) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.activity = activity;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, PhotoAlbum photoAlbum, int position) {
        if (photoAlbum != null) {
            Uri imgThumbnail = Uri.parse(photoAlbum.getPhotos().values().iterator().next());
            viewHolder.thumbnail.setImageURI(imgThumbnail);
            String name = String.format("%s\n%s", photoAlbum.getName(),
                ConverterUtils.getConvertedTime(photoAlbum.getTime()));
            viewHolder.albumName.setText(name);
            viewHolder.photosCount.setText(String.valueOf(photoAlbum.getPhotos().size()));
        }
        viewHolder.itemView.setOnClickListener(
            v -> PhotoAlbumsDetailActivity.startActivity(activity, photoAlbum));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.thumbnail)
        protected SimpleDraweeView thumbnail;
        @Bind(R.id.albumName)
        TextView albumName;
        @Bind(R.id.photosCount)
        TextView photosCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
