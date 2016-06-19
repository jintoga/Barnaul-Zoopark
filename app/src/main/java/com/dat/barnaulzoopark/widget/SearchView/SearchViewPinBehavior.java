package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 17-Jun-16.
 */
public class SearchViewPinBehavior extends CoordinatorLayout.Behavior<MySearchView> {
    private Context mContext;

    private float dependencyY;
    private float childMarginBottom, childMarginTop;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private float dependencyOldY = 0;

    private float childInitY;

    public SearchViewPinBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MySearchView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MySearchView child, View dependency) {
        shouldInitProperties(child, dependency);

        float diff = dependency.getY() - dependencyOldY;
        float childPosition = child.getY();

        if (diff < 0) {//********Collapsing
            if (dependencyY - childHeight - childMarginBottom - childMarginTop < offset) {
                childPosition = childPosition + diff;
                if (Math.abs(childPosition) > childHeight) {
                    childPosition = -childHeight - childMarginBottom;
                }
            }
        } else {//**********Expanding
            if (dependencyY - childHeight - childMarginBottom - childMarginTop >= offset
                    && childPosition < 0) {
                childPosition = childPosition + diff;
                if (dependencyY - childHeight - childMarginTop <= -dependencyHeight) {
                    childPosition = childInitY;
                }
            } else if (dependencyY - childHeight - childMarginBottom > offset) {
                childPosition = childInitY;
            }
        }

        child.setY(childPosition);

        dependencyOldY = dependency.getY();
        return true;
    }


    private void shouldInitProperties(MySearchView child, View dependency) {
        if (childInitY == 0) {
            childInitY = child.getY();
        }
        if (dependencyOldY == 0) {
            dependencyOldY = dependency.getY();
        }
        if (childHeight == 0) {
            childHeight = getToolbarHeight();
        }
        if (dependencyHeight == 0) {
            dependencyHeight = dependency.getHeight();
        }
        if (childMarginBottom == 0) {
            childMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_collapse_margin_bottom);
        }
        if (childMarginTop == 0) {
            childMarginTop = mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_margin_top);
        }
        offset = childHeight - dependencyHeight;
        dependencyY = dependency.getY();

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
