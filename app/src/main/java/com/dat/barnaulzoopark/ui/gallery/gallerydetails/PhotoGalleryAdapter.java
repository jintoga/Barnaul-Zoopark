package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Photo> data = new ArrayList<>();

    public PhotoGalleryAdapter() {
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
        return data.size();
    }
}
