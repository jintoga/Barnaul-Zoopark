package com.dat.barnaulzoopark.ui.newsdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
