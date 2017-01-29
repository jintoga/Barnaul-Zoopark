package com.dat.barnaulzoopark.ui.recyclerviewdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DAT on 1/2/2017.
 */

public class NewsItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public NewsItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = -verticalSpaceHeight;
            return;
        } else if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = -verticalSpaceHeight;
            outRect.bottom = verticalSpaceHeight;
            return;
        }
        outRect.bottom = verticalSpaceHeight;
    }
}