package com.dat.barnaulzoopark.ui.photoalbumsdetail;

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
import android.widget.ProgressBar;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.dat.barnaulzoopark.model.Photo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoAlbumsDetailFragment extends Fragment implements PhotoAlbumsDetailAdapter.GalleryAdapterListener {

    private View view;

    @Bind(R.id.gallery)
    protected RecyclerView gallery;
    private PhotoAlbumsDetailAdapter adapter;
    private GridLayoutManager layoutManager;

    @Bind(R.id.loading)
    protected ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_albums_detail, container, false);
        ButterKnife.bind(this, view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 4);
            gallery.addItemDecoration(new GridSpacingItemDecoration(4,
                    getContext().getResources().getDimensionPixelSize(R.dimen.recycler_view_photo_album_items_span),
                    false));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
            gallery.addItemDecoration(new GridSpacingItemDecoration(3,
                    getContext().getResources().getDimensionPixelSize(R.dimen.recycler_view_photo_album_items_span),
                    false));
        }
        gallery.setLayoutManager(layoutManager);

        if (adapter == null) {
            adapter = new PhotoAlbumsDetailAdapter(this);
        }
        gallery.setAdapter(adapter);

        return view;
    }


    public void loadData(String albumId) {
        if (albumId != null) {
            adapter.setData(DummyGenerator.getPhotoAlbumById(albumId));
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onPhotoSelected(@NonNull Photo photo) {
        Log.d("Photo", photo.getUrl());
    }
}
