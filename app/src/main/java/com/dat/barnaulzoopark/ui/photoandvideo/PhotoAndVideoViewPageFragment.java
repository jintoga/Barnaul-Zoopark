package com.dat.barnaulzoopark.ui.photoandvideo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.photoandvideo.gallerydetails.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.ui.photoandvideo.model.PhotoAlbum;
import java.util.List;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class PhotoAndVideoViewPageFragment extends TempBaseFragment {

    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.photoAlbums)
    protected RecyclerView photoAlbums;
    private PhotoAlbumsAdapter adapter;

    public static PhotoAndVideoFragment newInstance() {
        return new PhotoAndVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_and_video, container, false);
        ButterKnife.bind(this, view);
        if (systemBar != null) {
            systemBar.getLayoutParams().height = getStatusBarHeight();
            systemBar.requestLayout();
        }
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        GridLayoutManager gridlayoutManager;
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

        photoAlbums.setLayoutManager(gridlayoutManager);

        adapter = new PhotoAlbumsAdapter(null, getContext());
        photoAlbums.setAdapter(adapter);
        List<PhotoAlbum> data = DummyGenerator.getDummyData();
        adapter.setData(data);
    }
}
