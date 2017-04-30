package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.AbstractData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementAdapter<T extends AbstractData>
    extends FirebaseRecyclerAdapter<T, DataManagementAdapter.ViewHolder>
    implements SwipeableItemAdapter<DataManagementAdapter.ViewHolder> {

    private int pinnedItem = -1;

    DataManagementAdapter(Class<T> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass,
        Query ref) {

        super(modelClass, modelLayout, viewHolderClass, ref);
        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, AbstractData item, int position) {
        viewHolder.name.setText(item.getText());

        // set swiping properties
        viewHolder.setMaxLeftSwipeAmount(-0.3f);
        viewHolder.setMaxRightSwipeAmount(0);
        viewHolder.setSwipeItemHorizontalSlideAmount(getPinnedItem() == position ? -0.3f : 0);
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_LEFT;
    }

    @Override
    public void onSetSwipeBackground(ViewHolder holder, int position, int type) {
        if (type == Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND) {
            holder.behindViews.setVisibility(View.GONE);
        } else {
            holder.behindViews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SwipeResultAction onSwipeItem(ViewHolder holder, int position, int result) {
        switch (result) {
            // swipe left --- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends AbstractSwipeableItemViewHolder {

        @Bind(R.id.swipeableContainer)
        ViewGroup swipeableContainer;
        @Bind(R.id.behindViews)
        ViewGroup behindViews;
        @Bind(R.id.photo)
        ImageView photo;
        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public View getSwipeableContainerView() {
            return swipeableContainer;
        }
    }

    private void setPinnedItem(int pinnedItem) {
        this.pinnedItem = pinnedItem;
    }

    private int getPinnedItem() {
        return pinnedItem;
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private DataManagementAdapter adapter;
        private final int position;

        SwipeLeftResultAction(DataManagementAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            if (adapter.getPinnedItem() != position) {
                adapter.setPinnedItem(position);
                adapter.notifyItemChanged(position);
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private DataManagementAdapter adapter;
        private final int position;

        UnpinResultAction(DataManagementAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            if (adapter.getPinnedItem() == position) {
                adapter.setPinnedItem(-1);
                adapter.notifyItemChanged(position);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            adapter = null;
        }
    }
}
