package com.dat.barnaulzoopark.ui.videoalbumeditor.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.model.Attachment;
import com.dat.barnaulzoopark.model.VideoAlbum;
import com.dat.barnaulzoopark.utils.ConverterUtils;
import com.dat.barnaulzoopark.widget.PrefixEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by DAT on 5/23/2017.
 */

public class VideoAlbumEditorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_FIRST_ITEM = 0;
    private static final int TYPE_ITEM = 1;

    public static final int MAX_NUMBER_ATTACHMENT = 30;
    private List<Attachment> data = new ArrayList<>();

    private List<Attachment> itemsToDelete = new ArrayList<>();
    private List<Attachment> itemsToAdd = new ArrayList<>();

    private Activity activity;

    public VideoAlbumEditorAdapter(Activity activity, AttachmentListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

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

        void onSlotSelected(String videoId, int position);
    }

    private AttachmentListener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FIRST_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album_editor_header, parent, false);
            return new HeaderViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_video_album_editor_attachment, parent, false);
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
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.timeCreated.setOnClickListener(v -> setTime(headerViewHolder));
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Attachment attachment = data.get(getAttachItemPosition(position));
        if (attachment != null) {
            itemViewHolder.video.setEnabled(!attachment.isFilled());
            if (attachment.isFilled()) {
                itemViewHolder.bindData(attachment.getUrl());
                itemViewHolder.hideAddBtnAndShowRemoveBtn(true);
            } else {
                itemViewHolder.bindEmptyData();
                itemViewHolder.hideAddBtnAndShowRemoveBtn(false);
            }

            itemViewHolder.attach.setOnClickListener(v -> {
                if (itemViewHolder.video.getText().length() == 0) {
                    itemViewHolder.video.setError("Input required");
                    return;
                }
                itemViewHolder.video.setEnabled(false);
                listener.onSlotSelected(itemViewHolder.video.getText().toString(),
                    getAttachItemPosition(holder.getAdapterPosition()));
            });
            itemViewHolder.remove.setOnClickListener(
                v -> listener.onRemoved(getAttachItemPosition(holder.getAdapterPosition())));
        }
    }

    private void setTime(@NonNull HeaderViewHolder headerViewHolder) {
        final Calendar calendar = Calendar.getInstance();
        if (headerViewHolder.selectedDateCreated != null) {
            calendar.setTime(headerViewHolder.selectedDateCreated);
        }
        DatePickerDialog datePickerDialog =
            DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    headerViewHolder.selectedDateCreated = calendar.getTime();
                    headerViewHolder.timeCreated.setText(
                        ConverterUtils.DATE_FORMAT.format(headerViewHolder.selectedDateCreated));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.show(activity.getFragmentManager(), "DatePickerDialog");
    }

    private int getAttachItemPosition(int position) {
        return position - 1;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void addEmptySlot() {
        if (data.size() + 1 < MAX_NUMBER_ATTACHMENT) {
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

    @NonNull
    public List<Attachment> getFilledData() {
        List<Attachment> res = new ArrayList<>();
        for (Attachment attachment : data) {
            if (attachment.isFilled()) {
                res.add(attachment);
            }
        }
        return res;
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

        @Bind(R.id.name)
        EditText name;
        @Bind(R.id.timeCreated)
        EditText timeCreated;

        Date selectedDateCreated;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            timeCreated.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        timeCreated.setError(null);
                    }
                }
            });
        }

        public void bindSelectedPhotoAlbum(@NonNull VideoAlbum videoAlbum) {
            name.setText(videoAlbum.getName());
            selectedDateCreated = new Date(videoAlbum.getTime());
            timeCreated.setText(ConverterUtils.DATE_FORMAT.format(selectedDateCreated));
        }

        @Nullable
        public Date getDate() {
            return selectedDateCreated;
        }

        @NonNull
        public String getName() {
            return name.getText().toString();
        }

        public void highlightRequiredFields() {
            if (name.getText().toString().isEmpty()) {
                name.setError("Input required");
            }
            if (timeCreated.getText().toString().isEmpty()) {
                timeCreated.setError("Input required");
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.attach)
        ImageButton attach;
        @Bind(R.id.remove)
        ImageButton remove;
        @Bind(R.id.video)
        PrefixEditText video;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            remove.setVisibility(View.GONE);
            video.setPrefix(itemView.getContext().getString(R.string.youtube_prefix));
        }

        public void bindData(String videoId) {
            video.setText(videoId);
        }

        void bindEmptyData() {
            video.setText("");
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
