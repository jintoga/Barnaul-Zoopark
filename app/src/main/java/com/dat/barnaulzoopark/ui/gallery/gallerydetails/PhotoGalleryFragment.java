package com.dat.barnaulzoopark.ui.gallery.gallerydetails;

import android.os.Bundle;
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

/**
 * Created by DAT on 10-Apr-16.
 */
public class PhotoGalleryFragment extends Fragment {

    private View view;

    @Bind(R.id.gallery)
    protected RecyclerView gallery;
    private PhotoGalleryAdapter adapter;

    @Bind(R.id.loading)
    protected ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery_details, container, false);
        ButterKnife.bind(this, view);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        gallery.setLayoutManager(layoutManager);

        if (adapter == null) {
            adapter = new PhotoGalleryAdapter();
        }
        gallery.setAdapter(adapter);

        return view;
    }
}
