package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseActivityWithAnimation;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsDetailFragment;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

/**
 * Created by DAT on 5/12/2017.
 */

public class FavoriteAnimalsActivity extends
    BaseActivityWithAnimation<FavoriteAnimalsContract.View, FavoriteAnimalsContract.UserActionListener>
    implements FavoriteAnimalsContract.View {

    private static final String KEY_SELECTED_ANIMAL_POSITION = "KEY_SELECTED_ANIMAL_POSITION";

    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;
    @Bind(R.id.emptyText)
    protected TextView emptyText;

    private MaterialDialog progressDialog;

    public static void start(@NonNull Activity activity, int selectedAnimalPosition) {
        if (activity instanceof FavoriteAnimalsActivity) {
            return;
        }
        Intent intent = new Intent(activity, FavoriteAnimalsActivity.class);
        intent.putExtra(KEY_SELECTED_ANIMAL_POSITION, selectedAnimalPosition);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_animals);
        ButterKnife.bind(this);
        init();
        loadFavoriteAnimals();
    }

    private void init() {
        Toolbar toolbar = materialViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.favourite_animals));
        }
    }

    private void loadFavoriteAnimals() {
        User user = BZApplication.get(this).getUser();
        if (user != null) {
            presenter.loadFavoritesAnimals(user.getSubscribedAnimals().keySet());
        } else {
            showSnackBar(getString(R.string.user_data_empty));
        }
    }

    @NonNull
    @Override
    public FavoriteAnimalsContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new FavoriteAnimalsPresenter(null, database);
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> favoriteAnimals) {
        if (!favoriteAnimals.isEmpty()) {
            initMaterialViewPager(favoriteAnimals);
        }
        setViewPagerVisibility(!favoriteAnimals.isEmpty());
    }

    @Override
    public void onUpdatingUserData() {
    }

    @Override
    public void onUpdateUserDataError(@NonNull String localizedMessage) {
    }

    @Override
    public void onUpdateUserDataSuccess(boolean isAlreadySubscribed, int clickedPosition) {
    }

    @Override
    public void onLoadFavoriteAnimalsProgress() {
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(this,
                getString(R.string.loading_favorite_animals));
        }
        progressDialog.setContent(getString(R.string.loading_favorite_animals));
        progressDialog.show();
    }

    @Override
    public void onLoadFavoriteAnimalsSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onLoadFavoriteAnimalsError(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    @Override
    public void updateFavoriteAnimals() {
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
            if (selectedAnimalPosition >= 0 && selectedAnimalPosition < animals.size()) {
                materialViewPager.getViewPager().setCurrentItem(selectedAnimalPosition);
                materialViewPager.onPageSelected(selectedAnimalPosition);
            }
        });
    }

    private void setViewPagerVisibility(boolean shouldShow) {
        int viewpagerVisibility = shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        int emptyTextVisibility = !shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        materialViewPager.getViewPager().setVisibility(viewpagerVisibility);
        materialViewPager.getPagerTitleStrip().setVisibility(viewpagerVisibility);
        materialViewPager.getHeaderBackgroundContainer().setVisibility(viewpagerVisibility);
        if (shouldShow) {
            materialViewPager.setColor(ContextCompat.getColor(this, R.color.colorPrimary), 400);
        } else {
            materialViewPager.setColor(ContextCompat.getColor(this, R.color.transparent), 0);
        }
        emptyText.setVisibility(emptyTextVisibility);
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
