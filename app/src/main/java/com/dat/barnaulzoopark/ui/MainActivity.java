package com.dat.barnaulzoopark.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.PhotoAlbumsFragment;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.drawer)
    protected DrawerLayout drawerLayout;


    @Bind(R.id.app_bar_layout)
    protected AppBarLayout appBarLayout;

    @Bind(R.id.nestedScrollView)
    protected NestedScrollView nestedScrollView;

    @Bind(R.id.fabMenu)
    protected FloatingActionMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupNavDrawer();
    }


    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes com.dat
                        // .barnaulzoopark.ui.as we dont want anything to happen so we leave this
                        // blank
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open com.dat
                        // .barnaulzoopark.ui.as we dont want anything to happen so we leave this
                        // blank

                        super.onDrawerOpened(drawerView);
                    }
                };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset
                        == 0 - appBarLayout.getTotalScrollRange()) {     //fully collapsed
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (verticalOffset == 0) {       //fully expanded
                    toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_gradient));
                } else {
                    toolbar.setBackground(getResources().getDrawable(R.drawable.toolbar_gradient));
                }
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                if (Math.abs(scrollY) > mScrollOffset) {
                   /* Log.d("oldScrollY", oldScrollY + "");
                    Log.d("scrollY", scrollY + "");*/
                    if (scrollY > oldScrollY) {
                        //onScrollCallback.onScroll(true);
                        //Log.d("Hide", "hiding");
                        fabMenu.hideMenu(true);
                    } else {
                        //onScrollCallback.onScroll(false);
                        //Log.d("Show", "showing");
                        fabMenu.showMenu(true);
                    }
                }
            }
        });
    }

    private int mScrollOffset = 4;


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
