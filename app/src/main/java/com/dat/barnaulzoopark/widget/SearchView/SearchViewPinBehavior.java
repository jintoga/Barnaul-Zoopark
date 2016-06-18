package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by DAT on 17-Jun-16.
 */
public class SearchViewPinBehavior extends CoordinatorLayout.Behavior<MySearchView> implements AppBarLayout.OnOffsetChangedListener {
    private Context mContext;

    private float dependencyY;
    private float mStartMarginBottom;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private float myOffset = 0;

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = -1;
    private int mCurrentDirection;

    public SearchViewPinBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, MySearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            ((AppBarLayout) dependency).addOnOffsetChangedListener(this);
        }
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, MySearchView child, View dependency) {
        shouldInitProperties(child, dependency);

        float diff = dependency.getY() - myOffset;
        Log.d("diff", "diff:" + diff);
        float childPosition = child.getY();

        if (mCurrentDirection == DIRECTION_UP) {
            Log.d("Collapsing", "childPosition:" + childPosition);
            if (dependencyY - childHeight < offset) {
                childPosition = childPosition + diff;
                if (Math.abs(childPosition) > childHeight) {
                    childPosition = -childHeight;
                }
                child.setY(childPosition);
            }
        } else {
            Log.d("Expanding", "childPosition:" + childPosition);
            childPosition = childPosition + diff;
            if (Math.abs(childPosition) > 0) {
                childPosition = 0;
            }
            child.setY(childPosition);
        }
        myOffset = dependency.getY();

        return true;
    }


    private void shouldInitProperties(MySearchView child, View dependency) {
        if (myOffset == 0) {
            myOffset = dependency.getY();
        }
        if (childHeight == 0) {
            childHeight = getToolbarHeight();
        }
        if (dependencyHeight == 0) {
            dependencyHeight = dependency.getHeight();
        }
        if (mStartMarginBottom == 0) {
            mStartMarginBottom = dependencyHeight - childHeight;
        }
        offset = childHeight - dependencyHeight;
        dependencyY = dependency.getY();
//        Log.d("dependency", "dependency Height:" + dependency.getHeight());
//        Log.d("offset", "offset:" + offset);
//        Log.d("Y:", " dependencyY:" + dependencyY +
//                "  dependency's height:" + dependencyHeight +
//                "  child:" + child.getY() +
//                "  child's height:" + childHeight);
    }

    public int getToolbarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        return result;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mCurrentDirection = verticalOffset < 0 ? DIRECTION_UP : DIRECTION_DOWN;
    }
}
