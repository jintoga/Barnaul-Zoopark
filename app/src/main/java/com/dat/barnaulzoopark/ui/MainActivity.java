package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.animals.AnimalsFragment;
import com.dat.barnaulzoopark.ui.gallery.PhotoAlbumsFragment;

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
            FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new AnimalsFragment());
            fragmentTransaction.commit();
            currentMenuItemID = R.id.ourAnimals;
        }
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
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
            FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
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
