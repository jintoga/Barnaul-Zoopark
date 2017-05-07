package com.dat.barnaulzoopark.ui.animaleditor.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.adapters.FirebaseSpinnerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by DAT on 5/7/2017.
 */

public class AnimalEditorSpeciesSpinnerAdapter extends FirebaseSpinnerAdapter<Species> {

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
    public AnimalEditorSpeciesSpinnerAdapter(Activity activity, Class<Species> modelClass,
        int modelLayout, int dropdownLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref, dropdownLayout);
    }

    @Override
    protected void populateDropdownView(View dropdownView, Species species, int position) {
        ((TextView) dropdownView.findViewById(android.R.id.text1)).setText(species.getName());
    }

    @Override
    protected void populateView(View view, Species species, int position) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(species.getName());
    }
}
