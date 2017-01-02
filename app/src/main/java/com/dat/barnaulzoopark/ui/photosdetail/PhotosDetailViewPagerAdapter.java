package com.dat.barnaulzoopark.ui.photosdetail;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat on 10-Aug-16.
 */
public class PhotosDetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> urls = new ArrayList<>();
    private Context context;

    public PhotosDetailViewPagerAdapter(FragmentManager fm, List<String> urls, Context context) {
        super(fm);
        this.urls = urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (urls == null) {
            return 0;
        }
        return urls.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PhotosDetailPageFragment.newInstance(urls.get(position));
    }
}
