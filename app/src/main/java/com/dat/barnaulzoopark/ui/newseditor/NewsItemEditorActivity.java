package com.dat.barnaulzoopark.ui.newseditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by DAT on 1/29/2017.
 */

public class NewsItemEditorActivity
    extends BaseMvpActivity<NewsItemEditorContract.View, NewsItemEditorContract.UserActionListener>
    implements NewsItemEditorContract.View {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    public static void start(Context context) {
        if (context instanceof NewsItemEditorActivity) {
            return;
        }
        Intent intent = new Intent(context, NewsItemEditorActivity.class);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public NewsItemEditorContract.UserActionListener createPresenter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return new NewsItemEditorPresenter(database, storage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item_editor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_item_editor, menu);
        return true;
    }
}
