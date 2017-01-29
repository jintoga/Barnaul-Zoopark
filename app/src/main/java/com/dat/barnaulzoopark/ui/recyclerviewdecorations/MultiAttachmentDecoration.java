package com.dat.barnaulzoopark.ui.recyclerviewdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by DAT on 1/29/2017.
 */

public class MultiAttachmentDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public MultiAttachmentDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.right = verticalSpaceHeight;
        }
    }
}
