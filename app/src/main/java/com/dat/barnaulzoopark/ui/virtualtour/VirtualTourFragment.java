package com.dat.barnaulzoopark.ui.virtualtour;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseFragment;
import com.dat.barnaulzoopark.ui.MainActivity;

/**
 * Created by DAT on 5/17/2017.
 */

public class VirtualTourFragment extends BaseFragment {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.webViewTour)
    protected WebView webViewTour;
    @Bind(R.id.loading)
    protected ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_virtual_tour, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
            getString(R.string.virtual_tour));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webViewTour.getSettings().setJavaScriptEnabled(true);
        webViewTour.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loading != null && loading.isShown()) {
                    loading.setVisibility(View.GONE);
                }
            }
        });
        webViewTour.loadUrl("file:///android_asset/virtualtour.html");
    }

    @Override
    public void onStop() {
        webViewTour.loadUrl("about:blank"); //clear view's state
        super.onStop();
    }
}
