package com.dat.barnaulzoopark.ui.news;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.News;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.newsdetail.NewsDetailActivity;
import com.dat.barnaulzoopark.ui.newsdetail.NewsDetailFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class NewsFragment
    extends BaseMvpFragment<NewsContract.View, NewsContract.UserActionListener>
    implements NewsContract.View, NewsAdapter.NewsAdapterListener {

    private static final String KEY_NEWS_DETAIL_FRAGMENT = "NEWS_DETAIL_FRAGMENT";
    private static final String KEY_SELECTED_POSITION = "SELECTED_POSITION";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.recyclerViewNews)
    protected RecyclerView recyclerViewNews;
    private NewsAdapter adapter;

    private int selectedNewsPosition = 0;

    @NonNull
    @Override
    public NewsContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new NewsPresenter(database);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar, getString(R.string.news));

        if (/*savedInstanceState == null &&*/ BZApplication.get(getContext()).isTabletLandscape()) {
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
        initRecyclerView();
        if (BZApplication.get(getContext()).isTabletLandscape()) {
            presenter.loadData();
        }
    }

    @Override
    public void onItemClicked(@NonNull String uid, int selectedPosition) {
        if (adapter.getSelectedPosition() != selectedPosition) {
            this.selectedNewsPosition = selectedPosition;
            adapter.setSelectedPosition(selectedPosition);
            adapter.notifySelectedItem();
        }
        if (!BZApplication.get(getContext()).isTabletLandscape()) {
            NewsDetailActivity.startActivity(getActivity(), uid);
        } else {
            showNewsDetail();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_POSITION, adapter.getSelectedPosition());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            int selectedPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
            Log.e("TAG", selectedPosition + "");
            this.selectedNewsPosition = selectedPosition;
            adapter.setSelectedPosition(selectedPosition);
            adapter.notifySelectedItem();
        }
    }

    @Override
    public void bindNewsDetail() {
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

    private void showNewsDetail() {
        if (isAdded()) {
            NewsDetailFragment articleDetailFragment =
                (NewsDetailFragment) getChildFragmentManager().findFragmentByTag(
                    KEY_NEWS_DETAIL_FRAGMENT);
            if (articleDetailFragment != null) {
                articleDetailFragment.showNewsDetail(adapter.getSelectedItem());
            }
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNews.setLayoutManager(layoutManager);
        recyclerViewNews.addItemDecoration(new SimpleListDividerDecorator(
            ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material),
            true));
        recyclerViewNews.addItemDecoration(new ItemShadowDecorator(
            (NinePatchDrawable) ContextCompat.getDrawable(getContext(),
                R.drawable.material_shadow_z1)));

        adapter = new NewsAdapter(News.class, R.layout.item_news, NewsAdapter.ViewHolder.class,
            presenter.getNewsReference(), this);
        presenter.getNewsReference().addValueEventListener(new ValueEventListener() {
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
    }
}
