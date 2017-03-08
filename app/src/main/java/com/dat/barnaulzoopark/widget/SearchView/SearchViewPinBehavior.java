package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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
public class SearchViewPinBehavior extends CoordinatorLayout.Behavior<FloatingSearchView> {
    private Context mContext;

    private float dependencyY;
    private float childMarginBottom;
    private int offset;
    private int childHeight;
    private int dependencyHeight;
    private float dependencyOldY = 0;
    private float childInitY;
    private float cardViewShadow;

    public SearchViewPinBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingSearchView child,
        View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingSearchView child,
        View dependency) {
        shouldInitProperties(child, dependency);
        float diff = dependency.getY() - dependencyOldY;
        Log.e("TAG", " diff: " + diff);
        float childPosition = child.getY();

        if (diff < 0) {//********Collapsing
            if (dependencyY - childHeight - childMarginBottom - cardViewShadow < offset) {
                float percentageOldY = 1 - dependencyOldY / dependencyHeight;
                float percentageCurrentY = 1 - dependency.getY() / dependencyHeight;

                float newDiff = (percentageOldY - percentageCurrentY) * 100;
                childPosition = childPosition + newDiff * 8;
                Log.e("TAG", "GO TOP");
            }
        } else { //**********Expanding
            if (dependencyY - childHeight - childMarginBottom - cardViewShadow >= offset
                && childPosition < 0) {
                float percentageOldY = 1 - dependencyOldY / dependencyHeight;
                float percentageCurrentY = 1 - dependency.getY() / dependencyHeight;

                float newDiff = (percentageOldY - percentageCurrentY) * 100;
                childPosition = childPosition + newDiff * 8;
            } else if (dependencyY - childHeight - childMarginBottom - cardViewShadow > offset) {
                childPosition = childInitY;
            }
        }
        Log.e("TAG", "offset: " + offset);
        child.setY(childPosition);
        dependencyOldY = dependency.getY();
        return true;
    }

    private void shouldInitProperties(FloatingSearchView child, View dependency) {
        if (cardViewShadow == 0) {
            cardViewShadow = mContext.getResources()
                .getDimensionPixelOffset(R.dimen.search_view_cardview_shadow);
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
            childMarginBottom = mContext.getResources()
                .getDimensionPixelOffset(R.dimen.search_view_collapse_margin_bottom);
            float statusBarHeight =
                mContext.getResources().getDimensionPixelOffset(R.dimen.search_view_margin_top);
            childMarginBottom = childMarginBottom + statusBarHeight;
        }
        if (childInitY == 0) {
            childInitY = child.getY();
        }
        offset = childHeight - dependencyHeight;
        dependencyY = dependency.getY();
    }

    public int getToolbarHeight() {
        int result = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data,
                mContext.getResources().getDisplayMetrics());
        }
        return result;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, FloatingSearchView child) {
        float childPosition = child.getY();
        //Log.d("onSaveInstanceState", "childPosition:" + childPosition);
        Parcelable superState = super.onSaveInstanceState(parent, child);

        SavedState ss = new SavedState(superState);

        ss.childPositionToSave = childPosition;
        ss.childInitYToSave = childInitY;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, FloatingSearchView child,
        Parcelable state) {

        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(parent, child, state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(parent, child, ss.getSuperState());

        float childPositionSaved = ss.childPositionToSave;
        child.setY(childPositionSaved);
        childInitY = ss.childInitYToSave;
       /* Log.d("onRestoreInstanceState",
            "childPositionSaved:" + childPositionSaved + "  childInitYToSave:" + childInitY);*/
    }

    static class SavedState extends View.BaseSavedState {
        float childPositionToSave, childInitYToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.childPositionToSave = in.readFloat();
            this.childInitYToSave = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.childPositionToSave);
            out.writeFloat(this.childInitYToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }
}
