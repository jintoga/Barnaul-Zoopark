package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public PhotoGalleryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PhotoGalleryViewHolder.createInParent(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
