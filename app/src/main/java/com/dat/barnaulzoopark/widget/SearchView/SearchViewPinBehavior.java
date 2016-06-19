package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
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
public class SearchViewPinBehavior extends CoordinatorLayout.Behavior<MySearchView> {
    private Context mContext;

    private float dependencyY;
    private float mStartMarginBottom;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private float myOffset = 0;


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

        float diff = dependency.getY() - myOffset;
        // Log.d("diff", "diff:" + diff);
        float childPosition = child.getY();
        // Log.d("dependencyY", "dependencyY:" + dependencyY + " offset:" + offset + " newDiff:" + newDiff);
        if (diff < 0) {
            // Log.d("Collapsing", "childPosition:" + childPosition);
//            Log.d("Collapsing**:", " dependencyY - childHeight: " + (dependencyY - childHeight));
//            Log.d("Y:", " dependencyY:" + dependencyY +
//                    "  dependency's height:" + dependencyHeight +
//                    "  child:" + child.getY() +
//                    "  child's height:" + childHeight);
            if (dependencyY - childHeight - mStartMarginBottom <= offset) {
                childPosition = childPosition + diff;
                if (Math.abs(childPosition) > childHeight) {
                    childPosition = -childHeight - mStartMarginBottom;
                }
                child.setY(childPosition);
                Log.d("Collapsing 1*", "Collapsing 1 childPosition:" + childPosition);
            } else {
                Log.d("Collapsing 2**", "Collapsing 2** childPosition:" + childPosition);
            }
        } else {
            //Log.d("Expanding", "childPosition:" + childPosition);
            if (dependencyY - childHeight > -dependencyHeight
                    && dependencyY - childHeight < offset
                    && childPosition < 0) {
                //Decreasing child's Y
                childPosition = childPosition + diff;
                //Log.d("Expanding 1*", "Expanding 1*");
            } else if (dependencyY - childHeight > -dependencyHeight) {
//                Log.d("Expanding 2****:", " dependencyY - childHeight: " + (dependencyY - childHeight));
                /*Log.d("Expanding 2**", " dependencyY:" + dependencyY +
                        "  dependency's height:" + dependencyHeight +
                        "  child:" + child.getY() +
                        "  child's height:" + childHeight);*/
                childPosition = 0;

                Log.d("Expanding 2**", "Expanding 2** childPosition:" + childPosition);
            } else if (dependencyY - childHeight <= -dependencyHeight
                    && dependencyY - childHeight < offset
                    && childPosition < 0) {
                childPosition = -childHeight - mStartMarginBottom;
                Log.d("Expanding 3**", "Expanding 3*** childPosition:" + childPosition);
                Log.d("Expanding 3***", "Expanding 3*** dependencyY:" + dependencyY +
                        "  dependency's height:" + dependencyHeight +
                        "  \nchild:" + child.getY() +
                        "  child's height:" + childHeight);
            }
            child.setY(childPosition);
            //Log.d("Expanding**:", " dependencyY - childHeight: " + (dependencyY - childHeight));

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
            mStartMarginBottom = mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_collapse_margin_bottom);
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

}
