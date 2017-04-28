package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import rx.functions.Func1;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementAdapter<T>
    extends FirebaseRecyclerAdapter<T, DataManagementAdapter.ViewHolder> {

    private Func1<T, String> func1;

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
    public DataManagementAdapter(Class<T> modelClass, int modelLayout,
        Class<ViewHolder> viewHolderClass, Query ref, Func1<T, String> func1) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.func1 = func1;
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, T model, int position) {
        viewHolder.name.setText(func1.call(model));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.photo)
        ImageView photo;
        @Bind(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
