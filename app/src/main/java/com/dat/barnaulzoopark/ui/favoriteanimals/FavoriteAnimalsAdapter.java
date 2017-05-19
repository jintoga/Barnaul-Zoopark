package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/13/2017.
 */

class FavoriteAnimalsAdapter extends RecyclerView.Adapter<FavoriteAnimalsAdapter.ViewHolder> {

    private List<Animal> data = new ArrayList<>();
    private ClickListener clickListener;

    FavoriteAnimalsAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_favorite_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            final Animal animal = data.get(position);
            if (animal != null) {
                holder.bindData(animal, clickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void removeItem(int clickedPosition) {
        if (!data.isEmpty() && clickedPosition >= 0 && clickedPosition < data.size()) {
            data.remove(clickedPosition);
            notifyItemRemoved(clickedPosition);
        }
    }

    public void setData(@NonNull List<Animal> favoriteAnimals) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.clear();
        this.data.addAll(favoriteAnimals);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.thumbnail)
        SimpleDraweeView thumbnail;
        @Bind(R.id.subscribeAnimal)
        ImageView subscribeAnimal;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(@NonNull Animal animal, @NonNull ClickListener clickListener) {
            name.setText(animal.getName());
            if (animal.getPhotoSmall() != null) {
                thumbnail.setImageURI(animal.getPhotoSmall());
            } else if (animal.getPhotoBig() != null) {
                thumbnail.setImageURI(animal.getPhotoBig());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                thumbnail.setImageURI(uri);
            }
            User user = BZApplication.get(itemView.getContext()).getUser();
            if (user != null) {
                updateBookmarkButton(user.getSubscribedAnimals().containsKey(animal.getUid()));
                subscribeAnimal.setVisibility(View.VISIBLE);
                subscribeAnimal.setOnClickListener(v -> {
                    boolean isAlreadySubscribed =
                        user.getSubscribedAnimals().containsKey(animal.getUid());
                    updateBookmarkButton(!isAlreadySubscribed);
                    clickListener.onSubscribeClicked(animal, getAdapterPosition());
                });
            } else {
                subscribeAnimal.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(
                v -> clickListener.onItemClicked(animal, getAdapterPosition()));
        }

        private void updateBookmarkButton(boolean isFavorite) {
            int imageRes =
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_empty;
            subscribeAnimal.setImageResource(imageRes);
            AlphaAnimation alphaAnimationShowIcon = new AlphaAnimation(0.2f, 1.0f);
            alphaAnimationShowIcon.setDuration(500);
            subscribeAnimal.startAnimation(alphaAnimationShowIcon);
        }
    }

    interface ClickListener {
        void onItemClicked(@NonNull Animal animal, int position);

        void onSubscribeClicked(@NonNull Animal animal, int clickedPosition);
    }
}
