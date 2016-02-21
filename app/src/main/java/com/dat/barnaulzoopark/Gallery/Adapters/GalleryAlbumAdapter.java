package com.dat.barnaulzoopark.Gallery.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dat.barnaulzoopark.Gallery.Model.GalleryAlbum;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.SlideShowPicasso.PicassoRemoteBitmapAdapter;
import com.marvinlabs.widget.slideshow.SlideShowAdapter;
import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.TransitionFactory;
import com.marvinlabs.widget.slideshow.playlist.RandomPlayList;
import com.marvinlabs.widget.slideshow.transition.FadeTransitionFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by DAT on 07-Feb-16.
 */
public class GalleryAlbumAdapter extends RecyclerView.Adapter<GalleryAlbumAdapter.ViewHolder> {

    private ArrayList<GalleryAlbum> data;
    private Context context;

    private Integer[] durationPool = {3500, 4500, 5000, 5500, 6000};

    public GalleryAlbumAdapter(ArrayList<GalleryAlbum> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_gallery_album_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            GalleryAlbum galleryAlbum = data.get(position);
            holder.textViewTitle.setText(galleryAlbum.getName());
            holder.textViewDate.setText(galleryAlbum.getDate());
            holder.slideShowAdapter = createPicassoAdapter(galleryAlbum.getUrls());
            holder.slideShowView.setAdapter(holder.slideShowAdapter);
            TransitionFactory transitionFactory = new FadeTransitionFactory();
            RandomPlayList playList = new RandomPlayList();

            List<Integer> list = Arrays.asList(durationPool);
            Collections.shuffle(list);
            int randomDuration = list.get(position);
            playList.setSlideDuration(randomDuration);

            holder.slideShowView.setPlaylist(playList);
            holder.slideShowView.setTransitionFactory(transitionFactory);
            holder.slideShowView.play();
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    private SlideShowAdapter createPicassoAdapter(String[] slideUrls) {
        Picasso.with(context).setLoggingEnabled(true);
        return new PicassoRemoteBitmapAdapter(context, Arrays.asList(slideUrls), 300, 300);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.slideShowView)
        SlideShowView slideShowView;
        SlideShowAdapter slideShowAdapter;
        @InjectView(R.id.textViewTitle)
        TextView textViewTitle;
        @InjectView(R.id.textViewDate)
        TextView textViewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
