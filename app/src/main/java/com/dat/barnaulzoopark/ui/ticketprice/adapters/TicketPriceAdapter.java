package com.dat.barnaulzoopark.ui.ticketprice.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.TicketPrice;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 5/20/2017.
 */

public class TicketPriceAdapter
    extends FirebaseRecyclerAdapter<TicketPrice, TicketPriceAdapter.ViewHolder> {

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
    public TicketPriceAdapter(Class<TicketPrice> modelClass, int modelLayout,
        Class<ViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, TicketPrice ticketPrice,
        int position) {
        viewHolder.bindData(ticketPrice);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.icon)
        SimpleDraweeView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(@NonNull final TicketPrice ticketPrice) {
            name.setText(ticketPrice.getName());
            price.setText(ConverterUtils.getPriceWithCurrency(ticketPrice.getPrice()));
            if (ticketPrice.getIcon() != null) {
                icon.setImageURI(ticketPrice.getIcon());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                icon.setImageURI(uri);
            }
        }
    }
}
