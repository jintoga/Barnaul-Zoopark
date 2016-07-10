package com.dat.barnaulzoopark.ui.animals.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.ViewHolder> {

    private List<Photo> data = new ArrayList<>();
    private AnimalsAdapterListener listener;

    public AnimalsAdapter(AnimalsAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data.get(position) != null) {
            final Photo photo = data.get(position);
            holder.bindData(photo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPhotoSelected(photo, holder.getAdapterPosition());
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

    public void setData(List<Photo> photos) {
        data.clear();
        data.addAll(photos);
        notifyDataSetChanged();
    }

    public interface AnimalsAdapterListener {
        void onPhotoSelected(@NonNull Photo photo, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final Photo photo) {
            thumbnail.setImageURI(Uri.parse(photo.getUrl()));
        }
    }
}
