package com.dat.barnaulzoopark.ui.animalsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.DummyGenerator;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AnimalsDetailActivity extends AppCompatActivity {
    public static final String KEY_PAGE_NUM = "PAGE_NUM";
    public static final String KEY_SELECTED_PAGE_POSITION = "SELECTED_PAGE_POSITION";

    public static void startActivity(Activity activity, int position) {
        if (activity instanceof AnimalsDetailActivity) {
            return;
        }
        Intent intent = new Intent(activity, AnimalsDetailActivity.class);
        intent.putExtra(KEY_PAGE_NUM, DummyGenerator.getAnimalsPhotos().size());
        intent.putExtra(KEY_SELECTED_PAGE_POSITION, position);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Bind(R.id.materialViewPager)
    protected MaterialViewPager materialViewPager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_detail);
        ButterKnife.bind(this);
        toolbar = materialViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        int pageNum = getIntent().getIntExtra(KEY_PAGE_NUM, 0);
        if (pageNum > 0) {
            initMaterialViewPager(pageNum);
        }
    }

    private void initMaterialViewPager(final int pageNum) {
        materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return AnimalsDetailFragment.newInstance();
            }

            @Override
            public int getCount() {
                return pageNum;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Tab #" + position;
            }
        });
        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {

                return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary,
                        "https://hsto.org/files/87c/913/f5f/87c913f5fa3a4d4c807efbcccfa047f7.jpg");
            }
        });

        materialViewPager.getViewPager()
                .setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());

        //Move to selected animal
        materialViewPager.getViewPager().setCurrentItem(getIntent().getIntExtra(KEY_SELECTED_PAGE_POSITION, 0));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
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

    public void finishWithTransition(boolean withAnimation) {
        finish();
        if (withAnimation) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }

}
