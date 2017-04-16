package com.dat.barnaulzoopark.ui.animalspecies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.dat.barnaulzoopark.ui.animalspecies.adapters.AnimalSpeciesHeaderAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import java.util.List;

/**
 * Created by DAT on 4/16/2017.
 */

public class AnimalSpeciesActivity
    extends BaseMvpActivity<AnimalSpeciesContract.View, AnimalSpeciesContract.UserActionListener>
    implements AnimalSpeciesContract.View, AnimalSpeciesExpandableAdapter.ItemClickListener {

    public static final String KEY_SPECIES = "KEY_SPECIES";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.speciesContent)
    protected RecyclerView speciesContent;
    private AnimalSpeciesExpandableAdapter expandableAdapter;

    public static void start(Context context, @NonNull Species species) {
        if (context instanceof AnimalSpeciesActivity) {
            return;
        }
        Intent intent = new Intent(context, AnimalSpeciesActivity.class);
        Gson gson = new Gson();
        String speciesJson = gson.toJson(species, Species.class);
        intent.putExtra(KEY_SPECIES, speciesJson);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public AnimalSpeciesContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new AnimalSpeciesPresenter(database, storage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_species);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        CollapsingToolbarLayout.LayoutParams layoutParams =
            (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        String speciesJson = getIntent().getStringExtra(KEY_SPECIES);
        if (speciesJson == null) {
            Toast.makeText(this, "speciesUid NULL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Species species = new Gson().fromJson(speciesJson, Species.class);
        init(species);
        presenter.loadAnimals(species.getUid());
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> animals) {
        expandableAdapter.setData(animals);
        expandableAdapter.notifyDataSetChanged();
    }

    private void init(@NonNull Species species) {
        // Setup expandable feature and RecyclerView
        RecyclerViewExpandableItemManager expandableItemManager =
            new RecyclerViewExpandableItemManager(null);
        expandableItemManager.setDefaultGroupsExpandedState(true);
        RecyclerView.Adapter adapter;
        adapter = expandableAdapter = new AnimalSpeciesExpandableAdapter();
        adapter = expandableItemManager.createWrappedAdapter(adapter);
        adapter = new AnimalSpeciesHeaderAdapter(adapter, species);

        speciesContent.setAdapter(adapter);
        speciesContent.setLayoutManager(new LinearLayoutManager(this));
        // NOTE: need to disable change animations to ripple effect work properly
        ((SimpleItemAnimator) speciesContent.getItemAnimator()).setSupportsChangeAnimations(false);
        expandableItemManager.attachRecyclerView(speciesContent);
    }

    @Override
    public void onAnimalClicked(@NonNull Animal animal) {

    }
}
