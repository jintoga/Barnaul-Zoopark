package com.dat.barnaulzoopark.ui.animalsdetail;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    //Titles to hide when data is empty
    @Bind(R.id.aboutOurAnimalTitle)
    protected TextView aboutOurAnimalTitle;
    @Bind(R.id.aboutSpeciesTitle)
    protected TextView aboutSpeciesTitle;
    @Bind(R.id.aboutCharacteristicsTitle)
    protected TextView aboutCharacteristicsTitle;

    @Bind(R.id.part1)
    protected View part1;
    @Bind(R.id.part2)
    protected View part2;
    @Bind(R.id.part3)
    protected View part3;
    @Bind(R.id.part4)
    protected View part4;

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
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView,
            new ObservableScrollViewCallbacks() {
                @Override
                public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                    Log.d("MVP", scrollY + "");
                }

                @Override
                public void onDownMotionEvent() {

                }

                @Override
                public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                }
            });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (animalData != null) {
            loadAnimalsGallery(animalData.getSpecies());
            mScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindData();
                }
            }, 100);
        }
    }

    private void loadAnimalsGallery(@Nullable String species) {
        if (species != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference animalReference =
                database.getReference("animals_gallery").child(species);
            animalReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> values = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String url = (String) postSnapshot.getValue(true);
                        values.add(url);
                        Log.e("Get Data", postSnapshot.getKey());
                    }
                    Log.e("Get Data", values.toString());
                    animalsImagesAdapter.setData(values);
                    animalsImagesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void bindData() {
        aboutOurAnimal.setText(
            animalData.getAboutOurAnimals() == null ? "" : animalData.getAboutOurAnimals());
        if (animalData.getAboutOurAnimals() == null) {
            aboutOurAnimalTitle.setVisibility(View.GONE);
        }
        aboutSpecies.setText(
            animalData.getAboutSpecies() == null ? "" : animalData.getAboutSpecies());
        if (animalData.getAboutSpecies() == null) {
            aboutSpeciesTitle.setVisibility(View.GONE);
        }
        aboutCharacteristics.setText(
            animalData.getCharacteristics() == null ? "" : animalData.getCharacteristics());
        if (animalData.getCharacteristics() == null) {
            aboutCharacteristicsTitle.setVisibility(View.GONE);
        }
        factsAboutAnimal.setText(animalData.getFacts() == null ? "" : animalData.getFacts());
        if (animalData.getFacts() == null) {
            part3.setVisibility(View.GONE);
        }
        if (animalData.getAboutOurAnimals() == null
            && animalData.getAboutSpecies() == null
            && animalData.getCharacteristics() == null) {
            part1.setVisibility(View.GONE);
        }
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

        //hack: expand the header of Material ViewPager on tab selected
        if (mScrollView != null) {
            mScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScrollView.smoothScrollTo(0, 0);
                }
            }, 100);
        }
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
