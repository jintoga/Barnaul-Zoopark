package com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.dat.barnaulzoopark.R;
import com.dat.barnaulzoopark.widget.InfiniteViewPagerWithCircularIndicator.InfiniteViewPager.InfiniteViewPager;

public class CircularIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private InfiniteViewPager mViewpager;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;
    private int mAnimatorResId = R.animator.scale_with_alpha;
    private int mAnimatorReverseResId = 0;
    private int mIndicatorBackgroundResId = R.drawable.ic_circular_indicator_white_radius;
    private int mIndicatorUnselectedBackgroundResId = R.drawable.ic_circular_indicator_white_radius;
    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;

    private int mLastPosition = -1;

    public CircularIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public CircularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr,
        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        checkIndicatorConfig(context);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CircularIndicator);
        mIndicatorWidth =
            typedArray.getDimensionPixelSize(R.styleable.CircularIndicator_ci_width, -1);
        mIndicatorHeight =
            typedArray.getDimensionPixelSize(R.styleable.CircularIndicator_ci_height, -1);
        mIndicatorMargin =
            typedArray.getDimensionPixelSize(R.styleable.CircularIndicator_ci_margin, -1);

        mAnimatorResId = typedArray.getResourceId(R.styleable.CircularIndicator_ci_animator,
            R.animator.scale_with_alpha);
        mAnimatorReverseResId =
            typedArray.getResourceId(R.styleable.CircularIndicator_ci_animator_reverse, 0);
        mIndicatorBackgroundResId =
            typedArray.getResourceId(R.styleable.CircularIndicator_ci_drawable,
                R.drawable.ic_circular_indicator_white_radius);
        mIndicatorUnselectedBackgroundResId =
            typedArray.getResourceId(R.styleable.CircularIndicator_ci_drawable_unselected,
                mIndicatorBackgroundResId);

        int orientation = typedArray.getInt(R.styleable.CircularIndicator_orientation, -1);
        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        int gravity = typedArray.getInt(R.styleable.CircularIndicator_gravity, -1);
        setGravity(gravity >= 0 ? gravity : Gravity.CENTER);

        typedArray.recycle();
    }

    /**
     * Create and configure Indicator in Java code.
     */
    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin,
            R.animator.scale_with_alpha, 0, R.drawable.ic_circular_indicator_white_radius,
            R.drawable.ic_circular_indicator_white_radius);
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin,
        @AnimatorRes int animatorId, @AnimatorRes int animatorReverseId,
        @DrawableRes int indicatorBackgroundId, @DrawableRes int indicatorUnselectedBackgroundId) {

        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;

        mAnimatorResId = animatorId;
        mAnimatorReverseResId = animatorReverseId;
        mIndicatorBackgroundResId = indicatorBackgroundId;
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId;

        checkIndicatorConfig(getContext());
    }

    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
            (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
            (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

        mAnimatorResId = (mAnimatorResId == 0) ? R.animator.scale_with_alpha : mAnimatorResId;

        mAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn.setDuration(0);

        mIndicatorBackgroundResId =
            (mIndicatorBackgroundResId == 0) ? R.drawable.ic_circular_indicator_white_radius
                : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId =
            (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId
                : mIndicatorUnselectedBackgroundResId;
    }

    private Animator createAnimatorOut(Context context) {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId);
    }

    private Animator createAnimatorIn(Context context) {
        Animator animatorIn;
        if (mAnimatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            animatorIn.setInterpolator(new ReverseInterpolator());
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
        return animatorIn;
    }

    public void setViewPager(InfiniteViewPager viewPager) {
        mViewpager = viewPager;
        if (mViewpager != null && mViewpager.getAdapter() != null) {
            mLastPosition = -1;
            createIndicators();
            mViewpager.setInternalPageChangeListener(mInternalViewPageChangeListener);
            mInternalViewPageChangeListener.onPageSelected(mViewpager.getCurrentItem());
        }
    }

    private final InfiniteViewPager.OnPageChangeListener mInternalViewPageChangeListener =
        new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mViewpager.getAdapter() == null || mViewpager.getAdapter().getCount() <= 0) {
                    return;
                }

                if (mAnimatorIn.isRunning()) {
                    mAnimatorIn.end();
                    mAnimatorIn.cancel();
                }

                if (mAnimatorOut.isRunning()) {
                    mAnimatorOut.end();
                    mAnimatorOut.cancel();
                }

                View currentIndicator;
                if (mLastPosition >= 0
                    && (currentIndicator = getChildAt(transformToActualPosition(mLastPosition)))
                    != null) {
                    currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
                    mAnimatorIn.setTarget(currentIndicator);
                    mAnimatorIn.start();
                }

                View selectedIndicator = getChildAt(transformToActualPosition(position));
                if (selectedIndicator != null) {
                    selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
                    mAnimatorOut.setTarget(selectedIndicator);
                    mAnimatorOut.start();
                }
                mLastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

    private void createIndicators() {
        removeAllViews();
        int count = mViewpager.getAdapter().getActualCount() / 2;
        if (count <= 0) {
            return;
        }
        int currentItem = mViewpager.getCurrentItem() / 2;

        for (int i = 0; i < count; i++) {
            if (currentItem == i) {
                addIndicator(mIndicatorBackgroundResId, mImmediateAnimatorOut);
            } else {
                addIndicator(mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn);
            }
        }
    }

    private void addIndicator(@DrawableRes int backgroundDrawableId, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }

        View Indicator = new View(getContext());
        Indicator.setBackgroundResource(backgroundDrawableId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);

        animator.setTarget(Indicator);
        animator.start();
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int transformToActualPosition(int position) {
        int result = position % mViewpager.getAdapter().getActualCount();
        return result % (mViewpager.getAdapter().getActualCount() / 2);
    }
}