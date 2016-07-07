package com.dat.barnaulzoopark.ui.animals.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.ui.animals.AnimalsBannerViewPageFragment;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.FragmentPagerAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsBannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> data;
    private Context context;
    private boolean shouldShowChildren = false;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags;
    private int mLastPosition = -1;

    public AnimalsBannerFragmentPagerAdapter(FragmentManager fm, Context context,
        List<String> data) {
        super(fm);
        this.mFragmentManager = fm;
        this.context = context;
        mFragmentTags = new HashMap<>();
        this.data = data;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (position > 0) {
            highlightCurrentFragment(position);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getActualCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(context, AnimalsBannerViewPageFragment.class.getName(), null);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % getActualCount();
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof AnimalsBannerViewPageFragment) {
            // record the fragment tag here.
            AnimalsBannerViewPageFragment fragment = (AnimalsBannerViewPageFragment) obj;
            if (fragment.getArguments() == null) {
                Bundle args = new Bundle();
                args.putString(AnimalsBannerViewPageFragment.ARGUMENT_PHOTO, data.get(position));
                fragment.setArguments(args);
            }
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null) {
            return null;
        }
        return mFragmentManager.findFragmentByTag(tag);
    }

    public void highlightCurrentFragment(int position) {
        position = position % getActualCount();
        Fragment lastFragment = getFragment(mLastPosition);
       /* if (mLastPosition >= 0
            && lastFragment != null
            && lastFragment instanceof AnimalsBannerViewPageFragment) {
            ((AnimalsBannerViewPageFragment) lastFragment).displayOverGroundImage(true);
        }
        Fragment curFragment = getFragment(position);
        if (curFragment != null && curFragment instanceof AnimalsBannerViewPageFragment) {
            ((AnimalsBannerViewPageFragment) curFragment).displayOverGroundImage(false);
        }*/

        mLastPosition = position;
    }

    public void setShouldShowChildren(boolean shouldShowChildren) {
        this.shouldShowChildren = shouldShowChildren;
    }

    @Override
    public float getPageWidth(int position) {
        if (shouldShowChildren) {
            return 0.8f;
        }
        return 1f;
    }
}