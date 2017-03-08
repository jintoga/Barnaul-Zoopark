package com.dat.barnaulzoopark.ui.news;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 1/2/2017.
 */

public class NewsAdapter extends FirebaseRecyclerAdapter<News, NewsAdapter.ViewHolder> {

    private NewsAdapterListener listener;

    private int selectedPosition = 0;

    /**
     * @param modelClass Firebase will marshall the data at a location into an instance of a class
     * that
     * you provide
     * @param modelLayout This is the layout used to represent a single item in the list. You will
     * be
     * responsible for populating an
     * instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance
     * modelLayout.
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a
     * location,
     * using some
     * combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    NewsAdapter(Class<News> modelClass, int modelLayout, Class<ViewHolder> viewHolderClass,
        Query ref, NewsAdapterListener listener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.listener = listener;
    }

    @Override
    protected void populateViewHolder(final ViewHolder viewHolder, final News model, int position) {

        boolean isSelectedItem = false;
        if (position == getSelectedPosition() && BZApplication.isTabletLandscape(
            viewHolder.itemView.getContext())) {
            isSelectedItem = true;
        }
        if (isSelectedItem) {
            viewHolder.indicator.setVisibility(View.VISIBLE);
        } else {
            viewHolder.indicator.setVisibility(View.GONE);
        }
        if (model != null) {
            viewHolder.title.setText(model.getTitle());
            viewHolder.description.setText(model.getDescription());
            viewHolder.time.setText(ConverterUtils.epochToString(model.getTime()));
            Uri uri;
            if (model.getThumbnail() != null && !"".equals(model.getThumbnail())) {
                uri = Uri.parse(model.getThumbnail());
            } else {
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
            }
            viewHolder.thumbnail.setImageURI(uri);
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onNewsLongClicked(model.getUid());
                    return false;
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(model.getUid(), viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedPosition() {
        return selectedPosition < getItemCount() ? selectedPosition : 0;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public News getSelectedItem() {
        return getItem(getSelectedPosition());
    }

    public void notifySelectedItem() {
        //1st notify for only Selected Item to keep ripple effect
        notifyItemChanged(selectedPosition);
        //then notify all other items to hide Indicator
        for (int i = 0; i < getItemCount(); i++) {
            if (i != selectedPosition) {
                notifyItemChanged(i);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cardView)
        CardView cardView;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.thumbnail)
        SimpleDraweeView thumbnail;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.favourite)
        ImageView favourite;
        @Bind(R.id.share)
        ImageView share;
        @Bind(R.id.indicator)
        View indicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RecyclerView.LayoutParams layoutParams =
                (RecyclerView.LayoutParams) itemView.getLayoutParams();
            int insetShadow = (int) itemView.getResources()
                .getDimension(android.support.v7.cardview.R.dimen.cardview_compat_inset_shadow);
            layoutParams.topMargin = (int) -((cardView.getContentPaddingTop()
                + cardView.getContentPaddingBottom()
                + 2 * cardView.getCardElevation()) + insetShadow);
        }
    }

    interface NewsAdapterListener {
        void onItemClicked(@NonNull String uid, int selectedPosition);

        void onNewsLongClicked(String uid);
    }
}
