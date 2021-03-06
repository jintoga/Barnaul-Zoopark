package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
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
    protected void populateViewHolder(final ViewHolder viewHolder, final AbstractData item,
        int position) {
        viewHolder.bindData(item);
        viewHolder.edit.setOnClickListener(v -> {
            clearPinnedPosition(viewHolder);
            onEditButtonClick(item);
        });
        viewHolder.remove.setOnClickListener(v -> {
            clearPinnedPosition(viewHolder);
            onRemoveButtonClick(item);
        });

        // set swiping properties
        viewHolder.setMaxLeftSwipeAmount(0);
        viewHolder.setMaxRightSwipeAmount(0.3f);
        viewHolder.setSwipeItemHorizontalSlideAmount(getPinnedPosition() == position ? 0.3f : 0);
    }

    private void clearPinnedPosition(ViewHolder viewHolder) {
        pinnedPosition = -1;
        notifyItemChanged(viewHolder.getAdapterPosition());
    }

    @Override
    public int onGetSwipeReactionType(ViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_RIGHT;
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
            // swipe right --- pin
            case Swipeable.RESULT_SWIPED_RIGHT:
                return new SwipeRightResultAction(this, position);
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
        SimpleDraweeView photo;
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

        void bindData(@NonNull AbstractData item) {
            name.setText(item.getText());
            if (item.getPhotoUrl() != null) {
                photo.setImageURI(item.getPhotoUrl());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                photo.setImageURI(uri);
            }
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

    private static class SwipeRightResultAction extends SwipeResultActionMoveToSwipedDirection {
        private DataManagementAdapter adapter;
        private final int position;

        SwipeRightResultAction(DataManagementAdapter adapter, int position) {
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
