package com.dat.barnaulzoopark.ui.gallery;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //important
    }

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
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
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

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
        //SearchView
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setHintTextColor(getResources().getColor(R.color.gray));
        searchView.setQueryHint("Search");

        //Spinner
        final MenuItem spinnerMenuItem = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(spinnerMenuItem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((MainActivity) getActivity()).getSupportActionBar().getThemedContext(),
                R.array.test_toolbar_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        setItemsVisibility(spinnerMenuItem, false);
                        Log.d("menuItem", "onMenuItemActionExpand");
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        setItemsVisibility(spinnerMenuItem, true);
                        Log.d("menuItem", "onMenuItemActionCollapse");
                        return true;
                    }
                });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setItemsVisibility(MenuItem item, boolean visible) {
        if (item != null) {
            item.setVisible(visible);
        }
    }


}
