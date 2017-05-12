package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import java.util.List;

/**
 * Created by DAT on 5/12/2017.
 */

public class FavoriteAnimalsActivity extends
    BaseMvpActivity<FavoriteAnimalsContract.View, FavoriteAnimalsContract.UserActionListener>
    implements FavoriteAnimalsContract.View {

    private static final String KEY_USER = "KEY_USER";

    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;

    public static void start(@NonNull Context context, @NonNull User user) {
        if (context instanceof FavoriteAnimalsActivity) {
            return;
        }
        Intent intent = new Intent(context, FavoriteAnimalsActivity.class);
        String userJson = new Gson().toJson(user);
        intent.putExtra(KEY_USER, userJson);
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
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void loadFavoriteAnimals() {
        String userJson = getIntent().getStringExtra(KEY_USER);
        if (userJson != null) {
            User user = new Gson().fromJson(userJson, User.class);
            presenter.loadFavoritesAnimals(user.getSubscribedAnimals().keySet());
        } else {
            showSnackBar(getString(R.string.user_data_empty_error));
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
