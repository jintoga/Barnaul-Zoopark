package com.dat.barnaulzoopark.ui.animals.animalsviewpagefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.animals.adapters.AnimalsAdapter;
import com.dat.barnaulzoopark.ui.animalspecies.AnimalSpeciesActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.GridSpacingItemDecoration;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.List;

/**
 * Created by DAT on 04-Jul-16.
 */
public class AnimalsViewPageFragment extends
    BaseMvpFragment<AnimalsViewPageContract.View, AnimalsViewPageContract.UserActionListener>
    implements AnimalsViewPageContract.View, AnimalsAdapter.AnimalsAdapterListener {

    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";

    @Bind(R.id.animals)
    protected RecyclerView animals;
    private AnimalsAdapter animalsAdapter;

    public static Fragment create(@NonNull String categoryUid) {
        AnimalsViewPageFragment fragment = new AnimalsViewPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY_ID, categoryUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals_page, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
            animals.addItemDecoration(new GridSpacingItemDecoration(3, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_animals_items_span), true));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
            animals.addItemDecoration(new GridSpacingItemDecoration(2, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_animals_items_span), true));
        }
        animals.setLayoutManager(layoutManager);
        if (animalsAdapter == null) {
            animalsAdapter = new AnimalsAdapter(this);
        }
        animals.setHasFixedSize(true);
        animals.setAdapter(animalsAdapter);
    }

    @NonNull
    @Override
    public AnimalsViewPageContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseStorage();
        return new AnimalsViewPagePresenter(database, storage);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    public void loadData() {
        if (getArguments() != null) {
            String categoryUid = getArguments().getString(KEY_CATEGORY_ID);
            if (categoryUid != null) {
                presenter.loadSpecies(categoryUid);
            }
        }
    }

    @Override
    public void bindSpecies(@NonNull List<Species> speciesList) {
        animalsAdapter.setData(speciesList);
        animalsAdapter.notifyDataSetChanged();
    }

    public void moveToFirst() {
        if (animals != null && animals.getAdapter() != null) {
            animals.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onSpeciesSelected(@NonNull Species species) {
        Log.d("onSpeciesSelected", species.getUid());
        AnimalSpeciesActivity.start(getActivity(), species);
    }
}
