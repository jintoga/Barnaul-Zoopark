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
    private float mStartMarginBottom;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private float dependencyOldY = 0;


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
            if (dependencyY - childHeight - mStartMarginBottom < offset) {
                childPosition = childPosition + diff;
                if (Math.abs(childPosition) > childHeight) {
                    childPosition = -childHeight - mStartMarginBottom;
                }
                child.setY(childPosition);
            }
        } else {//**********Expanding
            if (dependencyY - childHeight - mStartMarginBottom >= offset
                    && childPosition < 0) {
                childPosition = childPosition + diff;
                if (dependencyY - childHeight <= -dependencyHeight) {
                    childPosition = 0;
                }
            } else if (dependencyY - childHeight - mStartMarginBottom > offset) {
                childPosition = 0;
            }
            child.setY(childPosition);
        }

        dependencyOldY = dependency.getY();
        return true;
    }


    private void shouldInitProperties(MySearchView child, View dependency) {
        if (dependencyOldY == 0) {
            dependencyOldY = dependency.getY();
        }
        if (childHeight == 0) {
            childHeight = getToolbarHeight();
        }
        if (dependencyHeight == 0) {
            dependencyHeight = dependency.getHeight();
        }
        if (mStartMarginBottom == 0) {
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_collapse_margin_bottom);
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