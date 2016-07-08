package com.dat.barnaulzoopark.ui.animalsdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;

public class AnimalsDetailActivity extends AppCompatActivity {

    public static void startActivity(Activity activity) {
        if (activity instanceof AnimalsDetailActivity) {
            return;
        }
        Intent intent = new Intent(activity, AnimalsDetailActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.container)
    protected FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_detail);
        ButterKnife.bind(this);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        /*FrameLayout.LayoutParams layoutParams =
            (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight();
        toolbar.setLayoutParams(layoutParams);*/
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithTransition(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishWithTransition(true);
                break;
            default:
                return false;
        }
        return true;
    }

    public void finishWithTransition(boolean withAnimation) {
        finish();
        if (withAnimation) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
