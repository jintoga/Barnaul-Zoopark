package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 17-Jun-16.
 */
public class SearchViewPinBehavior extends CoordinatorLayout.Behavior<FloatingSearchView> {

    public SearchViewPinBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingSearchView child,
        View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingSearchView child,
        View dependency) {
        if (dependency instanceof AppBarLayout) {
            float statusBarHeight =
                child.getResources().getDimensionPixelOffset(R.dimen.search_view_margin_top);
            child.setY(dependency.getY() + statusBarHeight);
            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, FloatingSearchView child) {
        float childPosition = child.getY();
        Parcelable superState = super.onSaveInstanceState(parent, child);

        SavedState ss = new SavedState(superState);

        ss.childPositionToSave = childPosition;
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
    }

    private static class SavedState extends View.BaseSavedState {
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
