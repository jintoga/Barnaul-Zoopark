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

    private static final String KEY_NEWS = "key_news";
    private static final String KEY_ANIMAL_CATEGORIES = "key_animal_categories";
    private static final String KEY_ANIMAL_SPECIES = "key_animal_species";
    private static final String KEY_ANIMALS = "key_animals";
    private static final String KEY_BLOG_ANIMAL = "key_blog_animal";
    private static final String KEY_TICKET_PRICE = "key_ticket_price";
    private static final String KEY_PHOTO_ALBUM = "key_photo_album";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.data_management);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_NEWS:
                DataManagementActivity.start(getContext(), BZFireBaseApi.news);
                break;
            case KEY_ANIMAL_CATEGORIES:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal_categories);
                break;
            case KEY_ANIMAL_SPECIES:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal_species);
                break;
            case KEY_ANIMALS:
                DataManagementActivity.start(getContext(), BZFireBaseApi.animal);
                break;
            case KEY_BLOG_ANIMAL:
                DataManagementActivity.start(getContext(), BZFireBaseApi.blog_animal);
                break;
            case KEY_TICKET_PRICE:
                DataManagementActivity.start(getContext(), BZFireBaseApi.ticket_price);
                break;
            case KEY_PHOTO_ALBUM:
                DataManagementActivity.start(getContext(), BZFireBaseApi.photo_album);
                break;
        }
        return super.onPreferenceTreeClick(preference);
    }
}
