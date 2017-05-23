package com.dat.barnaulzoopark.ui.ticketprice;

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
import com.dat.barnaulzoopark.model.TicketPrice;
import com.dat.barnaulzoopark.ui.BaseMvpFragment;
import com.dat.barnaulzoopark.ui.MainActivity;
import com.dat.barnaulzoopark.ui.ticketprice.adapters.TicketPriceAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;

/**
 * Created by DAT on 5/20/2017.
 */

public class TicketPriceFragment
    extends BaseMvpFragment<TicketPriceContract.View, TicketPriceContract.UserActionListener>
    implements TicketPriceContract.View {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.ticketPrices)
    protected RecyclerView ticketPrices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_price, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setupNavDrawerWithToolbar(toolbar,
                getString(R.string.ticket_price));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        ticketPrices.setLayoutManager(new LinearLayoutManager(getContext()));
        TicketPriceAdapter ticketPriceAdapter =
            new TicketPriceAdapter(TicketPrice.class, R.layout.item_ticket_price,
                TicketPriceAdapter.ViewHolder.class, presenter.getTicketPricesReference());
        ticketPrices.addItemDecoration(new SimpleListDividerDecorator(
            ContextCompat.getDrawable(getContext(), R.drawable.preference_list_divider_material), true));
        ticketPrices.addItemDecoration(new ItemShadowDecorator(
            (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        ticketPrices.setAdapter(ticketPriceAdapter);
    }

    @NonNull
    @Override
    public TicketPriceContract.UserActionListener createPresenter() {
        FirebaseDatabase database =
            BZApplication.get(getContext()).getApplicationComponent().fireBaseDatabase();
        return new TicketPricePresenter(database);
    }
}
