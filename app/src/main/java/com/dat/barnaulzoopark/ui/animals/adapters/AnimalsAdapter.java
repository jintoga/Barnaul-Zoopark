package com.dat.barnaulzoopark.ui.animals.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Species;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 6/20/2016.
 */
public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.ViewHolder> {

    private List<Species> data = new ArrayList<>();
    private AnimalsAdapterListener listener;

    public AnimalsAdapter(AnimalsAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data.get(position) != null) {
            final Species species = data.get(position);
            holder.bindData(species);
            holder.clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSpeciesSelected(species);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(@NonNull List<Species> speciesList) {
        data.clear();
        data.addAll(speciesList);
        notifyDataSetChanged();
    }

    public interface AnimalsAdapterListener {
        void onSpeciesSelected(@NonNull Species species);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.thumbnail)
        protected ImageView thumbnail;
        @Bind(R.id.species)
        protected TextView name;
        @Bind(R.id.clickable)
        protected View clickable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(@NonNull Species species) {
            name.setText(species.getName());
            if (species.getIcon() != null) {
                thumbnail.setImageURI(Uri.parse(species.getIcon()));
            }
        }
    }
}
