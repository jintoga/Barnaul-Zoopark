package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 4/27/2017.
 */

public class BasePreferenceFragment extends PreferenceFragmentCompat {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
                getString(R.string.data_control));
        }
    }
}
