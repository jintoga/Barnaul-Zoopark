package com.dat.barnaulzoopark.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.events.LoggedInEvent;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.ui.animals.animalsfragment.AnimalsFragment;
import com.dat.barnaulzoopark.ui.news.NewsFragment;
import com.dat.barnaulzoopark.ui.photoandvideo.PhotoAndVideoFragment;
import com.dat.barnaulzoopark.ui.startup.StartupActivity;
import com.dat.barnaulzoopark.ui.userprofile.UserProfileContract;
import com.dat.barnaulzoopark.ui.userprofile.UserProfilePresenter;
import com.dat.barnaulzoopark.ui.zoomap.ZooMapFragment;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import org.greenrobot.eventbus.EventBus;

public class MainActivity
    extends BaseMvpActivity<UserProfileContract.View, UserProfileContract.UserActionListener>
    implements UserProfileContract.View, OnNavigationItemSelectedListener,
    DrawerLayout.DrawerListener {

    private static final int GALLERY_REQUEST = 1;
    private static final String KEY_IS_GUEST = "IS_GUEST";
    private static final String TAG = MainActivity.class.getName();
    @Bind(R.id.navigation_view)
    protected NavigationView navigationView;
    @Bind(R.id.drawer)
    protected DrawerLayout drawerLayout;
    protected SimpleDraweeView userPhoto;
    protected ProgressBar loadingPhoto;
    protected TextView userName;
    protected TextView userEmail;
    protected ImageView logButton;
    private int currentMenuItemID = -1;
    DrawerListener drawerListener;

    private static final String TAG_HOME_FRAGMENT = "HOME";
    private boolean isLoggedIn = false;

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

    public static void start(@NonNull Context context) {
        if (context instanceof MainActivity) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_IS_GUEST, true);
        context.startActivity(intent);
    }

    @Override
    public void bindUserData(@NonNull User user) {
        if (user.getPhoto() != null) {
            Uri photoUri = Uri.parse(user.getPhoto());
            userPhoto.setImageURI(photoUri);
        }
        String name = user.getName();
        if (user.isAdmin()) {
            name += "(admin)";
        }
        EventBus.getDefault().post(new LoggedInEvent(user.isAdmin()));
        userName.setText(name);
        userEmail.setText(user.getEmail());
        logButton.setImageResource(R.drawable.ic_logout);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BZDialogBuilder.createVerifyLogoutDialog(MainActivity.this)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                            @NonNull DialogAction which) {
                            FirebaseAuth.getInstance().signOut();
                            SharedPreferences.Editor editor =
                                getSharedPreferences(BZApplication.BZSharedPreference, MODE_PRIVATE)
                                    .edit();
                            editor.putBoolean(BZApplication.KEY_IS_LOGGED_IN, false);
                            editor.apply();
                            goToStartUp();
                        }
                    })
                    .show();
            }
        });
    }

    @Override
    public void bindUserDataAsGuest() {
        Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
            .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
        userPhoto.setImageURI(uri);
        userName.setText(getString(R.string.dear_visitor));
        userEmail.setText(getString(R.string.welcome_to_our_zoo));
        logButton.setImageResource(R.drawable.ic_login);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartUp();
            }
        });
    }

    @Override
    public void showUpdateProfileError(@NonNull String error) {
        loadingPhoto.setVisibility(View.GONE);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUpdateProfileProgress() {
        loadingPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUpdateProfileSuccess() {
        loadingPhoto.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticate();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupNavDrawer();
        if (savedInstanceState == null) {
            addInitFragment(new NewsFragment());
            currentMenuItemID = R.id.news;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadUserData();
    }

    @NonNull
    @Override
    public UserProfileContract.UserActionListener createPresenter() {
        FirebaseAuth auth = BZApplication.get(this).getApplicationComponent().firebaseAuth();
        FirebaseDatabase database =
            BZApplication.get(this).getApplicationComponent().fireBaseDatabase();
        FirebaseStorage storage =
            BZApplication.get(this).getApplicationComponent().fireBaseStorage();
        return new UserProfilePresenter(auth, database, storage);
    }

    private void authenticate() {
        SharedPreferences sharedPreferences =
            getSharedPreferences(BZApplication.BZSharedPreference, MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean(BZApplication.KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            return;
        }
        boolean isGuest = getIntent().getBooleanExtra(KEY_IS_GUEST, false);
        if (!isGuest) {
            goToStartUp();
        }
    }

    private void setupNavDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(this);
        //Views in Drawer's header
        userPhoto = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.userPhoto);
        loadingPhoto =
            (ProgressBar) navigationView.getHeaderView(0).findViewById(R.id.loadingPhoto);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) {
                    presenter.browseProfilePicture(MainActivity.this, GALLERY_REQUEST);
                }
            }
        });
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedIn) {
                    //Edit userName
                    MaterialDialog dialog =
                        BZDialogBuilder.createEditUserNameDialog(MainActivity.this,
                            userName.getText().toString())
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog,
                                    @NonNull DialogAction which) {
                                    if (dialog.getInputEditText() != null) {
                                        presenter.updateUserName(
                                            dialog.getInputEditText().getText().toString());
                                    }
                                }
                            })
                            .build();
                    View positive = dialog.getActionButton(DialogAction.POSITIVE);
                    positive.setEnabled(false);
                    dialog.show();
                }
            }
        });
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        logButton = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.logButton);
    }

    private void goToStartUp() {
        finish();
        StartupActivity.start(this);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Closing drawer on item click
        drawerLayout.closeDrawers();
        if (currentMenuItemID == menuItem.getItemId()) {
            return false;
        }
        currentMenuItemID = menuItem.getItemId();
        Fragment fragment = null;
        //Check to see which item was being clicked and perform appropriate action
        switch (menuItem.getItemId()) {
            case R.id.news:
                getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                addInitFragment(new NewsFragment());
                return true;
            case R.id.ourAnimals:
                fragment = new AnimalsFragment();
                break;
            case R.id.photoAndVideo:
                fragment = new PhotoAndVideoFragment();
                break;
            case R.id.zooMap:
                Log.d(TAG, "ZOO MAP");
                fragment = new ZooMapFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Log.d(TAG, fragmentManager.getBackStackEntryCount() + "");
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
        if (currentMenuItemID == R.id.news) {
            finish();
            return;
        }
        //Back to "HOME" if BackPressed from other fragments than home
        //if the previous backStack is the FIRST Fragment(HOME), then just popBack to keep its state
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(TAG, fragmentManager.getBackStackEntryCount() + "");
        if (fragmentManager.getBackStackEntryCount() > 0) {
            if (fragmentManager.getBackStackEntryCount() == 1) {
                getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                navigationView.getMenu().getItem(0).setChecked(true);
                currentMenuItemID = R.id.news;
                return;
            }
            getSupportFragmentManager().popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
            addInitFragment(new NewsFragment());
            navigationView.getMenu().getItem(0).setChecked(true);
            currentMenuItemID = R.id.news;
            return;
        }
        super.onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            CropImage.activity(data.getData())
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                presenter.updateProfilePicture(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, error.getLocalizedMessage());
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dispatchTouchEventListener != null) {
            dispatchTouchEventListener.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    private DispatchTouchEventListener dispatchTouchEventListener;

    public void setDispatchTouchEventListener(
        DispatchTouchEventListener dispatchTouchEventListener) {
        this.dispatchTouchEventListener = dispatchTouchEventListener;
    }

    public interface DispatchTouchEventListener {
        void dispatchTouchEvent(MotionEvent ev);
    }
}
