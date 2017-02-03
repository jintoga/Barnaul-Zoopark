package com.dat.barnaulzoopark.ui.newseditor;

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
import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 1/29/2017.
 */

class MultiFileAttachmentAdapter
    extends RecyclerView.Adapter<MultiFileAttachmentAdapter.ViewHolder> {

    public static final int MAX_NUMBER_ATTACHMENT = 5;
    private List<Attachment> data = new ArrayList<>();

    interface AttachmentListener {
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
                holder.bindData(attachment.getFilePath());
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

    void addEmptySlot() {
        if (data.size() < MAX_NUMBER_ATTACHMENT) {
            Attachment attachment = new Attachment();
            attachment.setFilePath("");
            attachment.setFilled(false);
            data.add(attachment);
        }
        notifyDataSetChanged();
    }

    void emptySlot(int position) {
        Log.d("removed", "removed:" + data.get(position).getFilePath());
        data.remove(position);
        notifyDataSetChanged();
        String res = "";
        for (Attachment item : data) {
            res += item.getFilePath() + ", ";
        }
        Log.d("data", "data:" + res);
    }

    void fillSlot(int position, Attachment attachment) {
        data.set(position, attachment);
        notifyDataSetChanged();
    }

    private boolean isImage(@NonNull String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.indexOf("image") == 0;
    }

    public List<Attachment> getData() {
        return data;
    }

    public void setData(List<Attachment> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
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

        void bindData(String name) {
            if (isImage(name)) {
                File imgFile = new File(name);
                if (imgFile.exists()) {
                    Glide.with(thumbnail.getContext()).load(imgFile).into(thumbnail);
                    thumbnail.setVisibility(View.VISIBLE);
                }
            }
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
