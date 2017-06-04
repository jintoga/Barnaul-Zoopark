package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.AbstractData;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.model.PhotoAlbum;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.model.TicketPrice;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.model.animal.Species;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.dat.barnaulzoopark.ui.NonPredictiveItemAnimationsLinearLayoutManager;
import com.dat.barnaulzoopark.ui.animalcategoryeditor.CategoryEditorActivity;
import com.dat.barnaulzoopark.ui.animaleditor.AnimalEditorActivity;
import com.dat.barnaulzoopark.ui.animalspecieseditor.SpeciesEditorActivity;
import com.dat.barnaulzoopark.ui.bloganimaleditor.BlogAnimalEditorActivity;
import com.dat.barnaulzoopark.ui.newseditor.NewsItemEditorActivity;
import com.dat.barnaulzoopark.ui.photoalbumeditor.PhotoAlbumEditorActivity;
import com.dat.barnaulzoopark.ui.sponsoreditor.SponsorEditorActivity;
import com.dat.barnaulzoopark.ui.ticketpriceeditor.TicketPriceEditorActivity;
import com.dat.barnaulzoopark.ui.videoalbumeditor.VideoAlbumEditorActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
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

    private static final String TAG = DataManagementActivity.class.getName();
    private static final String EXTRA_REFERENCE_NAME = "EXTRA_REFERENCE_NAME";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.list)
    protected RecyclerView list;

    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;

    private MaterialDialog progressDialog;

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
        Log.d(TAG, "onRemoveError");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(errorMsg);
    }

    @Override
    public void showRemoveSuccess() {
        Log.d(TAG, "showRemoveSuccess");
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(getString(R.string.remove_success));
    }

    @Override
    public void showRemoveProgress() {
        Log.d(TAG, "showRemoveProgress");
        if (progressDialog == null) {
            progressDialog =
                BZDialogBuilder.createSimpleProgressDialog(this, getString(R.string.removing));
        }
        progressDialog.setContent(getString(R.string.removing));
        progressDialog.show();
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
        if (data instanceof Animal) {
            AnimalEditorActivity.start(this, ((Animal) data).getUid());
        } else if (data instanceof Species) {
            SpeciesEditorActivity.start(this, ((Species) data).getUid());
        } else if (data instanceof Category) {
            CategoryEditorActivity.start(this, ((Category) data).getUid());
        } else if (data instanceof BlogAnimal) {
            BlogAnimalEditorActivity.start(this, ((BlogAnimal) data).getUid());
        } else if (data instanceof News) {
            NewsItemEditorActivity.start(this, ((News) data).getUid());
        } else if (data instanceof TicketPrice) {
            TicketPriceEditorActivity.start(this, ((TicketPrice) data).getUid());
        } else if (data instanceof PhotoAlbum) {
            PhotoAlbumEditorActivity.start(this, ((PhotoAlbum) data).getUid());
        } else if (data instanceof VideoAlbum) {
            VideoAlbumEditorActivity.start(this, ((VideoAlbum) data).getUid());
        }
    }

    private void handleRemoveClicked(@NonNull final AbstractData data) {
        String itemToDelete = "";
        if (data instanceof Animal) {
            itemToDelete = getString(R.string.remove_item_animal);
        } else if (data instanceof Species) {
            itemToDelete = getString(R.string.remove_item_species);
        } else if (data instanceof Category) {
            itemToDelete = getString(R.string.remove_item_category);
        } else if (data instanceof BlogAnimal) {
            itemToDelete = getString(R.string.remove_item_blog_animal);
        } else if (data instanceof News) {
            itemToDelete = getString(R.string.remove_item_news);
        } else if (data instanceof TicketPrice) {
            itemToDelete = getString(R.string.remove_item_ticket_price);
        } else if (data instanceof PhotoAlbum) {
            itemToDelete = getString(R.string.remove_item_photo_album);
        } else if (data instanceof VideoAlbum) {
            itemToDelete = getString(R.string.remove_item_video_album);
        }
        String title =
            String.format(getString(R.string.data_management_remove_title), data.getText(),
                itemToDelete);
        BZDialogBuilder.createConfirmDialog(this, title, getString(R.string.remove))
            .onPositive((dialog, which) -> presenter.removeItem(data))
            .show();
    }

    @Nullable
    private DataManagementAdapter getAdapter(String referenceName) {
        String title = "";
        DataManagementAdapter adapter = null;
        switch (referenceName) {
            case BZFireBaseApi.news:
                title = getString(R.string.data_management_news);
                adapter = getAdapter(News.class, referenceName);
                break;
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
            case BZFireBaseApi.blog_animal:
                title = getString(R.string.data_management_blog_animals);
                adapter = getAdapter(BlogAnimal.class, referenceName);
                break;
            case BZFireBaseApi.sponsors:
                title = getString(R.string.sponsors);
                adapter = getAdapter(Sponsor.class, referenceName);
                break;
            case BZFireBaseApi.ticket_price:
                title = getString(R.string.ticket_price);
                adapter = getAdapter(TicketPrice.class, referenceName);
                break;
            case BZFireBaseApi.photo_album:
                title = getString(R.string.data_management_photo_album);
                adapter = getAdapter(PhotoAlbum.class, referenceName);
                break;
            case BZFireBaseApi.video_album:
                title = getString(R.string.data_management_video_album);
                adapter = getAdapter(VideoAlbum.class, referenceName);
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        return adapter;
    }

    @SuppressWarnings("unchecked")
    @NonNull
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
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new DataManagementPresenter(database, storage);
    }

    @OnClick(R.id.fabCreate)
    protected void fabCreateClicked() {
        String referenceName = getIntent().getStringExtra(EXTRA_REFERENCE_NAME);
        if (referenceName != null) {
            switch (referenceName) {
                case BZFireBaseApi.news:
                    NewsItemEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.animal_categories:
                    CategoryEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.animal_species:
                    SpeciesEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.animal:
                    AnimalEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.blog_animal:
                    BlogAnimalEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.sponsors:
                    SponsorEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.ticket_price:
                    TicketPriceEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.photo_album:
                    PhotoAlbumEditorActivity.start(this, null);
                    break;
                case BZFireBaseApi.video_album:
                    VideoAlbumEditorActivity.start(this, null);
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
