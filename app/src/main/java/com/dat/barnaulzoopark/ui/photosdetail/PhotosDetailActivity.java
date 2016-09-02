package com.dat.barnaulzoopark.ui.photosdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class PhotosDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.page)
    protected TextView page;
    private MyCountDownTimer countDownTimer; //to hide toolbar
    private final long startTime = 2000; // 2.5 SECONDS IDLE TIME
    private final long interval = 1000;

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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        countDownTimer = new MyCountDownTimer(startTime, interval);
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
                page.setText(position + 1 + "/" + photosDetailViewPagerAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        page.setText(1 + "/" + photosDetailViewPagerAdapter.getCount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentPosition = getIntent().getIntExtra(KEY_PHOTO_POSITION, -1);
        if (currentPosition != -1) {
            photoViewPager.setCurrentItem(currentPosition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                showToolbarWithAnimation(false);
            }
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithTransition(true);
                break;
            default:
                return false;
        }
        return true;
    }

    public void showToolbarWithAnimation(boolean shouldShow) {
        if (shouldShow) {
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
        } else {
            toolbar.animate()
                .translationY(-toolbar.getBottom())
                .setInterpolator(new AccelerateInterpolator())
                .start();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        showToolbarWithAnimation(true);
        //Reset the timer on user interaction...
        countDownTimer.cancel();
        countDownTimer.start();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            //DO WHATEVER YOU WANT HERE
            showToolbarWithAnimation(false);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }
}
