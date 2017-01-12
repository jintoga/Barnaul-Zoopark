package com.dat.barnaulzoopark.ui.newsdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;

/**
 * Created by DAT on 1/9/2017.
 */

public class NewsDetailFragment
    extends BaseMvpFragment<NewsDetailContract.View, NewsDetailContract.UserActionListener>
    implements NewsDetailContract.View {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.content_container)
    protected View contentContainer;

    @NonNull
    @Override
    public NewsDetailContract.UserActionListener createPresenter() {
        return new NewsDetailPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
        CollapsingToolbarLayout.LayoutParams layoutParams =
            (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int viewId = view.getId();
            }
        });
        return view;
    }

    @Override
    public void showNews() {

    }
}
