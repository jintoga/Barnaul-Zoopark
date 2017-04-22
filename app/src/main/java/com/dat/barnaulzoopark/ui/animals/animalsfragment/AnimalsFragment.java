package com.dat.barnaulzoopark.ui.animals.animalsfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.animals.adapters.AnimalsViewPagerAdapter;
import com.dat.barnaulzoopark.ui.newseditor.NewsItemEditorActivity;
import com.dat.barnaulzoopark.widget.SearchView.FloatingSearchView;
import com.dat.barnaulzoopark.widget.SmoothSupportAppBarLayout.AppBarManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.List;

/**
 * Created by Nguyen on 6/17/2016.
 */
public class AnimalsFragment
    extends BaseMvpFragment<AnimalsContract.View, AnimalsContract.UserActionListener>
    implements AnimalsContract.View, FloatingSearchView.SearchViewFocusedListener,
    FloatingSearchView.SearchViewDrawerListener, MainActivity.DrawerListener, AppBarManager,
    MainActivity.DispatchTouchEventListener {

    @Bind(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.tabLayout)
    protected TabLayout tabLayout;
    @Bind(R.id.search_view)
    protected FloatingSearchView searchView;
    @Bind(R.id.transparent_view)
    protected View backgroundView;

    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;

    @Bind(R.id.viewpagerAnimals)
    protected ViewPager animalsViewPager;
    private AnimalsViewPagerAdapter animalsViewPagerAdapter;

    private View view;

    @Override
    public void bindCategories(@NonNull List<Category> categories) {
        initAnimalsViewPager(categories);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        boolean isSearchViewFocused = searchView.isSearchViewFocused();
        if (!isSearchViewFocused) {
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.clearSearchView();
                }
            });
        }
    }

    @Override
    public void onDrawerOpen() {
        Log.d("onDrawerOpen", "onDrawerOpen");
        searchView.clearSearchView();
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
        CoordinatorLayout.LayoutParams layoutParams =
            (CoordinatorLayout.LayoutParams) backgroundView.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        backgroundView.setLayoutParams(layoutParams);
        searchView.setBackgroundView(backgroundView);
        searchView.setSearchViewFocusedListener(this);
        searchView.setSearchViewDrawerListener(this);
        //Listener to help close SearchView when NavDrawer is open
        ((MainActivity) getActivity()).setDrawerListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadCategories();
    }

    @NonNull
    @Override
    public AnimalsContract.UserActionListener createPresenter() {
        FirebaseAuth auth =
            BZApplication.get(getContext()).getApplicationComponent().firebaseAuth();
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseStorage();
        return new AnimalsPresenter(auth, database, storage);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");
    }

    private void initAnimalsViewPager(@NonNull List<Category> categories) {
        animalsViewPagerAdapter =
            new AnimalsViewPagerAdapter(getChildFragmentManager(), getContext(), categories);
        animalsViewPager.setAdapter(animalsViewPagerAdapter);
        tabLayout.setupWithViewPager(animalsViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (animalsViewPagerAdapter != null
                && tabLayout.getTabAt(i) != null
                && tabLayout != null) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                View view = animalsViewPagerAdapter.getTabView(i);
                if (tab != null) {
                    tab.setCustomView(view);
                }
            }
        }
        tabLayout.addOnTabSelectedListener(
            new TabLayout.ViewPagerOnTabSelectedListener(animalsViewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    animalsViewPager.setCurrentItem(tab.getPosition());
                    View view = tab.getCustomView();
                    animalsViewPagerAdapter.highlightSelectedView(view, true);
                    /*AnimalsViewPageFragment fragment =
                        (AnimalsViewPageFragment) animalsViewPagerAdapter.getCurrentFragment();
                    if (fragment != null) {
                        fragment.moveToFirst();
                    }
                    appBarLayout.setExpanded(true);*/
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    animalsViewPagerAdapter.highlightSelectedView(view, false);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        animalsViewPager.setCurrentItem(0);
        if (tabLayout.getTabAt(0) != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            if (tab != null) {
                View view = tab.getCustomView();
                animalsViewPagerAdapter.highlightSelectedView(view, true);
            }
        }
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

    @Override
    public void collapseAppBar() {
        appBarLayout.setExpanded(false, true);
    }

    @Override
    public void expandAppBar() {
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public int getVisibleHeightForRecyclerViewInPx() {
        int windowHeight, appBarHeight;
        windowHeight = getActivity().getWindow().getDecorView().getHeight();
        appBarHeight = appBarLayout.getHeight();
        return windowHeight - appBarHeight;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).setDispatchTouchEventListener(this);
    }

    public void dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float per = Math.abs(appBarLayout.getY()) / appBarLayout.getTotalScrollRange();
            boolean setExpanded = (per <= 0.5F);
            appBarLayout.setExpanded(setExpanded, true);
        }
    }

    @OnClick(R.id.fabCreate)
    protected void fabCreateClicked() {

    }
}
