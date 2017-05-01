package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.AbstractData;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.dat.barnaulzoopark.ui.NonPredictiveItemAnimationsLinearLayoutManager;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.CategoryEditorActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementActivity
    extends BaseMvpActivity<DataManagementContract.View, DataManagementContract.UserActionListener>
    implements DataManagementContract.View {

    private static final String EXTRA_REFERENCE_NAME = "EXTRA_REFERENCE_NAME";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.list)
    protected RecyclerView list;

    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;

    public static void start(Context context, @NonNull String referenceName) {
        if (context instanceof DataManagementActivity) {
            return;
        }
        Intent intent = new Intent(context, DataManagementActivity.class);
        intent.putExtra(EXTRA_REFERENCE_NAME, referenceName);
        context.startActivity(intent);
    }

    @Override
    public void onRemoveError(@NonNull String errorMsg) {
        showSnackBar(errorMsg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_management);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
    }

    private void init() {
        String referenceName = getIntent().getStringExtra(EXTRA_REFERENCE_NAME);
        if (referenceName != null) {
            initAdapter(referenceName);
        } else {
            finish();
        }
    }

    private void initAdapter(@NonNull String referenceName) {
        DataManagementAdapter adapter = getAdapter(referenceName);
        if (adapter != null) {
            // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
            RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager =
                new RecyclerViewTouchActionGuardManager();
            recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(
                true);
            recyclerViewTouchActionGuardManager.setEnabled(true);

            // swipe manager
            RecyclerViewSwipeManager recyclerViewSwipeManager = new RecyclerViewSwipeManager();

            RecyclerView.Adapter wrappedAdapter =
                recyclerViewSwipeManager.createWrappedAdapter(adapter);

            final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

            // Change animations are enabled by default since support-v7-recyclerview v22.
            // Disable the change animation in order to make turning back animation of swiped item works properly.
            animator.setSupportsChangeAnimations(false);

            list.setLayoutManager(new NonPredictiveItemAnimationsLinearLayoutManager(this));
            list.setAdapter(wrappedAdapter);
            list.setItemAnimator(animator);
            list.addItemDecoration(new SimpleListDividerDecorator(
                ContextCompat.getDrawable(this, R.drawable.preference_list_divider_material),
                true));
            list.addItemDecoration(new ItemShadowDecorator(
                (NinePatchDrawable) ContextCompat.getDrawable(this,
                    R.drawable.material_shadow_z1)));

            adapter.setActionListener(new DataManagementAdapter.ActionListener() {
                @Override
                public void onEditClicked(AbstractData data) {
                    handleEditClicked(data);
                }

                @Override
                public void onRemoveClicked(AbstractData data) {
                    handleRemoveClicked(data);
                }
            });

            // NOTE:
            // The initialization order is very important! This order determines the priority of touch event handling.
            //
            // priority: TouchActionGuard > Swipe > DragAndDrop
            recyclerViewTouchActionGuardManager.attachRecyclerView(list);
            recyclerViewSwipeManager.attachRecyclerView(list);
        } else {
            finish();
        }
    }

    private void handleEditClicked(@NonNull AbstractData data) {
        //ToDo: implement
        String res = "";
        if (data instanceof Animal) {
            res += "Edit Animal: " + ((Animal) data).getName();
        } else if (data instanceof Species) {
            res += "Edit Species: " + ((Species) data).getName();
        } else if (data instanceof Category) {
            res += "Edit Category: " + ((Category) data).getName();
        }
        showSnackBar(res);
    }

    private void handleRemoveClicked(@NonNull final AbstractData data) {
        BZDialogBuilder.createConfirmDialog(this, getString(R.string.remove_category_title),
            getString(R.string.remove)).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                presenter.removeItem(data);
            }
        }).show();
    }

    private void showSnackBar(@NonNull String msg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Nullable
    private DataManagementAdapter getAdapter(String referenceName) {
        String title = "";
        DataManagementAdapter adapter = null;
        switch (referenceName) {
            case BZFireBaseApi.animal_categories:
                title = getString(R.string.data_management_animal_categories);
                adapter = getAdapter(Category.class, referenceName);
                break;
            case BZFireBaseApi.animal_species:
                title = getString(R.string.data_management_animal_species);
                adapter = getAdapter(Species.class, referenceName);
                break;
            case BZFireBaseApi.animal:
                title = getString(R.string.data_management_animals);
                adapter = getAdapter(Animal.class, referenceName);
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        return adapter;
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractData> DataManagementAdapter getAdapter(Class<T> clzz,
        String referenceName) {
        return new DataManagementAdapter(clzz, R.layout.item_data_management,
            DataManagementAdapter.ViewHolder.class, presenter.getDataReference(referenceName));
    }

    @NonNull
    @Override
    public DataManagementContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new DataManagementPresenter(database);
    }

    @OnClick(R.id.fabCreate)
    protected void fabCreateClicked() {
        String referenceName = getIntent().getStringExtra(EXTRA_REFERENCE_NAME);
        if (referenceName != null) {
            switch (referenceName) {
                case BZFireBaseApi.animal_categories:
                    CategoryEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.animal_species:
                    break;
                case BZFireBaseApi.animal:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
