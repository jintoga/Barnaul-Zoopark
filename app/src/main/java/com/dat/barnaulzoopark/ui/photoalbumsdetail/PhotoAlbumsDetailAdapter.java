package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoAlbumsDetailAdapter extends RecyclerView.Adapter<PhotoAlbumsDetailViewHolder> {

    private List<String> data = new ArrayList<>();
    private GalleryAdapterListener listener;

    PhotoAlbumsDetailAdapter(GalleryAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public PhotoAlbumsDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_gallery, parent, false);
        return new PhotoAlbumsDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoAlbumsDetailViewHolder holder, int position) {
        if (data.get(position) != null) {
            holder.bindData(data.get(position));
            holder.itemView.setOnClickListener(v -> listener.onPhotoSelected(position));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(Collection<String> photos) {
        data.clear();
        data.addAll(photos);
        notifyDataSetChanged();
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface GalleryAdapterListener {
        void onPhotoSelected(int position);
    }
}
