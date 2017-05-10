package com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Species;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorAdapter
    extends FirebaseRecyclerAdapter<Species, CategoryEditorAdapter.ViewHolder> {

    private RemoveChildFromCategoryListener removeChildFromCategoryListener;

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
    public CategoryEditorAdapter(Class<Species> modelClass, int modelLayout,
        Class<ViewHolder> viewHolderClass, Query ref,
        RemoveChildFromCategoryListener removeChildFromCategoryListener) {

        super(modelClass, modelLayout, viewHolderClass, ref);

        this.removeChildFromCategoryListener = removeChildFromCategoryListener;
    }

    @Override
    protected void populateViewHolder(ViewHolder holder, Species species, int position) {
        holder.bindData(species);
        holder.remove.setOnClickListener(
            v -> removeChildFromCategoryListener.onRemoveChildFromCategoryClicked(species));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.photo)
        SimpleDraweeView photo;
        @Bind(R.id.remove)
        ImageView remove;

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

    public interface RemoveChildFromCategoryListener {
        void onRemoveChildFromCategoryClicked(@NonNull Species species);
    }
}
