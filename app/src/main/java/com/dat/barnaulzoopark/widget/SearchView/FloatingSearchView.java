package com.dat.barnaulzoopark.widget.SearchView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dat.barnaulzoopark.R;

/**
 * Created by DAT on 13-Jun-16.
 */
public class FloatingSearchView extends FrameLayout {

    private static final int MODE_HAMBURGER = 1;
    private static final int MODE_ARROW = 2;
    private int leftActionButtonMode = -1;
    private boolean isSearchViewOpen;
    private FrameLayout rootView;
    private EditText searchEditText;
    private CardView searchBar;
    private ImageButton leftActionButton;
    private ImageButton clear;
    private View backgroundView;
    private LinearLayout container;
    private ListView suggestions;
    private SuggestionsAdapter suggestionsAdapter;

    private SearchViewListener searchViewListener;
    private SearchViewDrawerListener searchViewDrawerListener;
    private SearchViewFocusedListener searchViewFocusedListener;

    private boolean collapsingSuggestions = false;
    private boolean isSearchViewFocused = false;

    public FloatingSearchView(Context context) {
        super(context);
        init();
        setEvents();
    }

    public FloatingSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawables();
        init();
        setEvents();
        initSearchView();
    }

    public void setBackgroundView(View backgroundView) {
        this.backgroundView = backgroundView;
        if (this.backgroundView != null) {
            this.backgroundView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuBtnDrawable.animateDrawable(MenuArrowDrawable.ARROW_TO_HAMBURGER);
                    searchViewFocusedListener.onSearchViewEditTextLostFocus();
                    closeSearchView();
                }
            });
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_search_view, this, true);
        rootView = (FrameLayout) findViewById(R.id.search_layout);
        searchBar = (CardView) findViewById(R.id.search_bar);
        leftActionButton = (ImageButton) findViewById(R.id.left_action_button);
        initLeftActionButton();
        searchEditText = (EditText) findViewById(R.id.et_search);
        clear = (ImageButton) findViewById(R.id.action_clear);
        suggestions = (ListView) findViewById(R.id.suggestion_list);
        suggestionsAdapter = new SuggestionsAdapter(getContext());
        suggestions.setAdapter(suggestionsAdapter);
        container = (LinearLayout) findViewById(R.id.container);
        LayoutTransition layoutTransition = container.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        layoutTransition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view,
                int transitionType) {
                //Ignore
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view,
                int transitionType) {
                if (collapsingSuggestions) {
                    Log.d("collapsingSuggestions", "collapsingSuggestions");
                    closeSearchBar();
                    collapsingSuggestions = false;
                }
            }
        });
    }

    private void initLeftActionButton() {
        leftActionButton.setImageDrawable(mMenuBtnDrawable);
        leftActionButtonMode = MODE_HAMBURGER;
    }

    private void setEvents() {
        leftActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                leftActionButtonToggle();
            }
        });
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
            }
        });
        suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", (String) parent.getItemAtPosition(position));
            }
        });

        searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchViewFocused = hasFocus;
                if (hasFocus) {
                    leftActionButtonMode = MODE_ARROW;
                    mMenuBtnDrawable.animateDrawable(MenuArrowDrawable.HAMBURGER_TO_ARROW);
                    searchViewFocusedListener.onSearchViewEditTextFocus();
                    if (backgroundView != null) {
                        backgroundView.setVisibility(VISIBLE);
                        backgroundView.requestFocus();
                    }
                } else {
                    leftActionButtonMode = MODE_HAMBURGER;
                    mMenuBtnDrawable.animateDrawable(MenuArrowDrawable.ARROW_TO_HAMBURGER);

                    //hide SOFT KEYBOARD
                    InputMethodManager inputMethodManager =
                        (InputMethodManager) getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    private void leftActionButtonToggle() {
        if (leftActionButtonMode == MODE_HAMBURGER) {
            searchViewDrawerListener.onNavigationDrawerOpen();
        } else if (leftActionButtonMode == MODE_ARROW) {
            searchViewDrawerListener.onNavigationDrawerClosed();
            clearSearchView();
        }
    }

    private void initSearchView() {

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence keyword, int start, int before, int count) {
                suggestionsAdapter.filterSuggestions(keyword);
                FloatingSearchView.this.onTextChanged(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private MenuArrowDrawable mMenuBtnDrawable;

    private void initDrawables() {
        mMenuBtnDrawable = new MenuArrowDrawable(getContext());
        mMenuBtnDrawable.setColor(getContext().getResources().getColor(R.color.black));
    }

    public void setSearchViewListener(SearchViewListener searchViewListener) {
        this.searchViewListener = searchViewListener;
    }

    public void setSearchViewFocusedListener(SearchViewFocusedListener searchViewFocusedListener) {
        this.searchViewFocusedListener = searchViewFocusedListener;
    }

    public void setSearchViewDrawerListener(SearchViewDrawerListener searchViewDrawerListener) {
        this.searchViewDrawerListener = searchViewDrawerListener;
    }

    public boolean isSearchViewFocused() {
        return isSearchViewFocused;
    }

    public void openSearchView() {
        // If search is already open, just return.
        if (isSearchViewOpen) {
            return;
        }

        searchEditText.requestFocus();

        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (backgroundView != null) {
                    backgroundView.setVisibility(VISIBLE);
                    backgroundView.requestFocus();
                }
                //After SearchBar is revealed if keyword is not empty then open suggestions section
                if (!searchEditText.getText().toString().isEmpty()) {
                    suggestionsAdapter.filterSuggestions(searchEditText.getText());
                    FloatingSearchView.this.onTextChanged(searchEditText.getText());
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootView.setVisibility(View.VISIBLE);
            AnimationUtils.circleRevealView(searchBar, listenerAdapter);
        } else {
            AnimationUtils.fadeInView(rootView);
        }

        // Call listener if we have one
        if (searchViewListener != null) {
            searchViewListener.onSearchViewOpen();
        }

        isSearchViewOpen = true;
    }

    public void clearSearchView() {
        searchEditText.setText("");
        searchEditText.clearFocus();
        if (backgroundView != null) {
            backgroundView.setVisibility(GONE);
        }
    }

    public void closeSearchView() {
        // If we're already closed, just return.
        if (!isSearchViewOpen) {
            return;
        }

        if (suggestionsAdapter.getCount() == 0) {
            // Suggestions section is empty(collapsed)
            // just close the SearchBar
            closeSearchBar();
        } else {
            // Suggestions section is expanded
            // Clear text, values, and focus.
            // closeSearchBar should be called after Suggestions section is collapsed
            suggestionsAdapter.clearData();
            collapsingSuggestions = true;
        }
    }

    private void closeSearchBar() {

        final View v = rootView;

        AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // After the animation is done. Hide the root view.
                v.setVisibility(View.GONE);
                if (backgroundView != null) {
                    backgroundView.setVisibility(View.GONE);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimationUtils.circleHideView(searchBar, listenerAdapter);
        } else {
            AnimationUtils.fadeOutView(rootView);
        }

        if (searchViewListener != null) {
            searchViewListener.onSearchViewClosed();
        }
        isSearchViewOpen = false;
    }

    private void onTextChanged(CharSequence newText) {

        // If the text is not empty, show the empty button and hide the voice button
        if (!TextUtils.isEmpty(searchEditText.getText())) {
            displayClearButton(true);
        } else {
            displayClearButton(false);
        }
    }

    private void displayClearButton(boolean display) {
        clear.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    public interface SearchViewListener {
        void onSearchViewOpen();

        void onSearchViewClosed();
    }

    public interface SearchViewDrawerListener {
        void onNavigationDrawerOpen();

        void onNavigationDrawerClosed();
    }

    public interface SearchViewFocusedListener {
        void onSearchViewEditTextFocus();

        void onSearchViewEditTextLostFocus();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isSearchViewFocused = isSearchViewFocused;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        isSearchViewFocused = ss.isSearchViewFocused;
    }

    static class SavedState extends View.BaseSavedState {
        boolean isSearchViewFocused;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isSearchViewFocused = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (isSearchViewFocused ? 1 : 0));
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
    }
}
