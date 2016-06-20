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
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.gallery.gallerydetails.GridSpacingItemDecoration;
import com.dat.barnaulzoopark.ui.gallery.model.Photo;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.CircularIndicator;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.InfiniteViewPager.InfinitePagerAdapter;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.InfiniteViewPager.InfiniteViewPager;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.PagerAdapter;
import com.dat.barnaulzoopark.widget.SearchView.FloatingSearchView;

/**
 * Created by Nguyen on 6/17/2016.
 */
public class AnimalsFragment extends TempBaseFragment
    implements FloatingSearchView.SearchViewFocusedListener, AnimalsAdapter.AnimalsAdapterListener {

    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.app_bar_layout)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.collapsing_toolbar_layout_banner)
    protected CollapsingToolbarLayout collapsingToolbarLayoutBanner;
    @Bind(R.id.collapsing_toolbar_layout_tabs)
    protected CollapsingToolbarLayout collapsingToolbarLayoutTabs;
    @Bind(R.id.search_view)
    protected FloatingSearchView searchView;
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

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            collapsingToolbarLayoutBanner.setNestedScrollingEnabled(false);
            collapsingToolbarLayoutBanner.setFocusable(false);
            collapsingToolbarLayoutBanner.setClickable(false);
            //collapsingToolbarLayoutTabs.setNestedScrollingEnabled(false);
            //appBarLayout.setNestedScrollingEnabled(false);
        }

        searchView.setSearchViewFocusedListener(this);
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
        animals.setAdapter(animalsAdapter);

        final String[] images = new String[] {
            "http://s11.postimg.org/aft369v1v/dog_how_to_select_your_new_best_friend_thinkstoc.jpg",
            "http://s22.postimg.org/3ydo64c3l/cutest_cat_ever_snoopy_face_2.jpg",
            "http://www.zoo22.ru/upload/iblock/05a/05ab85cdf16792f2efeb1a279ba399b0.jpg",
            "http://www.zoo22.ru/upload/iblock/024/024d113a2d4b8f44554eef348fc9affb.png",
            "http://www.zoo22.ru/upload/iblock/e55/e55f7897ac7a6f628900f1ef41558f26.png",
            "http://s32.postimg.org/bu2cb8dlh/018.jpg",
            "http://s32.postimg.org/mi63a2nkl/10472795_1516097865378010_1206966512854576988_o.jpg",
            "http://s32.postimg.org/lothhghjp/11014977_419568654912989_2640509535362674658_n.jpg",
            "http://s32.postimg.org/jlj29shqt/12791057_657347101072467_2630471624444555902_n.jpg",
            "http://s32.postimg.org/b0z8uv1sl/afro_samurai_resurrection_original.jpg",
            "http://s32.postimg.org/n6og59gid/asuras_wrath_wallpaper_hd_2_1080p.jpg",
            "http://s32.postimg.org/8or8x9p79/barret_M107_by_mimi3d.jpg",
            "http://s32.postimg.org/vz5esy1n9/barrett_m107_by_deargruadher_d4dikw8.jpg",
            "http://s32.postimg.org/wiai27t1x/Darksiders_Wrath_of_War_1920x1080.jpg"
        };
        fragmentPagerAdapter =
            new AnimalsHeaderFragmentPagerAdapter(getFragmentManager(), getContext(), images);
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
}
