package com.dat.barnaulzoopark.ui.recyclerviewdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Nguyen on 7/8/2016.
 */
public class AnimalsImagesHorizontalSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpace;

    public AnimalsImagesHorizontalSpaceDecoration(int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != state.getItemCount() - 1) {
            outRect.right = horizontalSpace;
        }
    }
}