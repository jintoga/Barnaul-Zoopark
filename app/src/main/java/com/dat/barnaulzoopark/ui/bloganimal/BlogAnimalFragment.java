package com.dat.barnaulzoopark.ui.bloganimal;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.BlogAnimal;
import com.dat.barnaulzoopark.model.User;
import com.dat.barnaulzoopark.ui.BZDialogBuilder;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by DAT on 5/13/2017.
 */

public class BlogAnimalFragment
    extends BaseMvpFragment<BlogAnimalContract.View, BlogAnimalContract.UserActionListener>
    implements BlogAnimalContract.View, BlogAnimalAdapter.ClickListener {

    @Bind(R.id.container)
    protected ViewGroup container;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.blogs)
    protected RecyclerView blogs;
    private BlogAnimalAdapter blogsAdapter;
    @Bind(R.id.emptyText)
    protected TextView emptyText;

    private MaterialDialog progressDialog;

    @Override
    public void bindBlogs(@NonNull List<BlogAnimal> result) {
        if (!result.isEmpty()) {
            blogsAdapter.setData(result);
        }
        setRecyclerViewVisibility(!result.isEmpty());
    }

    @Override
    public void onItemClicked(@NonNull BlogAnimal blogAnimal) {

    }

    private void setRecyclerViewVisibility(boolean shouldShow) {
        int recyclerViewVisibility =
            shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        int emptyTextVisibility = !shouldShow ? android.view.View.VISIBLE : android.view.View.GONE;
        blogs.setVisibility(recyclerViewVisibility);
        emptyText.setVisibility(emptyTextVisibility);
    }

    @Override
    public void onLoadBlogsError(@NonNull String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showSnackBar(message);
    }

    @Override
    public void onLoadBlogsProgress() {
        if (progressDialog == null) {
            progressDialog = BZDialogBuilder.createSimpleProgressDialog(getContext(),
                getString(R.string.loading_blogs));
        }
        progressDialog.setContent(getString(R.string.loading_blogs));
        progressDialog.show();
    }

    @Override
    public void onLoadBlogsSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void updateBlogs() {
        loadBlogs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog_animal, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadBlogs();
    }

    private void loadBlogs() {
        User user = BZApplication.get(getContext()).getUser();
        if (user != null) {
            presenter.loadBlogs(user.getSubscribedAnimals().keySet());
        } else {
            showSnackBar(container, getString(R.string.user_data_empty));
        }
    }

    private void init() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
                getString(R.string.animal_blog));
        }
        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        blogs.setLayoutManager(layoutManager);
        blogs.addItemDecoration(new SimpleListDividerDecorator(
            ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material),
            true));
        blogs.addItemDecoration(new ItemShadowDecorator(
            (NinePatchDrawable) ContextCompat.getDrawable(getContext(),
                R.drawable.material_shadow_z1)));

        blogsAdapter = new BlogAnimalAdapter(this);
        blogs.setAdapter(blogsAdapter);
    }

    @NonNull
    @Override
    public BlogAnimalContract.UserActionListener createPresenter() {
        EventBus eventBus = BZApplication.get(getContext()).getApplicationComponent().eventBus();
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new BlogAnimalPresenter(eventBus, database);
    }

    private void showSnackBar(String localizedMessage) {
        showSnackBar(container, localizedMessage);
    }
}
