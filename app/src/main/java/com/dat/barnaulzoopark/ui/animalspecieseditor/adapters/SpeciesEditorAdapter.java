package com.dat.barnaulzoopark.ui.animalspecieseditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 5/2/2017.
 */

public class SpeciesEditorAdapter
    extends FirebaseRecyclerAdapter<Animal, SpeciesEditorAdapter.ViewHolder> {

    private RemoveAnimalFromSpeciesListener removeChildFromCategoryListener;

    /**
     * @param modelClass Firebase will marshall the data at a location into an instance of a class
     * that
     * you provide
     * @param modelLayout This is the layout used to represent a single item in the list. You will
     * be
     * responsible for populating an
     * instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance
     * modelLayout.
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a
     * location,
     * using some
     * combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public SpeciesEditorAdapter(Class<Animal> modelClass, int modelLayout,
        Class<SpeciesEditorAdapter.ViewHolder> viewHolderClass, Query ref,
        RemoveAnimalFromSpeciesListener removeChildFromCategoryListener) {

        super(modelClass, modelLayout, viewHolderClass, ref);

        this.removeChildFromCategoryListener = removeChildFromCategoryListener;
    }

    @Override
    protected void populateViewHolder(ViewHolder holder, Animal animal, int position) {
        holder.bindData(animal);
        holder.remove.setOnClickListener(
            v -> removeChildFromCategoryListener.onRemoveAnimalFromSpeciesClicked(animal));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        protected TextView name;
        @Bind(R.id.photo)
        protected SimpleDraweeView photo;
        @Bind(R.id.remove)
        protected ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final Animal animal) {
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

    public interface RemoveAnimalFromSpeciesListener {
        void onRemoveAnimalFromSpeciesClicked(@NonNull Animal animal);
    }
}
