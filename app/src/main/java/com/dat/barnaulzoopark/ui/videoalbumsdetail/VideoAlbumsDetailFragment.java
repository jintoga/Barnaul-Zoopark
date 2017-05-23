package com.dat.barnaulzoopark.ui.videoalbumsdetail;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.GridSpacingItemDecoration;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumsDetailFragment extends Fragment
    implements VideoAlbumsDetailAdapter.GalleryAdapterListener {

    @Bind(R.id.gallery)
    protected RecyclerView gallery;
    private VideoAlbumsDetailAdapter adapter;

    @Bind(R.id.loading)
    protected ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_detail, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 4);
            gallery.addItemDecoration(new GridSpacingItemDecoration(4, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_photo_album_items_span), false));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
            gallery.addItemDecoration(new GridSpacingItemDecoration(3, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_photo_album_items_span), false));
        }
        gallery.setLayoutManager(layoutManager);

        if (adapter == null) {
            adapter = new VideoAlbumsDetailAdapter(this);
        }
        gallery.setAdapter(adapter);

        return view;
    }

    public void loadData(@NonNull VideoAlbum videoAlbum) {
        adapter.setData(videoAlbum.getVideos().values());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoSelected(String videoId) {
        VideoDetailActivity.start(getActivity(), videoId);
    }
}
