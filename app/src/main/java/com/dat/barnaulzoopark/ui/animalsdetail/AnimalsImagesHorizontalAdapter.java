package com.dat.barnaulzoopark.ui.animalsdetail;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 7/8/2016.
 */
public class AnimalsImagesHorizontalAdapter
    extends RecyclerView.Adapter<AnimalsImagesHorizontalAdapter.ViewHolder> {
    private List<String> data = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animals_detail_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data.get(position) != null) {
            final String photoUrl = data.get(position);
            holder.bindData(photoUrl);
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

    public void setData(List<String> animalDatas) {
        data.clear();
        data.addAll(animalDatas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final String photoUrl) {
            thumbnail.setImageURI(Uri.parse(photoUrl));
        }
    }
}
