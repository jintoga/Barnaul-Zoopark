package com.dat.barnaulzoopark.ui.animalspecies;

import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 4/16/2017.
 */

public class AnimalSpeciesExpandableAdapter extends
    AbstractExpandableItemAdapter<AnimalSpeciesExpandableAdapter.GroupViewHolder, AnimalSpeciesExpandableAdapter.ChildViewHolder> {

    private List<Animal> data = new ArrayList<>();

    public AnimalSpeciesExpandableAdapter() {
        setHasStableIds(true); // this is required for expandable feature.
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent,
        @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal_species_animals_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent,
        @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal_species_animals_child, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int groupPosition,
        @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        holder.name.setText("Animals");
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition,
        @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        holder.bindAnimalData(data.get(childPosition));
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x,
        int y, boolean expand) {
        return true;
    }

    public void setData(List<Animal> animals) {
        data.clear();
        data.addAll(animals);
        notifyDataSetChanged();
    }

    interface ItemClickListener {
        void onAnimalClicked(@NonNull Animal animal);
    }

    static class GroupViewHolder extends AbstractExpandableItemViewHolder {
        @Bind(R.id.name)
        protected TextView name;

        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChildViewHolder extends AbstractExpandableItemViewHolder {
        @Bind(R.id.name)
        protected TextView name;
        @Bind(R.id.photo)
        protected SimpleDraweeView photo;

        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindAnimalData(@NonNull final Animal animal) {
            name.setText(animal.getName());
            if (animal.getPhotoSmall() != null) {
                photo.setImageURI(animal.getPhotoSmall());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                photo.setImageURI(uri);
            }
        }
    }
}
