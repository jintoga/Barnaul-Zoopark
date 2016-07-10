package com.dat.barnaulzoopark.ui.animalsdetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private View view;

    public static AnimalsDetailFragment newInstance() {
        return new AnimalsDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals_detail, container, false);
        ButterKnife.bind(this, view);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

        initRecyclerView();
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.test_image_map))
                .build();
        imageMap.setImageURI(uri);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        animalsImagesAdapter.setData(DummyGenerator.getAnimalsPhotos());
        animalsImagesAdapter.notifyDataSetChanged();
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

}
