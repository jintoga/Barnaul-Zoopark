package com.dat.barnaulzoopark.ui.photoandvideo.adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.TempPhotoAlbum;
import com.dat.barnaulzoopark.ui.photoalbumsdetail.PhotoAlbumsDetailActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 22-Feb-16.
 */
public class PhotoAlbumsAdapter extends RecyclerView.Adapter<PhotoAlbumsAdapter.ViewHolder> {

    private List<TempPhotoAlbum> data;
    private Activity activity;

    public PhotoAlbumsAdapter(List<TempPhotoAlbum> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_albums, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setData(List<TempPhotoAlbum> data) {
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
            final TempPhotoAlbum photoAlbum = data.get(position);
            if (photoAlbum != null) {
                Uri imgThumbnail = Uri.parse(photoAlbum.getUrls()[0]);
                holder.thumbnail.setImageURI(imgThumbnail);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoAlbumsDetailActivity.startActivity(activity, photoAlbum);
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
