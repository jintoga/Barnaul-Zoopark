package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 17-Jun-16.
 */
public class SearchViewCollapseBehavior extends CoordinatorLayout.Behavior<MySearchView> {
    private Context mContext;

    private float dependencyY;
    private float mStartMarginBottom;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private boolean isHide;

    public SearchViewCollapseBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MySearchView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MySearchView child, View dependency) {
        shouldInitProperties(child, dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        Log.d("percentage", "percentage:" + percentage);
        float childPosition = dependencyHeight
                + dependency.getY()
                - childHeight
                - childHeight * percentage;


        childPosition = childPosition - mStartMarginBottom * (1f - percentage);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        child.setLayoutParams(lp);

        child.setY(childPosition);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (isHide && percentage < 1) {
                child.setVisibility(View.VISIBLE);
                isHide = false;
            } else if (!isHide && percentage == 1) {
                child.setVisibility(View.GONE);
                isHide = true;
            }
        }
        return true;
    }

    private void shouldInitProperties(MySearchView child, View dependency) {

        if (childHeight == 0) {
            childHeight = getToolbarHeight();
        }
        if (dependencyHeight == 0) {
            dependencyHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.expanded_banner_height);
        }
        if (mStartMarginBottom == 0) {
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom) - childHeight;
        }
        offset = childHeight - dependencyHeight;
        dependencyY = dependency.getY();
        Log.d("offset", "offset:" + offset);
        Log.d("Y:", "dependency:" + dependency.getY() +
                "dependency's height:" + dependencyHeight +
                "child:" + child.getY() +
                "child's height:" + childHeight);
    }

    public int getToolbarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        return result;
    }
}
