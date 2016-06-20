package com.dat.barnaulzoopark.ui.animals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.widget.SearchView.FloatingSearchView;

/**
 * Created by Nguyen on 6/17/2016.
 */
public class AnimalsFragment extends TempBaseFragment
    implements FloatingSearchView.SearchViewFocusedListener {
    @Bind(R.id.systemBar)
    protected View systemBar;
    @Bind(R.id.search_view)
    protected FloatingSearchView searchView;
    @Bind(R.id.animals)
    protected RecyclerView animals;
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
        init();
        return view;
    }

    private void init() {
        searchView.setSearchViewFocusedListener(this);
        animals.setLayoutManager(new LinearLayoutManager(getContext()));
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
