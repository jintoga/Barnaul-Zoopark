package com.dat.barnaulzoopark.ui.favoriteanimals;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.GridSpacingItemDecoration;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 5/13/2017.
 */

public class FavoriteAnimalsFragment extends
    BaseMvpFragment<FavoriteAnimalsContract.View, FavoriteAnimalsContract.UserActionListener>
    implements FavoriteAnimalsContract.View, FavoriteAnimalsAdapter.ClickListener {

    @Bind(R.id.container)
    protected ViewGroup container;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.favouriteAnimals)
    protected RecyclerView favouriteAnimals;
    @Bind(R.id.emptyText)
    protected TextView emptyText;
    private FavoriteAnimalsAdapter animalsAdapter;

    private MaterialDialog progressDialog;

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
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
                getString(R.string.favourite_animals));
        }
        GridLayoutManager gridlayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridlayoutManager = new GridLayoutManager(getContext(), 3);
            favouriteAnimals.addItemDecoration(new GridSpacingItemDecoration(3,
                getContext().getResources()
                    .getDimensionPixelSize(R.dimen.recycler_view_favorite_animals_span), true));
        } else {
            gridlayoutManager = new GridLayoutManager(getContext(), 2);
            favouriteAnimals.addItemDecoration(new GridSpacingItemDecoration(2,
                getContext().getResources()
                    .getDimensionPixelSize(R.dimen.recycler_view_favorite_animals_span), true));
        }
        favouriteAnimals.setLayoutManager(gridlayoutManager);
        animalsAdapter = new FavoriteAnimalsAdapter(this);
        favouriteAnimals.setAdapter(animalsAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFavoriteAnimals();
    }

    private void loadFavoriteAnimals() {
        User user = BZApplication.get(getContext()).getUser();
        if (user != null) {
            presenter.loadFavoritesAnimals(user.getSubscribedAnimals().keySet());
        } else {
            showSnackBar(container, getString(R.string.user_data_empty));
        }
    }

    @Override
    public void bindAnimals(@NonNull List<Animal> favoriteAnimals) {
        if (!favoriteAnimals.isEmpty()) {
            animalsAdapter.setData(favoriteAnimals);
        }
        setRecyclerViewVisibility(!favoriteAnimals.isEmpty());
    }

    private void setRecyclerViewVisibility(boolean shouldShow) {
        int recyclerViewVisibility =
            shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        int emptyTextVisibility = !shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        favouriteAnimals.setVisibility(recyclerViewVisibility);
        emptyText.setVisibility(emptyTextVisibility);
    }

    @NonNull
    @Override
    public FavoriteAnimalsContract.UserActionListener createPresenter() {
        EventBus eventBus = BZApplication.get(getContext()).getApplicationComponent().eventBus();
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new FavoriteAnimalsPresenter(eventBus, database);
    }

    @Override
    public void onItemClicked(@NonNull Animal animal, int position) {
        FavoriteAnimalsActivity.start(getActivity(), position);
    }

    @Override
    public void onUpdatingUserData() {
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(getContext(),
                getString(R.string.subscribing_to_animal));
        }
        progressDialog.setContent(getString(R.string.subscribing_to_animal));
        progressDialog.show();
    }

    @Override
    public void onUpdateUserDataError(@NonNull String localizedMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(localizedMessage);
    }

    private void showSnackBar(String localizedMessage) {
        showSnackBar(container, localizedMessage);
    }

    @Override
    public void onUpdateUserDataSuccess(boolean isAlreadySubscribed, int clickedPosition) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        String msg =
            isAlreadySubscribed ? getString(R.string.subscribed) : getString(R.string.unsubscribed);
        showSnackBar(msg);
        animalsAdapter.removeItem(clickedPosition);
        setRecyclerViewVisibility(animalsAdapter.getItemCount() > 0);
    }

    @Override
    public void onLoadFavoriteAnimalsProgress() {
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(getContext(),
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
        loadFavoriteAnimals();
    }

    @Override
    public void onSubscribeClicked(@NonNull Animal animal, int clickedPosition) {
        User user = BZApplication.get(getContext()).getUser();
        if (user == null) {
            showSnackBar(getString(R.string.user_data_empty));
            return;
        }
        subscribeToAnimal(animal, user, clickedPosition);
    }

    private void subscribeToAnimal(@NonNull Animal animal, @NonNull User user,
        int clickedPosition) {
        boolean isAlreadySubscribed = user.getSubscribedAnimals().containsKey(animal.getUid());
        presenter.updateUserData(isAlreadySubscribed, user, animal, clickedPosition);
    }
}
