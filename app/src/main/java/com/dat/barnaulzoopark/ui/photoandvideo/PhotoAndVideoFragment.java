package com.dat.barnaulzoopark.ui.photoandvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.photoandvideo.adapters.PhotoAndVideoViewPagerAdapter;

/**
 * Created by DAT on 07-Feb-16.
 */
public class PhotoAndVideoFragment extends TempBaseFragment {

    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.tabLayout)
    protected TabLayout tabLayout;
    @Bind(R.id.viewpagerPhotoAndVideo)
    protected ViewPager photoAndVideoViewPager;
    private PhotoAndVideoViewPagerAdapter photoAndVideoViewPagerAdapter;

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
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
            getString(R.string.photo_and_video));
        initViewPager();
        return view;
    }

    private void initViewPager() {
        photoAndVideoViewPagerAdapter =
            new PhotoAndVideoViewPagerAdapter(getChildFragmentManager(), getContext());
        photoAndVideoViewPager.setAdapter(photoAndVideoViewPagerAdapter);
        photoAndVideoViewPagerAdapter.addFragment(
            PhotoAndVideoViewPageFragment.newInstance(PhotoAndVideoViewPageFragment.PHOTO_TYPE),
            "Фото".toUpperCase());
        photoAndVideoViewPagerAdapter.addFragment(
            PhotoAndVideoViewPageFragment.newInstance(PhotoAndVideoViewPageFragment.VIDEO_TYPE),
            "Видео".toUpperCase());
        tabLayout.setupWithViewPager(photoAndVideoViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (photoAndVideoViewPagerAdapter != null
                && tabLayout.getTabAt(i) != null
                && tabLayout != null) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                View view = photoAndVideoViewPagerAdapter.getTabView(i);
                if (tab != null) {
                    tab.setCustomView(view);
                }
            }
        }
        tabLayout.setOnTabSelectedListener(
            new TabLayout.ViewPagerOnTabSelectedListener(photoAndVideoViewPager) {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    photoAndVideoViewPager.setCurrentItem(tab.getPosition());
                    View view = tab.getCustomView();
                    photoAndVideoViewPagerAdapter.highlightSelectedView(view, true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    photoAndVideoViewPagerAdapter.highlightSelectedView(view, false);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        photoAndVideoViewPager.setCurrentItem(0);
        if (tabLayout.getTabCount() > 0 && tabLayout.getTabAt(0) != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            if (tab != null) {
                View view = tab.getCustomView();
                photoAndVideoViewPagerAdapter.highlightSelectedView(view, true);
            }
        }
    }
}
