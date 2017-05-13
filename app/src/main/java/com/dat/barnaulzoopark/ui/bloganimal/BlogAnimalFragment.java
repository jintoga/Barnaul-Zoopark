package com.dat.barnaulzoopark.ui.bloganimal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DAT on 5/13/2017.
 */

public class BlogAnimalFragment
    extends BaseMvpFragment<BlogAnimalContract.View, BlogAnimalContract.UserActionListener>
    implements BlogAnimalContract.View {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_animal, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

    }

    @NonNull
    @Override
    public BlogAnimalContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new BlogAnimalPresenter(null, database);
    }
}
