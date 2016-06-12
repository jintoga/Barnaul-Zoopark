package com.dat.barnaulzoopark.ui.gallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.gallery.gallerydetails.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 07-Feb-16.
 */
public class PhotoAlbumsFragment extends TempBaseFragment {

    @Bind(R.id.photoAlbums)
    protected RecyclerView photoAlbums;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.systemBar)
    protected View systemBar;
    private PhotoAlbumsAdapter adapter;
    View view;
    private GridLayoutManager gridlayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        if (systemBar != null) {
            systemBar.getLayoutParams().height = getStatusBarHeight();
            systemBar.requestLayout();
        }
        ((MainActivity) getActivity()).setToolbar(toolbar);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridlayoutManager = new GridLayoutManager(getContext(), 3);
            photoAlbums.addItemDecoration(new GridSpacingItemDecoration(3,
                    getContext().getResources().getDimensionPixelSize(R.dimen.photo_album_items_span),
                    true));
        } else {

            gridlayoutManager = new GridLayoutManager(getContext(), 2);
            photoAlbums.addItemDecoration(new GridSpacingItemDecoration(2,
                    getContext().getResources().getDimensionPixelSize(R.dimen.photo_album_items_span),
                    true));
        }

        //photoAlbums.setHasFixedSize(true);
        photoAlbums.setLayoutManager(gridlayoutManager);

        List<PhotoAlbum> data = DummyGenerator.getDummyData();
        adapter = new PhotoAlbumsAdapter(data, getContext());
        photoAlbums.setAdapter(adapter);
    }
}
