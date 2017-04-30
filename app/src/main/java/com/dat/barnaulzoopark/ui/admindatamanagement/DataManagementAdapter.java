package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.v4.content.ContextCompat;
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

    private int pinnedPosition = -1;

    private ActionListener actionListener;

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

    interface ActionListener {
        void onEditClicked(AbstractData data);

        void onRemoveClicked(AbstractData data);
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, final AbstractData item,
        int position) {
        viewHolder.name.setText(item.getText());
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditButtonClick(item);
            }
        });
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRemoveButtonClick(item);
            }
        });

        // set swiping properties
        viewHolder.setMaxLeftSwipeAmount(-0.3f);
        viewHolder.setMaxRightSwipeAmount(0);
        viewHolder.setSwipeItemHorizontalSlideAmount(getPinnedPosition() == position ? -0.3f : 0);
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

    void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void onEditButtonClick(AbstractData data) {
        if (actionListener != null) {
            actionListener.onEditClicked(data);
        }
    }

    private void onRemoveButtonClick(AbstractData data) {
        if (actionListener != null) {
            actionListener.onRemoveClicked(data);
        }
    }

    public static class ViewHolder extends AbstractSwipeableItemViewHolder {

        @Bind(R.id.swipeableContainer)
        ViewGroup swipeableContainer;
        @Bind(R.id.behindViews)
        ViewGroup behindViews;
        @Bind(R.id.remove)
        ImageView remove;
        @Bind(R.id.edit)
        ImageView edit;
        @Bind(R.id.photo)
        ImageView photo;
        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            remove.setColorFilter(
                ContextCompat.getColor(itemView.getContext(), R.color.default_icon_color));
            edit.setColorFilter(
                ContextCompat.getColor(itemView.getContext(), R.color.default_icon_color));
        }

        @Override
        public View getSwipeableContainerView() {
            return swipeableContainer;
        }
    }

    private void setPinnedPosition(int pinnedPosition) {
        this.pinnedPosition = pinnedPosition;
    }

    private int getPinnedPosition() {
        return pinnedPosition;
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

            if (adapter.getPinnedPosition() != position) {
                adapter.setPinnedPosition(position);
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
            if (adapter.getPinnedPosition() == position) {
                adapter.setPinnedPosition(-1);
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
