package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.dat.barnaulzoopark.ui.animalsdetail.AnimalsDetailFragment;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

/**
 * Created by DAT on 5/12/2017.
 */

public class FavoriteAnimalsActivity extends
    BaseMvpActivity<FavoriteAnimalsContract.View, FavoriteAnimalsContract.UserActionListener>
    implements FavoriteAnimalsContract.View {

    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;
    @Bind(R.id.emptyText)
    protected TextView emptyText;

    public static void start(@NonNull Context context) {
        if (context instanceof FavoriteAnimalsActivity) {
            return;
        }
        Intent intent = new Intent(context, FavoriteAnimalsActivity.class);
        context.startActivity(intent);
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
        return new FavoriteAnimalsPresenter(database);
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> favoriteAnimals) {
        if (!favoriteAnimals.isEmpty()) {
            initMaterialViewPager(favoriteAnimals);
        }
        setViewPagerVisibility(!favoriteAnimals.isEmpty());
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
    }

    private void setViewPagerVisibility(boolean shouldShow) {
        int viewpagerVisibility = shouldShow ? View.VISIBLE : View.GONE;
        int emptyTextVisibility = !shouldShow ? View.VISIBLE : View.GONE;
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
                finish();
                break;
            default:
                return false;
        }
        return true;
    }
}
