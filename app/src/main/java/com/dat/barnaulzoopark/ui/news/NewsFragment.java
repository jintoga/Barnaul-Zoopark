package com.dat.barnaulzoopark.ui.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.dat.barnaulzoopark.model.Photo;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;
import com.dat.barnaulzoopark.ui.animals.adapters.AnimalsAdapter;

/**
 * Created by Nguyen on 7/13/2016.
 */
public class NewsFragment extends TempBaseFragment
    implements AnimalsAdapter.AnimalsAdapterListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.recyclerViewNews)
    protected RecyclerView recyclerViewNews;
    @Bind(R.id.fabCreate)
    protected FloatingActionButton fabCreate;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar, getString(R.string.news));
        init();
        return view;
    }

    @Override
    public void onPhotoSelected(@NonNull Photo photo, int position) {

    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewNews.setLayoutManager(layoutManager);
        recyclerViewNews.addItemDecoration(new NewsItemDecoration(
            (int) getResources().getDimension(R.dimen.item_news_margin_bottom_decoration)));
        adapter = new NewsAdapter();
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

    @Override
    public void onStart() {
        super.onStart();
    }
}
