package com.dat.barnaulzoopark.ui.bloganimal;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/14/2017.
 */

class BlogAnimalAdapter extends RecyclerView.Adapter<BlogAnimalAdapter.ViewHolder> {

    private List<BlogAnimal> data = new ArrayList<>();
    private ClickListener clickListener;

    BlogAnimalAdapter(@NonNull ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_blog_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            final BlogAnimal blogAnimal = data.get(position);
            if (blogAnimal != null) {
                holder.bindData(blogAnimal, clickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<BlogAnimal> result) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.clear();
        this.data.addAll(result);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        SimpleDraweeView thumbnail;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.time)
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(@NonNull BlogAnimal blogAnimal, @NonNull ClickListener clickListener) {
            title.setText(blogAnimal.getTitle());
            description.setText(blogAnimal.getDescription());
            time.setText(ConverterUtils.epochToString(blogAnimal.getTime()));
            Uri uri;
            if (blogAnimal.getThumbnail() != null && !"".equals(blogAnimal.getThumbnail())) {
                uri = Uri.parse(blogAnimal.getThumbnail());
            } else {
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
            }
            thumbnail.setImageURI(uri);
            itemView.setOnClickListener(v -> clickListener.onItemClicked(blogAnimal));
        }
    }

    interface ClickListener {
        void onItemClicked(@NonNull BlogAnimal blogAnimal);
    }
}
