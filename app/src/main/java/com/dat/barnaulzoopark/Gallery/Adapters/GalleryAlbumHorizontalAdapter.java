package com.dat.barnaulzoopark.Gallery.Adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dat.barnaulzoopark.Gallery.Model.GalleryAlbum;
import com.dat.barnaulzoopark.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DAT on 07-Feb-16.
 */
public class GalleryAlbumHorizontalAdapter extends RecyclerView.Adapter<GalleryAlbumHorizontalAdapter.ViewHolder> {

    private ArrayList<GalleryAlbum> data;
    private Context context;

    public GalleryAlbumHorizontalAdapter(ArrayList<GalleryAlbum> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_horizontal_gallery_album_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            GalleryAlbum galleryAlbum = data.get(position);
            holder.albumItemAdapter = new AlbumItemAdapter(context, galleryAlbum.getUrls());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerViewAlbumItem.setLayoutManager(layoutManager);
            holder.recyclerViewAlbumItem.setAdapter(holder.albumItemAdapter);

            holder.textViewTitle.setText(galleryAlbum.getName());
            holder.textViewDate.setText(galleryAlbum.getDate());
            holder.textViewCount.setText(galleryAlbum.getUrls().length + "");
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.recyclerViewAlbumItem)
        RecyclerView recyclerViewAlbumItem;
        AlbumItemAdapter albumItemAdapter;

        @InjectView(R.id.textViewTitle)
        TextView textViewTitle;
        @InjectView(R.id.textViewDate)
        TextView textViewDate;
        @InjectView(R.id.textViewCount)
        TextView textViewCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
