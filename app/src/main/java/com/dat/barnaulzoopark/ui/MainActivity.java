package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.animals.AnimalsFragment;
import com.dat.barnaulzoopark.ui.news.NewsFragment;
import com.dat.barnaulzoopark.ui.photoandvideo.PhotoAndVideoFragment;

public class MainActivity extends AppCompatActivity
    implements OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.drawer)
    protected DrawerLayout drawerLayout;
    private int currentMenuItemID = -1;
    DrawerListener drawerListener;

    private static final String TAG_HOME_FRAGMENT = "HOME";

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        //Ignore
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if (drawerListener != null) {
            //drawer is open, SearchView should be closed
            drawerListener.onDrawerOpen();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        //Ignore
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        //Ignore
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavDrawer();
        if (savedInstanceState == null) {
            addInitFragment(new AnimalsFragment());
            currentMenuItemID = R.id.ourAnimals;
        }
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
    }

    public void setupNavDrawerWithToolbar(Toolbar toolbar, String title) {
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle =
            new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open,
                R.string.nav_drawer_closed);
        drawerLayout.addDrawerListener(mDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            if (title != null) {
                getSupportActionBar().setTitle(title);
            }
        }
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Closing drawer on item click
        drawerLayout.closeDrawers();
        if (currentMenuItemID == menuItem.getItemId()) {
            return false;
        }
        currentMenuItemID = menuItem.getItemId();
        Fragment fragment = null;
        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case R.id.ourAnimals:
                getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                addInitFragment(new AnimalsFragment());
                return true;
            case R.id.photoAndVideo:
                fragment = new PhotoAndVideoFragment();
                break;
            case R.id.newsAndEvents:
                fragment = new NewsFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Log.d("entryCount", fragmentManager.getBackStackEntryCount() + "");
            if (fragmentManager.getBackStackEntryCount() > 1) {
                //CLEAR all back stack entries to save memory
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment).addToBackStack(null);
            fragmentTransaction.commit();
            //return TRUE to Highlight menuItem
            return true;
        }
        //return FALSE: MenuItem should NOT be highlighted if no fragment was replaced
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (currentMenuItemID == R.id.ourAnimals) {
            finish();
            return;
        }
        //Back to "HOME" if BackPressed from other fragments than home
        //if the previous backStack is the FIRST Fragment(HOME), then just popBack to keep its state
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d("entryCount", fragmentManager.getBackStackEntryCount() + "");
        if (fragmentManager.getBackStackEntryCount() > 0) {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                navigationView.getMenu().getItem(0).setChecked(true);
                currentMenuItemID = R.id.ourAnimals;
                return;
            }
            getSupportFragmentManager().popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
            addInitFragment(new AnimalsFragment());
            navigationView.getMenu().getItem(0).setChecked(true);
            currentMenuItemID = R.id.ourAnimals;
        }
    }

    private void addInitFragment(@NonNull Fragment fragment) {
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(TAG_HOME_FRAGMENT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment != null) {
            fragmentTransaction.remove(homeFragment);
        }
        fragmentTransaction.add(R.id.container, fragment, TAG_HOME_FRAGMENT);
        fragmentTransaction.commit();
    }

    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public interface DrawerListener {
        void onDrawerOpen();
    }

    public void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }
}
