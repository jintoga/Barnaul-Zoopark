package com.dat.barnaulzoopark.ui.gallery.adapters.stack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.ui.gallery.model.PhotoAlbum;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DAT on 22-Feb-16.
 */
public class StackAlbumAdapter extends RecyclerView.Adapter<StackAlbumAdapter.ViewHolder> {

    private ArrayList<PhotoAlbum> data;
    private Context context;

    public StackAlbumAdapter(ArrayList<PhotoAlbum> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_stack_album_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, new ViewHolder.OnViewHolderClick() {
            @Override
            public void onItemClick(View caller) {

                Log.d("Click", "ClickPo");
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            PhotoAlbum photoAlbum = data.get(position);
            holder.stackAlbumView.setData(photoAlbum.getUrls());
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.stackAlbumView)
        protected StackAlbumView stackAlbumView;

        public OnViewHolderClick mListener;

        public ViewHolder(View itemView, OnViewHolderClick mListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mListener = mListener;
            stackAlbumView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof StackAlbumView) {
                mListener.onItemClick((StackAlbumView) view);
            }
        }

        interface OnViewHolderClick {
            public void onItemClick(View caller);
        }
    }


}
