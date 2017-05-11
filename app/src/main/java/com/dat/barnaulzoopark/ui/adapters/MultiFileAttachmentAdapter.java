package com.dat.barnaulzoopark.ui.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
 * Created by DAT on 1/29/2017.
 */

public class MultiFileAttachmentAdapter
    extends RecyclerView.Adapter<MultiFileAttachmentAdapter.ViewHolder> {

    public static final int MAX_NUMBER_ATTACHMENT = 5;
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

    public MultiFileAttachmentAdapter(AttachmentListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_file_attachment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Attachment attachment = data.get(position);
        if (attachment != null) {
            holder.thumbnail.setImageDrawable(null);
            if (attachment.isFilled()) {
                Uri uri = Uri.parse(attachment.getUrl());
                holder.bindData(uri);
                holder.hideAddBtnAndShowRemoveBtn(true);
            } else {
                holder.bindEmptyData();
                holder.hideAddBtnAndShowRemoveBtn(false);
            }

            holder.attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSlotSelected(holder.getAdapterPosition());
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoved(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
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
        Log.d("removed", "removed:" + data.get(position).getUrl());
        if (isEditingMode) {
            final Attachment attachment = data.get(position);
            itemsToDelete.add(attachment);
            itemsToAdd.remove(position);
        }
        data.remove(position);

        notifyDataSetChanged();
    }

    public void fillSlot(int position, Attachment attachment) {
        data.set(position, attachment);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.attach)
        ImageButton attach;
        @Bind(R.id.remove)
        ImageButton remove;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            remove.setVisibility(View.GONE);
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
