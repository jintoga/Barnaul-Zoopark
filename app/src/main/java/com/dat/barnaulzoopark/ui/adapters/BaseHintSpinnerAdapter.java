package com.dat.barnaulzoopark.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 5/10/2017.
 */

public abstract class BaseHintSpinnerAdapter<T> extends BaseAdapter {
    private Context context;
    protected List<T> data;
    private String hint;

    protected BaseHintSpinnerAdapter(@NonNull Context context, @NonNull List<T> data) {
        this.context = context;
        this.data = new ArrayList<>();
        this.data.add(null); //Hint
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    @Nullable
    public T getItem(int position) {
        return data.get(position);
    }

    @NonNull
    public List<T> getData() {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected abstract String getItemStringValue(@NonNull T item);

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return getHintView(parent);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        T item = getItem(position);
        if (item != null) {
            textView.setText(getItemStringValue(item));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return getDropDownHintView(parent);
        }
        convertView = LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        T item = getItem(position);
        if (item != null) {
            textView.setText(getItemStringValue(item));
        }
        return convertView;
    }

    private View getHintView(ViewGroup parent) {
        View view =
            LayoutInflater.from(context).inflate(R.layout.spinner_header_layout, parent, false);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        if (textView != null && hint != null) {
            textView.setText(hint);
        }
        return view;
    }

    private View getDropDownHintView(ViewGroup parent) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.spinner_dropdown_header_layout, parent, false);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        if (textView != null && hint != null) {
            textView.setText(hint);
        }
        return view;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
