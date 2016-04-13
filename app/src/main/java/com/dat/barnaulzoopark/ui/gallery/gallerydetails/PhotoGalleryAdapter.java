package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryViewHolder> {

    private List<Photo> data = new ArrayList<>();

    public PhotoGalleryAdapter() {
    }

    @Override
    public PhotoGalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_gallery_item, parent, false);
        PhotoGalleryViewHolder viewHolder = new PhotoGalleryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoGalleryViewHolder holder, int position) {
        if (data.get(position) != null) {
            holder.bindData(data.get(position));
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

    @Override
    public int getItemCount() {
        return data.size();
    }
}
