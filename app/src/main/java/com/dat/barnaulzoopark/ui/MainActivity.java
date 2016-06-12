package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.PhotoAlbumsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.drawer)
    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavDrawer();
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Closing drawer on item click
        drawerLayout.closeDrawers();

        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case R.id.ourAnimals:
                return true;
            case R.id.photoGallery:
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new PhotoAlbumsFragment());
                fragmentTransaction.commit();
                return true;
            default:
                return true;
        }
    }


}
