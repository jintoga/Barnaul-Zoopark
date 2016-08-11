package com.dat.barnaulzoopark.ui.photosdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class PhotosDetailActivity extends BaseActivity {

    @Bind(R.id.photo_view_pager)
    protected ViewPager photoViewPager;
    private PhotosDetailViewPagerAdapter photosDetailViewPagerAdapter;

    private static final String KEY_PHOTO_POSITION = "PHOTO_POSITION";
    private static final String KEY_PHOTO_ALBUM = "PHOTO_ALBUM";
    private List<String> albums;
    private int currentPosition = -1;

    public static void startActivity(Activity activity, List<String> albums, int photo_position) {
        Intent intent = new Intent(activity, PhotosDetailActivity.class);
        intent.putStringArrayListExtra(KEY_PHOTO_ALBUM, new ArrayList<String>(albums));
        intent.putExtra(KEY_PHOTO_POSITION, photo_position);
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
        albums = getIntent().getStringArrayListExtra(KEY_PHOTO_ALBUM);
        if (albums == null) {
            return;
        }
        photosDetailViewPagerAdapter =
            new PhotosDetailViewPagerAdapter(getSupportFragmentManager(), albums, this);
        photoViewPager.setAdapter(photosDetailViewPagerAdapter);
        photoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
                Log.d("TAG", "pos:" + position + " pos offset:" + positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentPosition = getIntent().getIntExtra(KEY_PHOTO_POSITION, -1);
        if (currentPosition != -1) {
            photoViewPager.setCurrentItem(currentPosition);
        }
    }
}
