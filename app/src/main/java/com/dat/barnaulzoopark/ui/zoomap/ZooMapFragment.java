package com.dat.barnaulzoopark.ui.zoomap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.TempBaseFragment;

/**
 * Created by DAT on 11/4/2016.
 */

@SuppressLint("SetJavaScriptEnabled")
public class ZooMapFragment extends TempBaseFragment {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.webViewMap)
    protected WebView webViewMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zoo_map, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
            getString(R.string.zoo_park_map));
        init();
        return view;
    }

    private void init() {
        webViewMap.getSettings().setLoadsImagesAutomatically(true);
        webViewMap.getSettings().setJavaScriptEnabled(true);
        webViewMap.getSettings().setLoadWithOverviewMode(true);
        webViewMap.getSettings().setUseWideViewPort(true);
        webViewMap.getSettings().setBuiltInZoomControls(true);
        webViewMap.getSettings().setDisplayZoomControls(false);
        webViewMap.getSettings().setSupportZoom(true);

        webViewMap.addJavascriptInterface(new WebAppInterface(getContext()), "Android");
        webViewMap.loadUrl("file:///android_asset/map.html");
    }
}
