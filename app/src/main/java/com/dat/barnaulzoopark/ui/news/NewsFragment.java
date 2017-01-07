package com.dat.barnaulzoopark.ui.news;

import android.os.Bundle;
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
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class NewsFragment extends TempBaseFragment implements NewsAdapter.NewsAdapterListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.recyclerViewNews)
    protected RecyclerView recyclerViewNews;
    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;
    private NewsAdapter adapter;

    private int scrollFlags = -1;

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

    //Prevent toolbar from collapsing when user is ADMIN
    //ToDo: check if user is ADMIN and use this method
    private void disableCollapsing(boolean shouldDisable) {
        AppBarLayout.LayoutParams layoutParams =
            (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if (shouldDisable) {
            layoutParams.setScrollFlags(0);
        } else {
            layoutParams.setScrollFlags(scrollFlags);
        }
        toolbar.setLayoutParams(layoutParams);
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
        adapter = new NewsAdapter(this);
        recyclerViewNews.setAdapter(adapter);
        recyclerViewNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fabCreate.isShown()) {
                    fabCreate.hide();
                } else if (dy < 0) {
                    fabCreate.show();
                }
            }
        });
    }
}
