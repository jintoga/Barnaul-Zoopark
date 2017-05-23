package com.dat.barnaulzoopark.ui.videoalbumsdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

class VideoAlbumsDetailAdapter extends RecyclerView.Adapter<VideoAlbumsDetailAdapter.ViewHolder> {

    private List<String> data = new ArrayList<>();
    private GalleryAdapterListener listener;

    VideoAlbumsDetailAdapter(GalleryAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data.get(position) != null) {
            holder.bindData(data.get(position));
            holder.itemView.setOnClickListener(v -> listener.onVideoSelected(data.get(position)));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(Collection<String> videoIds) {
        data.clear();
        data.addAll(videoIds);
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
        void onVideoSelected(String videoId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        protected SimpleDraweeView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final String videoId) {
            String youtubeThumbnail =
                String.format(thumbnail.getContext().getString(R.string.youtube_thumbnail),
                    videoId);
            thumbnail.setImageURI(youtubeThumbnail);
        }
    }
}
