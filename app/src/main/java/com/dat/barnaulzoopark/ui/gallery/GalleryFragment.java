package com.dat.barnaulzoopark.ui.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.dat.barnaulzoopark.ui.gallery.adapters.stack.StackAlbumAdapter;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;
import com.dat.barnaulzoopark.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 07-Feb-16.
 */
public class GalleryFragment extends Fragment {

    @Bind(R.id.photoAlbums)
    protected RecyclerView photoAlbums;
    StackAlbumAdapter adapter;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    private void initRecyclerView() {

        GridLayoutManager gridlayoutManager = new GridLayoutManager(getContext(), 2);

        photoAlbums.setNestedScrollingEnabled(false);
        photoAlbums.setHasFixedSize(true);
        photoAlbums.setLayoutManager(gridlayoutManager);

        ArrayList<PhotoAlbum> data = DummyGenerator.getDummyData();
        adapter = new StackAlbumAdapter(data, getContext());
        photoAlbums.setAdapter(adapter);


    }


}