package com.dat.barnaulzoopark.ui.animalcategoryeditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorHeaderAdapter extends
    AbstractHeaderFooterWrapperAdapter<RecyclerView.ViewHolder, CategoryEditorHeaderAdapter.FooterViewHolder> {

    public CategoryEditorHeaderAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category_editor_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public FooterViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindHeaderItemViewHolder(RecyclerView.ViewHolder holder, int localPosition) {

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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        EditText name;
        @Bind(R.id.description)
        EditText description;
        @Bind(R.id.icon)
        ImageView icon;
        private Uri iconUri;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
