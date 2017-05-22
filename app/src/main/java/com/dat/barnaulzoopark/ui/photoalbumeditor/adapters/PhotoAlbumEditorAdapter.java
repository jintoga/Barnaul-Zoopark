package com.dat.barnaulzoopark.ui.photoalbumeditor.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/21/2017.
 */

public class PhotoAlbumEditorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FIRST_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    public static final int MAX_NUMBER_ATTACHMENT = 30;
    private List<Attachment> data = new ArrayList<>();

    private List<Attachment> itemsToDelete = new ArrayList<>();
    private List<Attachment> itemsToAdd = new ArrayList<>();

    private boolean isEditingMode = false;

    public boolean isModified() {
        if (itemsToDelete.size() > 0) {
            return true;
        }
        for (Attachment attachment : itemsToAdd) {
            if (attachment.getAttachmentUid() == null) {
                return true;
            }
        }
        return false;
    }

    public boolean isEditingMode() {
        return isEditingMode;
    }

    public void setEditingMode(boolean editingMode) {
        isEditingMode = editingMode;
    }

    public interface AttachmentListener {
        void onRemoved(int position);

        void onSlotSelected(int position);
    }

    private AttachmentListener listener;

    public PhotoAlbumEditorAdapter(AttachmentListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FIRST_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_album_editor_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_album_editor_attachment, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIRST_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Attachment attachment = data.get(getAttachItemPosition(position));
        if (attachment != null) {
            itemViewHolder.thumbnail.setImageDrawable(null);
            if (attachment.isFilled()) {
                Uri uri = Uri.parse(attachment.getUrl());
                itemViewHolder.bindData(uri);
                itemViewHolder.hideAddBtnAndShowRemoveBtn(true);
            } else {
                itemViewHolder.bindEmptyData();
                itemViewHolder.hideAddBtnAndShowRemoveBtn(false);
            }

            itemViewHolder.attach.setOnClickListener(
                v -> listener.onSlotSelected(holder.getAdapterPosition()));
            itemViewHolder.remove.setOnClickListener(
                v -> listener.onRemoved(holder.getAdapterPosition()));
        }
    }

    private int getAttachItemPosition(int position) {
        return position - 1;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void addEmptySlot() {
        if (data.size() < MAX_NUMBER_ATTACHMENT) {
            Attachment attachment = new Attachment();
            attachment.setUrl(null);
            attachment.setFilled(false);
            data.add(attachment);
        }
        notifyDataSetChanged();
    }

    public void emptySlot(int position) {
        Log.d("removed", "removed:" + data.get(getAttachItemPosition(position)).getUrl());
        if (isEditingMode) {
            final Attachment attachment = data.get(getAttachItemPosition(position));
            itemsToDelete.add(attachment);
            itemsToAdd.remove(getAttachItemPosition(position));
        }
        data.remove(getAttachItemPosition(position));

        notifyDataSetChanged();
    }

    public void fillSlot(int position, Attachment attachment) {
        data.set(getAttachItemPosition(position), attachment);
        if (isEditingMode) {
            itemsToAdd.add(attachment);
        }
        notifyDataSetChanged();
    }

    @NonNull
    public List<Attachment> getData() {
        return data;
    }

    public List<Attachment> getItemsToDelete() {
        return itemsToDelete;
    }

    public List<Attachment> getItemsToAdd() {
        return itemsToAdd;
    }

    public void setData(List<Attachment> data) {
        this.data.clear();
        this.data.addAll(data);
        if (isEditingMode) {
            itemsToAdd.addAll(data);
        }
        notifyDataSetChanged();
    }

    public boolean hasAttachment() {
        return data.get(0).isFilled();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private Uri iconUri;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.attach)
        ImageButton attach;
        @Bind(R.id.remove)
        ImageButton remove;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            remove.setVisibility(View.GONE);
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

        void bindData(Uri uri) {
            Glide.with(thumbnail.getContext()).load(uri).into(thumbnail);
            thumbnail.setVisibility(View.VISIBLE);
        }

        void bindEmptyData() {
            thumbnail.setVisibility(View.GONE);
        }

        void hideAddBtnAndShowRemoveBtn(boolean isFilled) {
            if (isFilled) {
                attach.setVisibility(View.GONE);
                remove.setVisibility(View.VISIBLE);
            } else {
                attach.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            }
        }
    }
}
