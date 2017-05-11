package com.dat.barnaulzoopark.ui.animalsdetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.YoutubeVideoFragment;
import com.dat.barnaulzoopark.ui.photosdetail.PhotosDetailActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.AnimalsImagesHorizontalSpaceDecoration;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 10-Jul-16.
 */
public class AnimalsDetailFragment extends Fragment
    implements AnimalsImagesHorizontalAdapter.ItemClickListener {

    private static final String KEY_ANIMAL = "ANIMAL";

    @Bind(R.id.scrollView)
    protected ObservableScrollView mScrollView;
    @Bind(R.id.animals_images)
    protected RecyclerView animalsImages;
    private AnimalsImagesHorizontalAdapter animalsImagesAdapter;
    @Bind(R.id.habitatMapImage)
    protected SimpleDraweeView habitatMapImage;

    @Bind(R.id.aboutOurAnimal)
    protected TextView aboutOurAnimal;
    @Bind(R.id.name)
    protected TextView name;
    @Bind(R.id.gender)
    protected ImageView gender;
    @Bind(R.id.dateEnter)
    protected TextView dateEnter;
    @Bind(R.id.dateOfBirth)
    protected TextView dateOfBirth;
    @Bind(R.id.photosContainer)
    protected View photosContainer;
    @Bind(R.id.habitatMapContainer)
    protected View habitatMapContainer;
    @Bind(R.id.videoContainer)
    protected View videoContainer;

    private Animal selectedAnimal;

    public static AnimalsDetailFragment newInstance(@NonNull Animal animal) {
        Gson gson = new Gson();
        String animalJson = gson.toJson(animal);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ANIMAL, animalJson);
        AnimalsDetailFragment fragment = new AnimalsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals_detail, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onItemClicked(int adapterPosition) {
        if (selectedAnimal != null) {
            List<String> albumUrls = new ArrayList<>(selectedAnimal.getPhotos().values());
            PhotosDetailActivity.start(getActivity(), albumUrls, adapterPosition, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindData();
    }

    private void bindData() {
        if (getArguments() == null) {
            return;
        }
        String animalJson = getArguments().getString(KEY_ANIMAL);
        Gson gson = new Gson();
        selectedAnimal = gson.fromJson(animalJson, Animal.class);
        aboutOurAnimal.setText(selectedAnimal.getAboutOurAnimal());
        name.setText(selectedAnimal.getName());
        Drawable drawable = selectedAnimal.isGender() ? ContextCompat.getDrawable(getContext(),
            R.drawable.ic_gender_male)
            : ContextCompat.getDrawable(getContext(), R.drawable.ic_gender_female);
        gender.setImageDrawable(drawable);
        if (selectedAnimal.isGender()) {
            gender.setColorFilter(ContextCompat.getColor(getContext(), R.color.blue));
        } else {
            gender.setColorFilter(ContextCompat.getColor(getContext(), R.color.pink));
        }
        dateEnter.setText(
            ConverterUtils.DATE_FORMAT.format(new Date(selectedAnimal.getTimeEnter())));
        if (selectedAnimal.getDateOfBirth() != null) {
            dateOfBirth.setText(
                ConverterUtils.DATE_FORMAT.format(new Date(selectedAnimal.getDateOfBirth())));
        }
        if (!selectedAnimal.getPhotos().isEmpty()) {
            animalsImagesAdapter.setData(new ArrayList<>(selectedAnimal.getPhotos().values()));
            photosContainer.setVisibility(View.VISIBLE);
        } else {
            photosContainer.setVisibility(View.GONE);
        }
        if (selectedAnimal.getImageHabitatMap() != null) {
            habitatMapImage.setImageURI(selectedAnimal.getImageHabitatMap());
            habitatMapContainer.setVisibility(View.VISIBLE);
        } else {
            habitatMapContainer.setVisibility(View.GONE);
        }
        if (selectedAnimal.getVideo() != null && !selectedAnimal.getVideo().isEmpty()) {
            YoutubeVideoFragment youtubeVideoFragment =
                YoutubeVideoFragment.newInstance(selectedAnimal.getVideo());
            getChildFragmentManager().beginTransaction()
                .replace(R.id.youtubeContainer, youtubeVideoFragment)
                .commitAllowingStateLoss();
            videoContainer.setVisibility(View.VISIBLE);
        } else {
            videoContainer.setVisibility(View.GONE);
        }
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        animalsImages.setLayoutManager(linearLayoutManager);
        animalsImages.addItemDecoration(new AnimalsImagesHorizontalSpaceDecoration(6));
        if (animalsImagesAdapter == null) {
            animalsImagesAdapter = new AnimalsImagesHorizontalAdapter();
            animalsImagesAdapter.setItemClickListener(this);
        }
        animalsImages.setAdapter(animalsImagesAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //hack: expand the header of Material ViewPager on tab selected
        if (mScrollView != null) {
            mScrollView.postDelayed(() -> mScrollView.smoothScrollTo(0, 0), 100);
        }
    }
}
