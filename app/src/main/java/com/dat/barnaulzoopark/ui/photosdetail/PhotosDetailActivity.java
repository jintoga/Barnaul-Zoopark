package com.dat.barnaulzoopark.ui.photosdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;

public class PhotosDetailActivity extends BaseActivity {

    @Bind(R.id.photo_view_pager)
    protected ViewPager photoViewPager;
    private PhotosDetailViewPagerAdapter photosDetailViewPagerAdapter;

    private static final String KEY_PHOTO_URL = "PHOTO_URL";

    public static void startActivity(Activity activity, String photo_url) {
        Intent intent = new Intent(activity, PhotosDetailActivity.class);
        intent.putExtra(KEY_PHOTO_URL, photo_url);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_detail);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        photosDetailViewPagerAdapter =
            new PhotosDetailViewPagerAdapter(getSupportFragmentManager(), this);
        photoViewPager.setAdapter(photosDetailViewPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        photosDetailViewPagerAdapter.addFragment(
            PhotosDetailPageFragment.newInstance(getIntent().getStringExtra(KEY_PHOTO_URL)));
    }
}
