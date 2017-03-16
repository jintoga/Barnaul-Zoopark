package com.dat.barnaulzoopark.ui.recyclerviewdecorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import com.dat.barnaulzoopark.R;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position < spanCount) {
            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (view.getContext()
                .getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    view.getContext().getResources().getDisplayMetrics());
            }
            float marginTop = view.getContext()
                .getResources()
                .getDimension(R.dimen.search_view_margin_top_not_fully_expanded);
            float marginBottom = view.getContext()
                .getResources()
                .getDimension(R.dimen.search_view_margin_bottom_not_fully_expanded);
            outRect.top = (int) (actionBarHeight * 2 + marginTop + marginBottom);
            return;
        }
        int column = position % spanCount; // item column
        if (includeEdge) {
            outRect.left = spacing
                - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing
                - (column + 1) * spacing
                / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}