package com.dat.barnaulzoopark.ui.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.gallerydetails.PhotoGalleryActivity;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 22-Feb-16.
 */
public class PhotoAlbumsAdapter extends RecyclerView.Adapter<PhotoAlbumsAdapter.ViewHolder> {

    private List<PhotoAlbum> data;
    private Context context;

    public PhotoAlbumsAdapter(List<PhotoAlbum> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_albums, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            final PhotoAlbum photoAlbum = data.get(position);
            holder.stackPhotoAlbumView.setData(photoAlbum.getUrls());
            holder.stackPhotoAlbumView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoGalleryActivity.startActivity(context, photoAlbum);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.stackAlbumView)
        protected StackPhotoAlbumView stackPhotoAlbumView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
