package com.dat.barnaulzoopark.ui.zoomap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.TempBaseFragment;

/**
 * Created by DAT on 11/4/2016.
 */

public class ZooMapFragment extends TempBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoo_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
