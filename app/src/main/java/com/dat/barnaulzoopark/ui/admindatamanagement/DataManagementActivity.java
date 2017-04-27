package com.dat.barnaulzoopark.ui.admindatamanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;

/**
 * Created by DAT on 4/28/2017.
 */

public class DataManagementActivity extends
    BaseMvpActivity<DataManagementContract.View, DataManagementContract.UserActionListener> {

    private static final String EXTRA_DATA_TYPE = "EXTRA_DATA_TYPE";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.list)
    protected RecyclerView list;

    public static <T> void start(Context context, @NonNull Class<T> clazz) {
        if (context instanceof DataManagementActivity) {
            return;
        }
        Intent intent = new Intent(context, DataManagementActivity.class);
        intent.putExtra(EXTRA_DATA_TYPE, clazz);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_management);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        init();
    }

    private void init() {
        list.setLayoutManager(new LinearLayoutManager(this));
        
    }

    @NonNull
    @Override
    public DataManagementContract.UserActionListener createPresenter() {
        return new DataManagementPresenter();
    }
}
