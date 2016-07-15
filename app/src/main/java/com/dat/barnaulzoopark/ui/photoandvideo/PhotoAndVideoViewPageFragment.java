package com.dat.barnaulzoopark.ui.photoandvideo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.photoalbumsdetail.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.ui.photoandvideo.adapters.PhotoAlbumsAdapter;
import com.dat.barnaulzoopark.ui.photoandvideo.adapters.VideoAlbumsAdapter;
import java.util.List;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class PhotoAndVideoViewPageFragment extends TempBaseFragment {

    @Bind(R.id.photoAlbums)
    protected RecyclerView multiMediaList;
    private PhotoAlbumsAdapter photoAlbumsAdapter;
    private VideoAlbumsAdapter videoAlbumsAdapter;

    private static final String KEY_FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int PHOTO_TYPE = 1;
    public static final int VIDEO_TYPE = 2;

    public static PhotoAndVideoViewPageFragment newInstance(int type) {
        PhotoAndVideoViewPageFragment fragment = new PhotoAndVideoViewPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAGMENT_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_and_video_page, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            int type = getArguments().getInt(KEY_FRAGMENT_TYPE);
            initRecyclerView(type);
        }
        return view;
    }

    private void initRecyclerView(int type) {
        GridLayoutManager gridlayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridlayoutManager = new GridLayoutManager(getContext(), 3);
            multiMediaList.addItemDecoration(new GridSpacingItemDecoration(3,
                getContext().getResources().getDimensionPixelSize(R.dimen.photo_album_items_span),
                true));
        } else {
            gridlayoutManager = new GridLayoutManager(getContext(), 2);
            multiMediaList.addItemDecoration(new GridSpacingItemDecoration(2,
                getContext().getResources().getDimensionPixelSize(R.dimen.photo_album_items_span),
                true));
        }

        multiMediaList.setLayoutManager(gridlayoutManager);
        if (type == PHOTO_TYPE) {
            photoAlbumsAdapter = new PhotoAlbumsAdapter(null, getActivity());
            multiMediaList.setAdapter(photoAlbumsAdapter);
        } else if (type == VIDEO_TYPE) {
            videoAlbumsAdapter = new VideoAlbumsAdapter(null, getContext());
            multiMediaList.setAdapter(videoAlbumsAdapter);
        }
        List<PhotoAlbum> data = DummyGenerator.getDummyData();
        if (photoAlbumsAdapter != null) {
            photoAlbumsAdapter.setData(data);
        }
        if (videoAlbumsAdapter != null) {
            videoAlbumsAdapter.setData(data);
        }
    }
}
