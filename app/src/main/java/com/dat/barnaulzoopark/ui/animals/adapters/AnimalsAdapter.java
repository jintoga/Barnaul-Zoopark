package com.dat.barnaulzoopark.ui.animals.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.AnimalData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.ViewHolder> {

    private List<AnimalData> data = new ArrayList<>();
    private AnimalsAdapterListener listener;

    private MediaPlayer mp;

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
            final AnimalData animalData = data.get(position);
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
                    clearAllOtherPlayingSounds(holder.getAdapterPosition());
                    togglePlaySound(animalData);
                    updatePlaySoundIcon(animalData, holder.playSound);
                }
            });
        }
    }

    private void clearAllOtherPlayingSounds(int position) {
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
            AnimalData animalData = data.get(i);
            if (animalData.getSound().isPlaying()) {
                animalData.getSound().setPlaying(false);
            }
        }
        notifyDataSetChanged();
    }

    private void updatePlaySoundIcon(AnimalData animalData, ImageButton playSound) {
        if (!animalData.getSound().isPlaying()) {
            playSound.setImageDrawable(playSound.getContext()
                .getResources()
                .getDrawable(R.drawable.ic_play_circle_filled_white));
        } else {
            playSound.setImageDrawable(playSound.getContext()
                .getResources()
                .getDrawable(R.drawable.ic_pause_circle_filled_white));
        }
    }

    private void togglePlaySound(AnimalData animalData) {
        if (!animalData.getSound().isPlaying()) {
            playAudio(animalData);
            animalData.getSound().setPlaying(true);
        } else {
            playAudio(null);
            animalData.getSound().setPlaying(false);
        }
    }

    private void playAudio(final AnimalData data) {
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
            mp.setDataSource(data.getSound().getUrl());
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    data.getSound().setPlaying(false);
                    notifyDataSetChanged();
                }
            });
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
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

    public void setData(List<AnimalData> animalData) {
        data.clear();
        data.addAll(animalData);
        notifyDataSetChanged();
    }

    public interface AnimalsAdapterListener {
        void onPhotoSelected(@NonNull AnimalData animalData, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;
        @Bind(R.id.playSound)
        protected ImageButton playSound;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final AnimalData animalData) {
            thumbnail.setImageURI(Uri.parse(animalData.getPhoto().getUrl()));
        }
    }
}
