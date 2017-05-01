package com.dat.barnaulzoopark.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by DAT on 5/1/2017.
 */
//Custom LayoutManager to deal with problem java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position
public class NonPredictiveItemAnimationsLinearLayoutManager extends LinearLayoutManager {
    public NonPredictiveItemAnimationsLinearLayoutManager(Context context) {
        super(context);
    }

    public NonPredictiveItemAnimationsLinearLayoutManager(Context context, int orientation,
        boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NonPredictiveItemAnimationsLinearLayoutManager(Context context, AttributeSet attrs,
        int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //override this method and implement code as below
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
