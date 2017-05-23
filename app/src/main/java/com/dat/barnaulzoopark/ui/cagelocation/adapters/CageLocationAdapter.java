package com.dat.barnaulzoopark.ui.cagelocation.adapters;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.animal.Animal;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

public class CageLocationAdapter extends RecyclerView.Adapter<CageLocationAdapter.ViewHolder> {

    private List<Animal> animals = new ArrayList<>();
    private Location currentLocation;

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_cage_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (animals.get(position) != null && currentLocation != null) {
            holder.bindData(animals.get(position), currentLocation);
        }
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public void setData(@NonNull List<Animal> data, @NonNull Location currentLocation) {
        this.animals.clear();
        this.animals.addAll(data);
        this.currentLocation = currentLocation;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photo)
        SimpleDraweeView photo;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.distance)
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(@NonNull final Animal animal, @NonNull Location currentLocation) {
            photo.setImageURI(animal.getPhotoSmall());
            name.setText(animal.getName());
            float[] result = new float[3];
            Location.distanceBetween(animal.getLat(), animal.getLng(),
                currentLocation.getLatitude(), currentLocation.getLongitude(), result);
            distance.setText(String.format(itemView.getContext().getString(R.string.distance_away),
                String.valueOf(result[0])));
        }
    }
}
