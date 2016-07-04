package com.dat.barnaulzoopark.ui.animals;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.gallery.gallerydetails.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.CircularIndicator;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.InfiniteViewPager.InfinitePagerAdapter;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.InfiniteViewPager.InfiniteViewPager;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.PagerAdapter;
import com.dat.barnaulzoopark.widget.SearchView.FloatingSearchView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nguyen on 6/17/2016.
 */
public class AnimalsFragment extends TempBaseFragment
    implements FloatingSearchView.SearchViewFocusedListener,
    FloatingSearchView.SearchViewDrawerListener, AnimalsAdapter.AnimalsAdapterListener {

    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.app_bar_layout)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.collapsing_toolbar_layout_banner)
    protected CollapsingToolbarLayout collapsingToolbarLayoutBanner;
    @Bind(R.id.search_view)
    protected FloatingSearchView searchView;
    @Bind(R.id.transparent_view)
    protected View backgroundView;
    @Bind(R.id.animals)
    protected RecyclerView animals;
    private AnimalsAdapter animalsAdapter;
    private GridLayoutManager layoutManager;

    @Bind(R.id.viewpagerObject)
    protected InfiniteViewPager objectViewPager;
    @Bind(R.id.indicatorObject)
    protected CircularIndicator indicatorObject;

    private AnimalsHeaderFragmentPagerAdapter fragmentPagerAdapter;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals, container, false);
        ButterKnife.bind(this, view);
        if (systemBar != null) {
            systemBar.getLayoutParams().height = getStatusBarHeight();
            systemBar.requestLayout();
        }
        init();
        return view;
    }

    @Override
    public void onPhotoSelected(@NonNull Photo photo) {

    }

    @Override
    public void onNavigationDrawerOpen() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openDrawer();
        }
    }

    @Override
    public void onNavigationDrawerClosed() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).closeDrawer();
        }
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            collapsingToolbarLayoutBanner.setNestedScrollingEnabled(false);
            appBarLayout.setNestedScrollingEnabled(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //change systemBar's color when appBarLayout collapse more than 60% of it's height
                if (verticalOffset < -appBarLayout.getTotalScrollRange() * 3 / 5) {
                    systemBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    systemBar.setBackgroundColor(getResources().getColor(R.color.transparent));
                }

                if (verticalOffset + collapsingToolbarLayoutBanner.getHeight() == 0) {
                    //Log.d("OPEN SEARCHVIEW", "OPEN SEARCHVIEW");
                } else {
                    //Log.d("CLOSE SEARCHVIEW", "CLOSE SEARCHVIEW");
                }
            }
        });

        searchView.setBackgroundView(backgroundView);
        searchView.setSearchViewFocusedListener(this);
        searchView.setSearchViewDrawerListener(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
            animals.addItemDecoration(new GridSpacingItemDecoration(3,
                getContext().getResources().getDimensionPixelSize(R.dimen.photo_gallery_items_span),
                true));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
            animals.addItemDecoration(new GridSpacingItemDecoration(2,
                getContext().getResources().getDimensionPixelSize(R.dimen.photo_gallery_items_span),
                true));
        }
        animals.setLayoutManager(layoutManager);
        if (animalsAdapter == null) {
            animalsAdapter = new AnimalsAdapter(this);
        }
        animals.setHasFixedSize(true);
        animals.setAdapter(animalsAdapter);

        final String[] images = new String[] {
            "https://s31.postimg.org/lzogm934b/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
            "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
            "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
            "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
            "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png",
            "https://s32.postimg.org/qdg1ceg9x/unnamed.jpg"
        };
        List<String> data = multiplyItems(images, 2);
        fragmentPagerAdapter =
            new AnimalsHeaderFragmentPagerAdapter(getFragmentManager(), getContext(), data);
        final PagerAdapter wrappedFragmentPagerAdapter =
            new InfinitePagerAdapter(fragmentPagerAdapter);

        objectViewPager.setAdapter(wrappedFragmentPagerAdapter);
        indicatorObject.setViewPager(objectViewPager);
        //objectViewPager.setOffscreenPageLimit(2);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentPagerAdapter.setShouldShowChildren(true);
            objectViewPager.setPageMargin(-1);
        } else {
            fragmentPagerAdapter.setShouldShowChildren(false);
            objectViewPager.setPageMargin(0);
        }
        objectViewPager.setClipToPadding(false);
        objectViewPager.enableCenterLockOfChilds();
        objectViewPager.setCurrentItemInCenter(0);
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

    @Override
    public void onSearchViewEditTextFocus() {
        Log.d("Focus", "Focus on searchView");
    }

    @Override
    public void onSearchViewEditTextLostFocus() {
        Log.d("Lost Focus", "Lost Focus on searchView");
        searchView.clearSearchView();
    }

    private List<String> multiplyItems(String[] images, int n) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.addAll(Arrays.asList(images));
        }
        return result;
    }
}
