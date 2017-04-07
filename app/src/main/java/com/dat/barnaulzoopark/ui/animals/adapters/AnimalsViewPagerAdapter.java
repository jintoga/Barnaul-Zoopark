package com.dat.barnaulzoopark.ui.animals.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Category;
import com.dat.barnaulzoopark.ui.animals.AnimalsViewPageFragment;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 04-Jul-16.
 */
public class AnimalsViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private Context context;
    private Fragment mCurrentFragment;
    private List<Category> categories;

    public AnimalsViewPagerAdapter(FragmentManager fm, Context context, List<Category> categories) {
        super(fm);
        this.context = context;
        this.categories = categories;
        for (Category category : categories) {
            mFragmentList.add(new AnimalsViewPageFragment());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
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
        SimpleDraweeView tabItemAvatar =
            (SimpleDraweeView) view.findViewById(R.id.imageViewTabItemAvatar);

        Category category = categories.get(position);
        tabItemName.setText(category.getName());
        if (category.getIcon() != null) {
            tabItemAvatar.setImageURI(Uri.parse(category.getIcon()));
            tabItemAvatar.setVisibility(View.VISIBLE);
        } else {
            tabItemAvatar.setVisibility(View.GONE);
        }
        return view;
    }

    public void highlightSelectedView(@Nullable View view, boolean shouldHighLight) {
        if (view == null) {
            return;
        }
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
