package com.dat.barnaulzoopark.ui.animalspecies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Species;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

/**
 * Created by DAT on 4/16/2017.
 */

public class AnimalSpeciesHeaderAdapter extends
    AbstractHeaderFooterWrapperAdapter<RecyclerView.ViewHolder, AnimalSpeciesHeaderAdapter.FooterViewHolder> {

    private Species species;

    public AnimalSpeciesHeaderAdapter(RecyclerView.Adapter adapter, Species species) {
        setAdapter(adapter);
        this.species = species;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal_species_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal_species_footer, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindHeaderItemViewHolder(RecyclerView.ViewHolder holder, int localPosition) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bindHeaderData(species);
        }
    }

    @Override
    public void onBindFooterItemViewHolder(FooterViewHolder holder, int localPosition) {

    }

    @Override
    public int getHeaderItemCount() {
        return 1;
    }

    @Override
    public int getFooterItemCount() {
        return 1;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;
        @Bind(R.id.name)
        protected TextView name;
        @Bind(R.id.description)
        protected TextView description;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindHeaderData(@NonNull Species species) {
            if (species.getIcon() != null) {
                Glide.with(itemView.getContext()).load(species.getIcon()).into(thumbnail);
            } else {
                Glide.with(itemView.getContext())
                    .load(R.drawable.img_photo_gallery_placeholder)
                    .into(thumbnail);
            }
            name.setText(species.getName());
            description.setText(species.getDescription());
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
