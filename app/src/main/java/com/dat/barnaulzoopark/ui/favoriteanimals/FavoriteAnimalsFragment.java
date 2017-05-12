package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import java.util.List;

/**
 * Created by DAT on 5/12/2017.
 */

public class FavoriteAnimalsFragment extends
    BaseMvpFragment<FavoriteAnimalsContract.View, FavoriteAnimalsContract.UserActionListener>
    implements FavoriteAnimalsContract.View {

    private static final String KEY_USER = "KEY_USER";

    @NonNull
    public static Fragment newInstance(@NonNull User user) {
        Bundle args = new Bundle();
        String userJson = new Gson().toJson(user);
        args.putString(KEY_USER, userJson);
        FavoriteAnimalsFragment fragment = new FavoriteAnimalsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_animals, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFavoriteAnimals();
    }

    private void loadFavoriteAnimals() {
        if (getArguments() != null) {
            String userJson = getArguments().getString(KEY_USER);
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
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new FavoriteAnimalsPresenter(database);
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> favoriteAnimals) {

    }
}
