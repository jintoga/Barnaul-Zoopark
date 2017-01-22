package com.dat.barnaulzoopark.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.newsdetail.NewsDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class NewsFragment
    extends BaseMvpFragment<NewsContract.View, NewsContract.UserActionListener>
    implements NewsContract.View, NewsAdapter.NewsAdapterListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.recyclerViewNews)
    protected RecyclerView recyclerViewNews;
    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;
    private NewsAdapter adapter;

    private int scrollFlags = -1;
    private boolean isAdmin = false;

    @Override
    public NewsContract.UserActionListener createPresenter() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return new NewsPresenter(EventBus.getDefault(), auth, database, storage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar, getString(R.string.news));
        init();
        AppBarLayout.LayoutParams layoutParams =
            (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = layoutParams.getScrollFlags();

        return view;
    }

    @Override
    public void showAdminPrivilege(boolean isAdmin) {
        this.isAdmin = isAdmin;
        enableCollapsingToolbar(isAdmin);
        if (isAdmin) {
            fabCreate.setVisibility(View.VISIBLE);
        } else {
            fabCreate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.checkAdminPrivilege();
    }

    //Prevent toolbar from collapsing when user is ADMIN
    private void enableCollapsingToolbar(boolean shouldEnable) {
        AppBarLayout.LayoutParams layoutParams =
            (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if (shouldEnable) {
            layoutParams.setScrollFlags(0);
        } else {
            layoutParams.setScrollFlags(scrollFlags);
        }
        toolbar.setLayoutParams(layoutParams);
    }

    @Override
    public void onClicked(@NonNull String uid) {
        if (!BZApplication.isTabletLandscape(getContext())) {
            NewsDetailActivity.startActivity(getActivity(), uid);
        }
    }

    @Override
    public void onNewsLongClicked(int position) {
        //ToDo: display Edit, Delete Buttons on Toolbar

    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNews.setLayoutManager(layoutManager);
        recyclerViewNews.addItemDecoration(new NewsItemDecoration(
            (int) getResources().getDimension(R.dimen.item_news_margin_bottom_decoration)));

        DatabaseReference newsReference =
            FirebaseDatabase.getInstance().getReference(BZFireBaseApi.news);
        adapter = new NewsAdapter(News.class, R.layout.item_news, NewsAdapter.ViewHolder.class,
            newsReference, this);
        recyclerViewNews.setAdapter(adapter);
        recyclerViewNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isAdmin) {
                    if (dy > 0 && fabCreate.isShown()) {
                        fabCreate.hide();
                    } else if (dy < 0) {
                        fabCreate.show();
                    }
                }
            }
        });
    }
}
