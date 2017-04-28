package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.ui.BasePreferenceFragment;

/**
 * Created by DAT on 4/27/2017.
 */

public class DataManagementPreferenceFragment extends BasePreferenceFragment {

    private static final String KEY_ANIMAL_CATEGORIES = "key_animal_categories";
    private static final String KEY_ANIMAL_SPECIES = "key_animal_species";
    private static final String KEY_ANIMALS = "key_animals";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.data_management);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_ANIMAL_CATEGORIES:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal_categories);
                break;
            case KEY_ANIMAL_SPECIES:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal_species);
                break;
            case KEY_ANIMALS:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal);
                break;
        }
        return super.onPreferenceTreeClick(preference);
    }
}
