package com.dat.barnaulzoopark.ui.animals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.widget.SearchView.MySearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nguyen on 6/17/2016.
 */
public class AnimalsFragment extends TempBaseFragment implements MySearchView.SearchViewFocusedListener {
    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.search_view)
    protected MySearchView searchView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals, container, false);
        ButterKnife.bind(this, view);
        if (systemBar != null) {
            systemBar.getLayoutParams().height = getStatusBarHeight();
            systemBar.requestLayout();
        }
        searchView.setSearchViewFocusedListener(this);
        return view;
    }

    @Override
    public void onSearchViewEditTextFocus() {
        Log.d("Focus", "Focus on searchView");
    }

    @Override
    public void onSearchViewEditTextLostFocus() {
        Log.d("Lost Focus", "Lost Focus on searchView");
        searchView.clearSearchView();
    }
}
