package com.dat.barnaulzoopark.ui.animalsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.DummyGenerator;
import com.dat.barnaulzoopark.ui.BaseActivity;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class AnimalsDetailActivity extends BaseActivity {
    public static final String KEY_PAGE_NUM = "PAGE_NUM";
    public static final String KEY_SELECTED_PAGE_POSITION = "SELECTED_PAGE_POSITION";
    private String headerURL;

    int selectedPage;

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

        selectedPage = getIntent().getIntExtra(KEY_SELECTED_PAGE_POSITION, 0);
        headerURL = DummyGenerator.getAnimalsImageHeader(selectedPage);
    }

    private void initMaterialViewPager(final int pageNum) {
        materialViewPager.getViewPager()
            .setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
                    return DummyGenerator.getAnimalsSpeciesName(position);
                }
            });
        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                if (headerURL == null || headerURL.equals("")) {
                    return HeaderDesign.fromColorResAndDrawable(R.color.colorPrimary,
                        getResources().getDrawable(R.drawable.img_photo_gallery_placeholder));
                }
                String url = DummyGenerator.getAnimalsImageHeader(page);
                Log.d("URL", url);
                return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary, url);
            }
        });

        materialViewPager.getViewPager()
            .setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());

        //Move to selected animal
        materialViewPager.post(new Runnable() {
            @Override
            public void run() {
                materialViewPager.getViewPager().setCurrentItem(selectedPage);
                materialViewPager.onPageSelected(selectedPage);
            }
        });
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
}
