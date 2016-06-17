package com.dat.barnaulzoopark.widget.SearchView;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dat.barnaulzoopark.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyen on 6/14/2016.
 */
public class SuggestionsAdapter extends ArrayAdapter<String> {

    private final static int MAX_NUMBER_SUGGESTIONS = 7;

    private List<String> suggestions;
    private List<String> suggestionsFromAssets;
    private String keyword;

    public SuggestionsAdapter(Context context) {
        super(context, R.layout.search_view_suggestion_item);
        //suggestionsFromAssets =
        //    Arrays.asList(context.getResources().getStringArray(R.array.suggestions));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.search_view_suggestion_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.suggestion);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (suggestions.get(position) != null) {
            final String currentData = suggestions.get(position);
            holder.mTextView.setText(getColoredKeywordSuggestion(currentData));
        }
        return convertView;
    }

    public void filterSuggestions(CharSequence charSequence) {
        List<String> results = new ArrayList<>();
        if (charSequence != null && !charSequence.toString().trim().isEmpty()) {
            this.keyword = charSequence.toString();
            for (String item : suggestionsFromAssets) {
                if (item.toLowerCase().startsWith(keyword)) {
                    results.add(item);
                }
            }
        }
        setData(results);
    }

    public void setData(List<String> data) {
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        suggestions.clear();
        suggestions.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (suggestions == null) {
            return;
        }
        suggestions.clear();
        notifyDataSetChanged();
    }

    public String getValueAt(int position) {
        return suggestions.get(position);
    }

    private Spannable getColoredKeywordSuggestion(String suggestion) {
        Spannable result = new SpannableString(suggestion);
        result.setSpan(new ForegroundColorSpan(
                getContext().getResources().getColor(R.color.search_view_secondary_color)), 0,
            keyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public int getCount() {
        if (suggestions != null) {
            return suggestions.size() > MAX_NUMBER_SUGGESTIONS ? MAX_NUMBER_SUGGESTIONS
                : suggestions.size();
        }
        return 0;
    }

    static class ViewHolder {
        public TextView mTextView;
    }
}