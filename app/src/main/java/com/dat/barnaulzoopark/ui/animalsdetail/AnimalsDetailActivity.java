package com.dat.barnaulzoopark.ui.animalsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BaseActivityWithAnimation;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AnimalsDetailActivity extends
    BaseActivityWithAnimation<AnimalsDetailContract.ViewActivity, AnimalsDetailContract.ActivityUserActionListener>
    implements AnimalsDetailContract.ViewActivity {
    public static final String KEY_SPECIES_UID = "KEY_SPECIES_UID";
    private static final String KEY_SELECTED_ANIMAL_POSITION = "KEY_SELECTED_ANIMAL_POSITION";

    public static void start(Activity activity, @NonNull String speciesUid,
        int selectedAnimalPosition) {
        if (activity instanceof AnimalsDetailActivity) {
            return;
        }
        Intent intent = new Intent(activity, AnimalsDetailActivity.class);
        intent.putExtra(KEY_SPECIES_UID, speciesUid);
        intent.putExtra(KEY_SELECTED_ANIMAL_POSITION, selectedAnimalPosition);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_detail);
        ButterKnife.bind(this);
        init();
        String speciesUid = getIntent().getStringExtra(KEY_SPECIES_UID);
        if (speciesUid == null) {
            Toast.makeText(this, "speciesUid NULL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        presenter.loadAnimals(speciesUid);
    }

    @NonNull
    @Override
    public AnimalsDetailContract.ActivityUserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new AnimalsDetailActivityPresenter(database);
    }

    private void init() {
        Toolbar toolbar = materialViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> animals) {
        if (!animals.isEmpty()) {
            initMaterialViewPager(animals);
        } else {
            Toast.makeText(this, R.string.animals_list_empty, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initMaterialViewPager(@NonNull final List<Animal> animals) {
        materialViewPager.getViewPager()
            .setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return AnimalsDetailFragment.newInstance(animals.get(position));
                }

                @Override
                public int getCount() {
                    return animals.size();
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return animals.get(position).getName();
                }
            });
        materialViewPager.setMaterialViewPagerListener(page -> {
            if (animals.get(page).getPhotoBig() == null
                && animals.get(page).getPhotoSmall() == null) {
                return HeaderDesign.fromColorResAndDrawable(R.color.colorPrimary,
                    ContextCompat.getDrawable(this, R.drawable.img_photo_gallery_placeholder));
            } else {
                return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary,
                    getHeaderImageUrl(animals.get(page)));
            }
        });

        materialViewPager.getViewPager()
            .setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());

        //Move to selected animal
        final int selectedAnimalPosition = getIntent().getIntExtra(KEY_SELECTED_ANIMAL_POSITION, 0);
        materialViewPager.post(() -> {
            if (selectedAnimalPosition >= 0 && selectedAnimalPosition < animals.size()) {
                materialViewPager.getViewPager().setCurrentItem(selectedAnimalPosition);
                materialViewPager.onPageSelected(selectedAnimalPosition);
            }
        });
    }

    @NonNull
    private String getHeaderImageUrl(@NonNull Animal animal) {
        if (animal.getPhotoBig() != null) {
            return animal.getPhotoBig();
        } else {
            return animal.getPhotoSmall();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithTransition(true);
                break;
            default:
                return false;
        }
        return true;
    }
}
