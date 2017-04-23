package com.dat.barnaulzoopark.ui.animalcategoryeditor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;

/**
 * Created by DAT on 4/23/2017.
 */

public class CategoryEditorContentHeaderAdapter extends
    AbstractHeaderFooterWrapperAdapter<RecyclerView.ViewHolder, CategoryEditorContentHeaderAdapter.FooterViewHolder> {

    public CategoryEditorContentHeaderAdapter(RecyclerView.Adapter adapter) {
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

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
