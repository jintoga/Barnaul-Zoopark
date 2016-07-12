package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
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

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.animals.AnimalsFragment;
import com.dat.barnaulzoopark.ui.gallery.PhotoAlbumsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.drawer)
    protected DrawerLayout drawerLayout;
    private int currentMenuItemID = -1;
    DrawerListener drawerListener;

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
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            currentMenuItemID = R.id.ourAnimals;
        }
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
    }

    public void setupNavDrawerWithToolbar(Toolbar toolbar) {
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_closed
        );
        drawerLayout.addDrawerListener(mDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
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
                fragment = new AnimalsFragment();
                break;
            case R.id.photoGallery:
                fragment = new PhotoAlbumsFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            //Back to "home" if BackPressed from other fragments than home
            //if the previous backStack is the FIRST Fragment, then just popBack to keep its state
            if (fragmentManager.getBackStackEntryCount() == 2) {
                navigationView.getMenu().getItem(0).setChecked(true);
                currentMenuItemID = R.id.ourAnimals;
                Log.d("HIGHLIGHT", "HOME HIGHLIGHTED");
                super.onBackPressed();
                return;
            }
            //otherwise navigate to the FIRST Fragment
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.getMenu().getItem(0).setChecked(true);
            currentMenuItemID = R.id.ourAnimals;
            return;
        }
        super.onBackPressed();
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
