package com.dat.barnaulzoopark.ui.animals;

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
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Animal;
import com.dat.barnaulzoopark.ui.animals.adapters.AnimalsAdapter;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsDetailActivity;
import com.dat.barnaulzoopark.ui.photoalbumsdetail.GridSpacingItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 04-Jul-16.
 */
public class AnimalsViewPageFragment extends Fragment
    implements AnimalsAdapter.AnimalsAdapterListener {
    @Bind(R.id.loading)
    protected ProgressBar loading;
    @Bind(R.id.animals)
    protected RecyclerView animals;
    private AnimalsAdapter animalsAdapter;
    private GridLayoutManager layoutManager;
    private View view;

    private FirebaseDatabase database;
    private DatabaseReference animalDatabaseReference;

    private List<Animal> animalList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animals_page, container, false);
        ButterKnife.bind(this, view);
        testMethod();
        init();
        return view;
    }

    //ToDo: remove this shit
    private void testMethod() {

        FirebaseDatabase database;
        DatabaseReference tigerRef;

        database = FirebaseDatabase.getInstance();
        tigerRef = database.getReference("animals_gallery").child("tigeras");
        tigerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> values = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String url = (String) postSnapshot.getValue(true);
                    values.add(url);
                    Log.e("Get Data", postSnapshot.getKey());
                }
                Log.e("Get Data", values.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        animalDatabaseReference = database.getReference("animals");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
            animals.addItemDecoration(new GridSpacingItemDecoration(3, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_animals_items_span), false));
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
            animals.addItemDecoration(new GridSpacingItemDecoration(2, getContext().getResources()
                .getDimensionPixelSize(R.dimen.recycler_view_animals_items_span), false));
        }
        animals.setLayoutManager(layoutManager);
        if (animalsAdapter == null) {
            animalsAdapter = new AnimalsAdapter(Animal.class, R.layout.item_animals,
                AnimalsAdapter.ViewHolder.class, animalDatabaseReference, this);
        }
        animals.setHasFixedSize(true);
        animalDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                animalList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Animal animal = postSnapshot.getValue(Animal.class);
                    animalList.add(animal);
                    Log.e("Get Data", postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        animalDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERR", "Failed to read value.", error.toException());
                loading.setVisibility(View.GONE);
            }
        });
        animals.setAdapter(animalsAdapter);
    }

    public void moveToFirst() {
        if (animals != null && animals.getAdapter() != null) {
            animals.scrollToPosition(0);
        }
    }

    @Override
    public void onPhotoSelected(@NonNull Animal animalData, int position) {
        Log.d("Click Animals", animalData.getImageUrl() != null ? animalData.getImageUrl() : "");
        AnimalsDetailActivity.startActivity(getActivity(), new ArrayList<>(animalList), position);
    }
}
