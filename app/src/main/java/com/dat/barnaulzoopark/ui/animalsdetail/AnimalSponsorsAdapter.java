package com.dat.barnaulzoopark.ui.animalsdetail;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Sponsor;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 6/7/2017.
 */

class AnimalSponsorsAdapter extends RecyclerView.Adapter<AnimalSponsorsAdapter.ViewHolder> {
    private List<Sponsor> data = new ArrayList<>();
    private SponsorClickListener listener;

    AnimalSponsorsAdapter(SponsorClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_animal_sponsor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data.get(position) != null) {
            final Sponsor sponsor = data.get(position);
            holder.bindData(sponsor);
            holder.clickable.setOnClickListener(v -> listener.onSponsorClicked(sponsor));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@NonNull List<Sponsor> sponsors) {
        data.clear();
        data.addAll(sponsors);
        notifyDataSetChanged();
    }

    interface SponsorClickListener {
        void onSponsorClicked(@NonNull Sponsor sponsor);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        protected SimpleDraweeView thumbnail;
        @Bind(R.id.clickable)
        protected View clickable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(@NonNull Sponsor sponsor) {
            if (sponsor.getLogo() != null) {
                thumbnail.setImageURI(sponsor.getLogo());
            } else {
                Uri uri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.img_photo_gallery_placeholder)).build();
                thumbnail.setImageURI(uri);
            }
        }
    }
}
