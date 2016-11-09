package com.dat.barnaulzoopark.ui.photoandvideo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 14-Jul-16.
 */
public class PhotoAndVideoViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    private Fragment mCurrentFragment;

    public PhotoAndVideoViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @NonNull
    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_and_video_tab, null);
        ImageView tabItemAvatar = (ImageView) view.findViewById(R.id.imageViewTabItemAvatar);
        if (mFragmentTitleList.get(position).toLowerCase().equals("Фото".toLowerCase())) {
            tabItemAvatar.setImageResource(R.drawable.ic_collections_black);
        } else {
            tabItemAvatar.setImageResource(R.drawable.ic_video);
        }

        return view;
    }

    public void highlightSelectedView(View view, boolean shouldHighLight) {
        ImageView tabItemAvatar = (ImageView) view.findViewById(R.id.imageViewTabItemAvatar);

        if (shouldHighLight) {
            tabItemAvatar.setColorFilter(context.getResources().getColor(R.color.white));
        } else {
            tabItemAvatar.setColorFilter(context.getResources().getColor(R.color.gray));
        }
    }
}
