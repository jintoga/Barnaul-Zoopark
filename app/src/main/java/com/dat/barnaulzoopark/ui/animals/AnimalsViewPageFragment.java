package com.dat.barnaulzoopark.ui.animals;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.dat.barnaulzoopark.ui.animals.adapters.AnimalsAdapter;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsDetailActivity;
import com.dat.barnaulzoopark.ui.photoalbumsdetail.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.model.Photo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 04-Jul-16.
 */
public class AnimalsViewPageFragment extends Fragment
        implements AnimalsAdapter.AnimalsAdapterListener {
    @Bind(R.id.animals)
    protected RecyclerView animals;
    private AnimalsAdapter animalsAdapter;
    private GridLayoutManager layoutManager;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals_page, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
            animals.addItemDecoration(new GridSpacingItemDecoration(3,
                    getContext().getResources().getDimensionPixelSize(R.dimen.recycler_view_animals_items_span),
                    false));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
            animals.addItemDecoration(new GridSpacingItemDecoration(2,
                    getContext().getResources().getDimensionPixelSize(R.dimen.recycler_view_animals_items_span),
                    false));
        }
        animals.setLayoutManager(layoutManager);
        if (animalsAdapter == null) {
            animalsAdapter = new AnimalsAdapter(this);
        }
        animals.setHasFixedSize(true);
        animals.setAdapter(animalsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    public void loadData() {
        animalsAdapter.setData(DummyGenerator.getAnimalsPhotos());
        animalsAdapter.notifyDataSetChanged();
    }

    public void moveToFirst() {
        if (animals != null && animals.getAdapter() != null) {
            animals.scrollToPosition(0);
        }
    }

    @Override
    public void onPhotoSelected(@NonNull Photo photo, int position) {
        Log.d("Click Animals", photo.getUrl());
        AnimalsDetailActivity.startActivity(getActivity(), position);
    }
}
