package com.dat.barnaulzoopark.ui.animalspecieseditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.R;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

/**
 * Created by DAT on 5/2/2017.
 */

public class SpeciesEditorHeaderAdapter extends
    AbstractHeaderFooterWrapperAdapter<SpeciesEditorHeaderAdapter.HeaderViewHolder, SpeciesEditorHeaderAdapter.FooterViewHolder> {

    private IconClickListener iconClickListener;

    public SpeciesEditorHeaderAdapter(RecyclerView.Adapter adapter,
        IconClickListener iconClickListener) {
        setAdapter(adapter);
        this.iconClickListener = iconClickListener;
    }

    @Override
    public HeaderViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_species_editor_header, parent, false);
        return new SpeciesEditorHeaderAdapter.HeaderViewHolder(view);
    }

    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindHeaderItemViewHolder(HeaderViewHolder holder, int localPosition) {
        holder.attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iconClickListener != null) {
                    iconClickListener.onAttachIconClicked();
                }
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iconClickListener != null) {
                    iconClickListener.onRemoveIconClicked();
                }
            }
        });
    }

    @Override
    public void onBindFooterItemViewHolder(FooterViewHolder holder, int localPosition) {

    }

    @Override
    public int getHeaderItemCount() {
        return 1;
    }

    @Override
    public int getFooterItemCount() {
        return 0;
    }

    public interface IconClickListener {
        void onAttachIconClicked();

        void onRemoveIconClicked();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        EditText name;
        @Bind(R.id.description)
        EditText description;
        @Bind(R.id.category)
        Spinner category;
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.remove)
        ImageButton remove;
        @Bind(R.id.attach)
        ImageButton attach;

        private Uri iconUri;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindIcon(@NonNull Uri uri) {
            this.iconUri = uri;
            this.icon.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext()).load(iconUri).into(icon);
            updateButtons(true);
        }

        public void clearIcon() {
            this.iconUri = null;
            this.icon.setVisibility(View.GONE);
            this.icon.setImageDrawable(null);
            updateButtons(false);
        }

        private void updateButtons(boolean isFilled) {
            if (isFilled) {
                attach.setVisibility(View.GONE);
                remove.setVisibility(View.VISIBLE);
            } else {
                attach.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            }
        }

        @NonNull
        public String getName() {
            return name.getText().toString();
        }

        @NonNull
        public String getDescription() {
            return description.getText().toString();
        }

        @Nullable
        public Uri getIconUri() {
            return iconUri;
        }

        public void highlightRequiredFields() {
            if (name.getText().toString().isEmpty()) {
                name.setError("Input required");
            }
            if (description.getText().toString().isEmpty()) {
                description.setError("Input required");
            }
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
