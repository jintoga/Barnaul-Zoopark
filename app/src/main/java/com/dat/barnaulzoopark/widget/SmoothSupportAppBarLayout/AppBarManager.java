package com.dat.barnaulzoopark.widget.SmoothSupportAppBarLayout;

/**
 * Inspired by http://novoda.com/blog/fixing-hiding-appbarlayout-android-tv/
 */

public interface AppBarManager {

    void collapseAppBar();

    void expandAppBar();

    int getVisibleHeightForRecyclerViewInPx();
}
