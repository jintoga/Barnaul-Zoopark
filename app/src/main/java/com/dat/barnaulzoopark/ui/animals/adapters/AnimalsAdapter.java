package com.dat.barnaulzoopark.ui.animals.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Animal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import java.io.IOException;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsAdapter extends FirebaseRecyclerAdapter<Animal, AnimalsAdapter.ViewHolder> {

    private AnimalsAdapterListener listener;

    private MediaPlayer mp;

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
    public AnimalsAdapter(Class<Animal> modelClass, int modelLayout,
        Class<ViewHolder> viewHolderClass, Query ref, AnimalsAdapterListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(final ViewHolder holder, Animal model, int position) {
        final Animal animalData = model;
        if (animalData != null) {
            holder.bindData(animalData);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPhotoSelected(animalData, holder.getAdapterPosition());
                }
            });
            updatePlaySoundIcon(animalData, holder.playSound);
            holder.playSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //clearAllOtherPlayingSounds(holder.getAdapterPosition());
                    togglePlaySound(animalData);
                    updatePlaySoundIcon(animalData, holder.playSound);
                }
            });
        }
    }

    /*private void clearAllOtherPlayingSounds(int position) {
        //stop streaming audio
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        //set all playing sounds(except clicked one) to false
        for (int i = 0; i < data.size(); i++) {
            if (i == position) {
                continue;
            }
            Animal animalData = data.get(i);
            if (animalData.isSoundPlaying()) {
                animalData.setSoundPlaying(false);
            }
        }
        notifyDataSetChanged();
    }*/

    private void updatePlaySoundIcon(Animal animalData, ImageButton playSound) {
        if (!animalData.isSoundPlaying()) {
            playSound.setImageDrawable(playSound.getContext()
                .getResources()
                .getDrawable(R.drawable.ic_play_circle_filled_white));
        } else {
            playSound.setImageDrawable(playSound.getContext()
                .getResources()
                .getDrawable(R.drawable.ic_pause_circle_filled_white));
        }
    }

    private void togglePlaySound(Animal animalData) {
        if (!animalData.isSoundPlaying()) {
            playAudio(animalData);
            animalData.setSoundPlaying(true);
        } else {
            playAudio(null);
            animalData.setSoundPlaying(false);
        }
    }

    private void playAudio(final Animal data) {
        if (data == null) {
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
            return;
        }
        try {
            mp = new MediaPlayer();
            mp.setDataSource(data.getSoundUrl());
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    data.setSoundPlaying(false);
                    notifyDataSetChanged();
                }
            });
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface AnimalsAdapterListener {
        void onPhotoSelected(@NonNull Animal animalData, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.species)
        protected TextView species;
        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;
        @Bind(R.id.playSound)
        protected ImageButton playSound;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(@NonNull final Animal animalData) {
            species.setText(animalData.getName());
            if (animalData.getImageUrl() != null) {
                thumbnail.setImageURI(Uri.parse(animalData.getImageUrl()));
            } else {
                thumbnail.setImageDrawable(itemView.getContext()
                    .getResources()
                    .getDrawable(R.drawable.img_photo_gallery_placeholder));
            }
        }
    }

    public static class ViewHolderLoading extends RecyclerView.ViewHolder {
        @Bind(R.id.loading)
        protected ProgressBar loading;

        public ViewHolderLoading(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
