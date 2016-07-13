package com.dat.barnaulzoopark.ui.photoandvideo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.photoandvideo.gallerydetails.PhotoGalleryActivity;
import com.dat.barnaulzoopark.ui.photoandvideo.model.PhotoAlbum;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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

    public void setData(List<PhotoAlbum> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            final PhotoAlbum photoAlbum = data.get(position);
            if (photoAlbum != null) {
                Uri imgThumbnail = Uri.parse(photoAlbum.getUrls()[0]);
                holder.thumbnail.setImageURI(imgThumbnail);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        @Bind(R.id.thumbnail)
        protected SimpleDraweeView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
