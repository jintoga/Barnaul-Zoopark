package com.dat.barnaulzoopark.ui.animalsdetail;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Animal;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 10-Jul-16.
 */
public class AnimalsDetailFragment extends Fragment {

    @Bind(R.id.scrollView)
    protected ObservableScrollView mScrollView;
    @Bind(R.id.animals_images)
    protected RecyclerView animalsImages;
    AnimalsImagesHorizontalAdapter animalsImagesAdapter;
    @Bind(R.id.image_map)
    protected SimpleDraweeView imageMap;
    @Bind(R.id.playSound)
    protected ImageButton playSound;

    @Bind(R.id.aboutOurAnimal)
    protected TextView aboutOurAnimal;
    @Bind(R.id.aboutSpecies)
    protected TextView aboutSpecies;
    @Bind(R.id.aboutCharacteristics)
    protected TextView aboutCharacteristics;
    @Bind(R.id.factsAboutAnimal)
    protected TextView factsAboutAnimal;

    private MediaPlayer mp;
    private boolean isSoundPlaying = false;
    private Animal animalData;

    public static AnimalsDetailFragment newInstance(int position,
        @Nullable ArrayList<Animal> animalList) {
        AnimalsDetailFragment fragment = new AnimalsDetailFragment();
        Bundle arg = new Bundle();
        arg.putInt(AnimalsDetailActivity.KEY_SELECTED_PAGE_POSITION, position);
        arg.putParcelableArrayList(AnimalsDetailActivity.KEY_ANIMAL_LIST, animalList);
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals_detail, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
            .path(String.valueOf(R.drawable.test_image_map))
            .build();
        imageMap.setImageURI(uri);
        aboutOurAnimal.setText("");
        aboutSpecies.setText("");
        aboutCharacteristics.setText("");
        factsAboutAnimal.setText("");
        if (getArguments() != null) {
            List<Animal> animalList =
                getArguments().getParcelableArrayList(AnimalsDetailActivity.KEY_ANIMAL_LIST);
            if (animalList != null && animalList.size() > 0) {
                int selectedPage =
                    getArguments().getInt(AnimalsDetailActivity.KEY_SELECTED_PAGE_POSITION);
                animalData = animalList.get(selectedPage);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        animalsImagesAdapter.setData(DummyGenerator.getAnimalsDatas());
        animalsImagesAdapter.notifyDataSetChanged();
        aboutOurAnimal.postDelayed(new Runnable() {
            @Override
            public void run() {
                bindData();
            }
        }, 500);
    }

    private void bindData() {
        if (animalData == null) {
            return;
        }
        aboutOurAnimal.setText(
            animalData.getAboutOurAnimals() == null ? "" : animalData.getAboutOurAnimals());
        aboutSpecies.setText(
            animalData.getAboutSpecies() == null ? "" : animalData.getAboutSpecies());
        aboutCharacteristics.setText(
            animalData.getCharacteristics() == null ? "" : animalData.getCharacteristics());
        factsAboutAnimal.setText(animalData.getFacts() == null ? "" : animalData.getFacts());
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        animalsImages.setLayoutManager(linearLayoutManager);
        animalsImages.addItemDecoration(new AnimalsImagesHorizontalSpaceDecoration(6));
        if (animalsImagesAdapter == null) {
            animalsImagesAdapter = new AnimalsImagesHorizontalAdapter();
        }
        animalsImages.setAdapter(animalsImagesAdapter);
    }

    @OnClick(R.id.playSoundContainer)
    protected void playSound() {
        if (animalData != null && animalData.getSoundUrl() != null) {
            togglePlaySound(animalData);
            updatePlaySoundIcon();
        }
    }

    private void updatePlaySoundIcon() {
        if (playSound == null) {
            return;
        }
        if (!isSoundPlaying) {
            playSound.setImageDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_play_circle_filled_white));
        } else {
            playSound.setImageDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.ic_pause_circle_filled_white));
        }
    }

    private void togglePlaySound(Animal animalData) {
        if (!isSoundPlaying) {
            playAudio(animalData);
            isSoundPlaying = true;
        } else {
            playAudio(null);
            isSoundPlaying = false;
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
                    isSoundPlaying = false;
                    updatePlaySoundIcon();
                }
            });
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        clearPlayingSound();
        updatePlaySoundIcon();
    }

    @Override
    public void onPause() {
        clearPlayingSound();
        updatePlaySoundIcon();
        super.onPause();
    }

    private void clearPlayingSound() {
        //stop streaming audio
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        isSoundPlaying = false;
    }
}
