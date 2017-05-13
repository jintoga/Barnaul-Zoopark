package com.dat.barnaulzoopark.ui.newsdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 2/7/2017.
 */

public class NewsDetailPhotosAdapter
    extends RecyclerView.Adapter<NewsDetailPhotosAdapter.ViewHolder> {
    private List<String> data = new ArrayList<>();
    private ItemClickListener itemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_news_detail_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data.get(position) != null) {
            holder.bindData(data.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClicked(holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<String> photoUrls) {
        data.clear();
        data.addAll(photoUrls);
        notifyDataSetChanged();
    }

    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    interface ItemClickListener {
        void onItemClicked(int adapterPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photo)
        protected ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(@NonNull final String url) {
            Glide.with(itemView.getContext()).load(url).centerCrop().into(photo);
        }
    }
}
