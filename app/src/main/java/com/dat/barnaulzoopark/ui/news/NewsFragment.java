package com.dat.barnaulzoopark.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.api.BZFireBaseApi;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.newsdetail.NewsDetailActivity;
import com.dat.barnaulzoopark.ui.newsdetail.NewsDetailFragment;
import com.dat.barnaulzoopark.ui.newseditor.NewsItemEditorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class NewsFragment
    extends BaseMvpFragment<NewsContract.View, NewsContract.UserActionListener>
    implements NewsContract.View, NewsAdapter.NewsAdapterListener {

    private static final String KEY_NEWS_DETAIL_FRAGMENT = "NEWS_DETAIL_FRAGMENT";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.recyclerViewNews)
    protected RecyclerView recyclerViewNews;
    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;
    private NewsAdapter adapter;

    private int scrollFlags = -1;
    private boolean isAdmin = false;

    private DatabaseReference newsReference;

    private int selectedNewsPosition = 0;

    @NonNull
    @Override
    public NewsContract.UserActionListener createPresenter() {
        FirebaseAuth auth =
            BZApplication.get(getContext()).getApplicationComponent().firebaseAuth();
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseStorage();
        EventBus eventBus = BZApplication.get(getContext()).getApplicationComponent().eventBus();
        return new NewsPresenter(eventBus, auth, database, storage);
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

        if (savedInstanceState == null && BZApplication.isTabletLandscape(getContext())) {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(null);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.newsDetailFragmentContainer, newsDetailFragment,
                KEY_NEWS_DETAIL_FRAGMENT);
            fragmentTransaction.commit();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (BZApplication.isTabletLandscape(getContext()) && newsReference != null) {
            newsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NewsDetailFragment newsDetailFragment =
                        (NewsDetailFragment) getChildFragmentManager().findFragmentByTag(
                            KEY_NEWS_DETAIL_FRAGMENT);
                    if (adapter != null && adapter.getItemCount() > 0) {
                        adapter.setSelectedPosition(selectedNewsPosition);
                        adapter.notifyDataSetChanged();
                        if (newsDetailFragment != null) {
                            newsDetailFragment.showNewsDetail(adapter.getSelectedItem());
                        }
                    } else {
                        if (newsDetailFragment != null) {
                            newsDetailFragment.showNewsDetail(null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
    public void onItemClicked(@NonNull String uid, int selectedPosition) {
        if (!BZApplication.isTabletLandscape(getContext())) {
            NewsDetailActivity.startActivity(getActivity(), uid);
        } else {
            if (adapter.getSelectedPosition() != selectedPosition) {
                this.selectedNewsPosition = selectedPosition;
                adapter.setSelectedPosition(selectedPosition);
                adapter.notifySelectedItem();
                showNewsDetail();
            }
        }
    }

    private void showNewsDetail() {
        NewsDetailFragment articleDetailFragment =
            (NewsDetailFragment) getChildFragmentManager().findFragmentByTag(
                KEY_NEWS_DETAIL_FRAGMENT);
        if (articleDetailFragment != null) {
            articleDetailFragment.showNewsDetail(adapter.getSelectedItem());
        }
    }

    @Override
    public void onNewsLongClicked(String uid) {
        //ToDo: display Edit, Delete Buttons on Toolbar
        if (isAdmin) {
            NewsItemEditorActivity.start(getActivity(), uid);
        }
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNews.setLayoutManager(layoutManager);
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        newsReference = database.getReference(BZFireBaseApi.news);
        adapter = new NewsAdapter(News.class, R.layout.item_news, NewsAdapter.ViewHolder.class,
            newsReference, this);
        newsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (adapter.getItemCount() == 0) {
                    recyclerViewNews.setVisibility(View.INVISIBLE);
                } else {
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    showNewsDetail();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerViewNews.setAdapter(adapter);
        recyclerViewNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isAdmin && !BZApplication.isTabletLandscape(getContext())) {
                    if (dy > 0 && fabCreate.isShown()) {
                        fabCreate.hide();
                    } else if (dy < 0) {
                        fabCreate.show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.fabCreate)
    protected void fabCreateClicked() {
        NewsItemEditorActivity.start(getContext(), null);
    }
}
