package com.dat.barnaulzoopark.ui.sponsors;

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
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.BZApplication;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Sponsor;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.sponsors.adapters.SponsorsAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

/**
 * Created by DAT on 6/4/2017.
 */

public class SponsorsFragment
    extends BaseMvpFragment<SponsorsContract.View, SponsorsContract.UserActionListener>
    implements SponsorsContract.View {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.sponsors)
    protected RecyclerView sponsors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sponsors, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
                getString(R.string.sponsors));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        sponsors.setLayoutManager(new LinearLayoutManager(getContext()));
        SponsorsAdapter sponsorsAdapter = new SponsorsAdapter(Sponsor.class, R.layout.item_sponsor,
            SponsorsAdapter.ViewHolder.class, presenter.getSponsorsReference());
        sponsors.addItemDecoration(new SimpleListDividerDecorator(
            ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material),
            true));
        sponsors.addItemDecoration(new ItemShadowDecorator(
            (NinePatchDrawable) ContextCompat.getDrawable(getContext(),
                R.drawable.material_shadow_z1)));
        sponsors.setAdapter(sponsorsAdapter);
    }

    @NonNull
    @Override
    public SponsorsContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new SponsorsPresenter(database);
    }
}
