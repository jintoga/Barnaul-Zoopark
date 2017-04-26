package com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters;

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
import com.dat.barnaulzoopark.model.animal.Species;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorAdapter extends RecyclerView.Adapter<CategoryEditorAdapter.ViewHolder> {

    private List<Species> data = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category_editor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data.get(position) != null) {
            final Species species = data.get(position);
            holder.bindData(species);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<Species> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        protected TextView name;
        @Bind(R.id.photo)
        protected SimpleDraweeView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final Species species) {
            name.setText(species.getName());
            if (species.getIcon() != null) {
                photo.setImageURI(species.getIcon());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                photo.setImageURI(uri);
            }
        }
    }
}
