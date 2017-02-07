package com.dat.barnaulzoopark.ui.newsdetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.ConverterUtils;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.recyclerviewdecorations.AnimalsImagesHorizontalSpaceDecoration;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 1/9/2017.
 */

public class NewsDetailFragment
    extends BaseMvpFragment<NewsDetailContract.View, NewsDetailContract.UserActionListener>
    implements NewsDetailContract.View {

    private static final String KEY_NEWS_UID = "NEWS_UID";

    @Bind(R.id.app_bar_layout)
    protected AppBarLayout appBarLayout;
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
    @Bind(R.id.photos)
    protected RecyclerView photos;
    private NewsDetailPhotosAdapter photosAdapter;

    @NonNull
    @Override
    public NewsDetailContract.UserActionListener createPresenter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return new NewsDetailPresenter(database, storage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            String newsUid = getArguments().getString(KEY_NEWS_UID);
            if (newsUid != null) {
                presenter.loadNewsDetail(newsUid);
            }
        }
    }

    private void init() {
        if (BZApplication.isTabletLandscape(getContext())) {
            toolbar.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("");
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
            CollapsingToolbarLayout.LayoutParams layoutParams =
                (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.topMargin = getStatusBarHeight();
            toolbar.setLayoutParams(layoutParams);
        }
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewId = view.getId();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photos.setLayoutManager(linearLayoutManager);
        photos.addItemDecoration(new AnimalsImagesHorizontalSpaceDecoration(6));
        if (photosAdapter == null) {
            photosAdapter = new NewsDetailPhotosAdapter();
        }
        photos.setAdapter(photosAdapter);
    }

    @Override
    public void showNewsDetail(@NonNull News news) {
        appBarLayout.setExpanded(true, false);

        if (news.getThumbnail() != null) {
            thumbnail.setImageURI(Uri.parse(news.getThumbnail()));
        } else {
            Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
            thumbnail.setImageURI(uri);
        }
        title.setText(news.getTitle());
        description.setText(news.getDescription());
        time.setText(ConverterUtils.epochToString(news.getTime()));
        if (news.getPhotos() != null && !news.getPhotos().isEmpty()) {
            List<String> urls = new ArrayList<>(news.getPhotos().values());
            photosAdapter.setData(urls);
        }
    }
}
