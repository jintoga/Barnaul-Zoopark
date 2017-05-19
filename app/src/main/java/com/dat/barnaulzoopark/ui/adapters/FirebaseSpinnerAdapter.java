package com.dat.barnaulzoopark.ui.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 5/6/2017.
 */

public abstract class FirebaseSpinnerAdapter<T> extends FirebaseListAdapter<T>
    implements SpinnerAdapter {

    private int mDropDownLayout;

    /**
     * @param activity The activity containing the ListView
     * @param modelClass Firebase will marshall the data at a location into an instance of a class
     * that
     * you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be
     * responsible for populating an
     * instance of the corresponding view with the data from an instance of modelClass.
     * @param ref The Firebase location to watch for data changes. Can also be a slice of a
     * location,
     * using some
     * combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FirebaseSpinnerAdapter(Activity activity, Class<T> modelClass, int modelLayout,
        Query ref, int dropdownLayout) {
        super(activity, modelClass, modelLayout, ref);
        mDropDownLayout = dropdownLayout;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = mActivity.getLayoutInflater().inflate(mDropDownLayout, parent, false);

        T model = getItem(position);

        populateDropdownView(convertView, model, position);
        return convertView;
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for
     * each item that needs
     * to be displayed. The first two arguments correspond to the mDropdownLayout and mModelClass
     * given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the dropdown view using the data contained in the model.
     *
     * @param dropdownView The spinner's dropdown view to populate
     * @param model The object containing the data used to populate the view
     * @param position The position in the list of the view being populated
     */
    abstract protected void populateDropdownView(View dropdownView, T model, int position);
}
