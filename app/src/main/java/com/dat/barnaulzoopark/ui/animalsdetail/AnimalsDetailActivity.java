package com.dat.barnaulzoopark.ui.animalsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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
    BaseActivityWithAnimation<AnimalsDetailContract.View, AnimalsDetailContract.UserActionListener>
    implements AnimalsDetailContract.View {
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
    public AnimalsDetailContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new AnimalsDetailPresenter(database);
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
            String url = animals.get(page).getPhotoBig();
            if (url == null || url.equals("")) {
                return HeaderDesign.fromColorResAndDrawable(R.color.colorPrimary,
                    getResources().getDrawable(R.drawable.img_photo_gallery_placeholder));
            }
            return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary, url);
        });

        materialViewPager.getViewPager()
            .setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());

        //Move to selected animal
        final int selectedAnimalPosition = getIntent().getIntExtra(KEY_SELECTED_ANIMAL_POSITION, 0);
        materialViewPager.post(() -> {
            materialViewPager.getViewPager().setCurrentItem(selectedAnimalPosition);
            materialViewPager.onPageSelected(selectedAnimalPosition);
        });
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
