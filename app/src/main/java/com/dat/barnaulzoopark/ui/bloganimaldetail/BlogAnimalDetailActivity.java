package com.dat.barnaulzoopark.ui.bloganimaldetail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.ui.BaseActivityWithAnimation;
import com.dat.barnaulzoopark.ui.YoutubeVideoFragment;
import com.dat.barnaulzoopark.ui.adapters.AttachmentImagesHorizontalAdapter;
import com.dat.barnaulzoopark.ui.photosdetail.PhotosDetailActivity;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.AnimalsImagesHorizontalSpaceDecoration;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.widget.NestedScrollView;

/**
 * Created by DAT on 5/15/2017.
 */

public class BlogAnimalDetailActivity extends
    BaseActivityWithAnimation<BlogAnimalDetailContract.View, BlogAnimalDetailContract.UserActionListener>
    implements BlogAnimalDetailContract.View, AttachmentImagesHorizontalAdapter.ItemClickListener {

    private static final String EXTRA_BLOG_ANIMAL_UID = "EXTRA_BLOG_ANIMAL_UID";

    @Bind(R.id.app_bar_layout)
    protected SmoothAppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.content_container)
    protected View contentContainer;
    @Bind(R.id.thumbnail)
    protected SimpleDraweeView thumbnail;
    @Bind(R.id.title)
    protected TextView title;
    @Bind(R.id.description)
    protected TextView description;
    @Bind(R.id.time)
    protected TextView time;
    @Bind(R.id.photosLabel)
    protected TextView photosLabel;
    @Bind(R.id.photos)
    protected RecyclerView photos;
    private AttachmentImagesHorizontalAdapter photosAdapter;
    @Bind(R.id.youtubeContainer)
    protected FrameLayout youtubeContainer;
    @Bind(R.id.nestedScrollView)
    protected NestedScrollView nestedScrollView;

    private BlogAnimal selectedBlogAnimal;

    public static void start(Activity activity, @NonNull String uid) {
        if (activity instanceof BlogAnimalDetailActivity) {
            return;
        }
        Intent intent = new Intent(activity, BlogAnimalDetailActivity.class);
        intent.putExtra(EXTRA_BLOG_ANIMAL_UID, uid);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void onAttachmentClicked(int adapterPosition) {
        if (selectedBlogAnimal != null) {
            List<String> albumUrls = new ArrayList<>(selectedBlogAnimal.getPhotos().values());
            PhotosDetailActivity.start(this, albumUrls, adapterPosition, false);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_animal_detail);
        ButterKnife.bind(this);
        String blogAnimalUid = getIntent().getStringExtra(EXTRA_BLOG_ANIMAL_UID);
        if (blogAnimalUid == null) {
            Toast.makeText(this, "blogAnimalUid NULL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        init();
        presenter.loadBlogAnimal(blogAnimalUid);
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        CollapsingToolbarLayout.LayoutParams layoutParams =
            (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photos.setLayoutManager(linearLayoutManager);
        photos.addItemDecoration(new AnimalsImagesHorizontalSpaceDecoration(6));
        if (photosAdapter == null) {
            photosAdapter = new AttachmentImagesHorizontalAdapter();
            photosAdapter.setItemClickListener(this);
        }
        photos.setAdapter(photosAdapter);
    }

    @NonNull
    @Override
    public BlogAnimalDetailContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        return new BlogAnimalDetailPresenter(database);
    }

    @Override
    public void showBlogAnimal(@NonNull BlogAnimal blogAnimal) {
        this.selectedBlogAnimal = blogAnimal;
        nestedScrollView.scrollTo(0, 0);
        if (blogAnimal.getThumbnail() != null) {
            thumbnail.setImageURI(Uri.parse(blogAnimal.getThumbnail()));
        } else {
            Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
            thumbnail.setImageURI(uri);
        }
        title.setText(blogAnimal.getTitle());
        description.setText(blogAnimal.getDescription());
        time.setText(ConverterUtils.epochToString(blogAnimal.getTime()));
        if (!blogAnimal.getPhotos().isEmpty()) {
            List<String> urls = new ArrayList<>(blogAnimal.getPhotos().values());
            photosAdapter.setData(urls);
            photos.setVisibility(View.VISIBLE);
            photosLabel.setVisibility(View.VISIBLE);
        } else {
            photos.setVisibility(View.GONE);
            photosLabel.setVisibility(View.GONE);
        }
        if (blogAnimal.getVideo() != null && !blogAnimal.getVideo().isEmpty()) {
            YoutubeVideoFragment youtubeVideoFragment =
                YoutubeVideoFragment.newInstance(blogAnimal.getVideo());
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.youtubeContainer, youtubeVideoFragment)
                .commitAllowingStateLoss();
            youtubeContainer.setVisibility(View.VISIBLE);
        } else {
            youtubeContainer.setVisibility(View.GONE);
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
