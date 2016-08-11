package com.dat.barnaulzoopark.ui.photoalbumsdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Photo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoAlbumsDetailAdapter extends RecyclerView.Adapter<PhotoAlbumsDetailViewHolder> {

    private List<Photo> data = new ArrayList<>();
    private GalleryAdapterListener listener;

    public PhotoAlbumsDetailAdapter(GalleryAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public PhotoAlbumsDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_gallery, parent, false);
        PhotoAlbumsDetailViewHolder viewHolder = new PhotoAlbumsDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoAlbumsDetailViewHolder holder, final int position) {
        if (data.get(position) != null) {
            holder.bindData(data.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPhotoSelected(position);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Photo> photos) {
        data.clear();
        data.addAll(photos);
        notifyDataSetChanged();
    }

    public List<Photo> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface GalleryAdapterListener {
        void onPhotoSelected(int position);
    }
}
