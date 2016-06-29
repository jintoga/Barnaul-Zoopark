package com.dat.barnaulzoopark.widget.SearchView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private boolean isSearchViewOpen;
    private FrameLayout rootView;
    private EditText searchEditText;
    private CardView searchBar;
    private ImageButton back;
    private ImageButton clear;
    private View backgroundView;
    private LinearLayout container;
    private ListView suggestions;
    private SuggestionsAdapter suggestionsAdapter;

    private SearchViewListener searchViewListener;

    private SearchViewFocusedListener searchViewFocusedListener;

    private boolean collapsingSuggestions = false;

    public FloatingSearchView(Context context) {
        super(context);
        init();
    }

    public FloatingSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawables();
        init();
        initSearchView();
    }

    public void setBackgroundView(View backgroundView) {
        this.backgroundView = backgroundView;
        if (this.backgroundView != null) {
            this.backgroundView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
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
        back = (ImageButton) findViewById(R.id.action_back);
        back.setImageDrawable(mMenuBtnDrawable);
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

        setEvents();
    }

    private void setEvents() {
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchView();
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
                if (hasFocus) {
                    openMenuDrawable(mMenuBtnDrawable, true);
                    searchViewFocusedListener.onSearchViewEditTextFocus();
                    if (backgroundView != null) {
                        backgroundView.setVisibility(VISIBLE);
                        backgroundView.requestFocus();
                    }
                } else {
                    closeMenuDrawable(mMenuBtnDrawable, true);
                }
            }
        });
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

    private DrawerArrowDrawable mMenuBtnDrawable;

    private void initDrawables() {
        mMenuBtnDrawable = new DrawerArrowDrawable(getContext());
        mMenuBtnDrawable.setColor(getContext().getResources().getColor(R.color.black));
    }

    private void openMenuDrawable(final DrawerArrowDrawable drawerArrowDrawable, boolean withAnim) {
        if (withAnim) {
            ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (Float) animation.getAnimatedValue();
                    drawerArrowDrawable.setProgress(value);
                }
            });
            anim.setDuration(200);
            anim.start();
        } else {
            drawerArrowDrawable.setProgress(1.0f);
        }
    }

    private void closeMenuDrawable(final DrawerArrowDrawable drawerArrowDrawable,
        boolean withAnim) {
        if (withAnim) {
            ValueAnimator anim = ValueAnimator.ofFloat(1.0f, 0.0f);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    float value = (Float) animation.getAnimatedValue();
                    drawerArrowDrawable.setProgress(value);
                }
            });
            anim.setDuration(200);
            anim.start();
        } else {
            drawerArrowDrawable.setProgress(0.0f);
        }
    }

    public void setSearchViewListener(SearchViewListener searchViewListener) {
        this.searchViewListener = searchViewListener;
    }

    public void setSearchViewFocusedListener(SearchViewFocusedListener searchViewFocusedListener) {
        this.searchViewFocusedListener = searchViewFocusedListener;
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

    public interface SearchViewFocusedListener {
        void onSearchViewEditTextFocus();

        void onSearchViewEditTextLostFocus();
    }
}
