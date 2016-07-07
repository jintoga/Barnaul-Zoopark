package com.dat.barnaulzoopark.ui.animals.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 04-Jul-16.
 */
public class AnimalsViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;
    private Fragment mCurrentFragment;

    public AnimalsViewPagerAdapter(FragmentManager fm, Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_animals_tab, null);
        TextView tabItemName = (TextView) view.findViewById(R.id.textViewTabItemName);
        ImageView tabItemAvatar = (ImageView) view.findViewById(R.id.imageViewTabItemAvatar);

        tabItemName.setText(mFragmentTitleList.get(position));
        if (mFragmentTitleList.get(position).toLowerCase().equals("Млекопитающие".toLowerCase())) {
            tabItemAvatar.setImageResource(R.drawable.ic_bear);
        } else {
            tabItemAvatar.setImageResource(R.drawable.ic_bird);
        }

        return view;
    }

    public void highlightSelectedView(View view, boolean shouldHighLight) {
        TextView tabItemName = (TextView) view.findViewById(R.id.textViewTabItemName);
        ImageView tabItemAvatar = (ImageView) view.findViewById(R.id.imageViewTabItemAvatar);

        if (shouldHighLight) {
            tabItemName.setTextColor(context.getResources().getColor(R.color.white));
            tabItemAvatar.setColorFilter(context.getResources().getColor(R.color.white));
        } else {
            tabItemName.setTextColor(context.getResources().getColor(R.color.gray));
            tabItemAvatar.setColorFilter(context.getResources().getColor(R.color.gray));
        }
    }
}
