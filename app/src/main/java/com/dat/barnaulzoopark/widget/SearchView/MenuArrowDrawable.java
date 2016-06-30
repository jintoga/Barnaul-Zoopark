package com.dat.barnaulzoopark.widget.SearchView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;

/**
 * Created by Nguyen on 6/30/2016.
 */
public class MenuArrowDrawable extends DrawerArrowDrawable {

    private final ValueAnimator mMenuToArrowAnimator;
    private final ValueAnimator mArrowToMenuAnimator;

    public MenuArrowDrawable(Context context) {
        super(context);

        ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setPosition((float) animation.getAnimatedValue());
                    Log.d("Value:", (float) animation.getAnimatedValue() + "");
                }
            };

        mMenuToArrowAnimator = ValueAnimator.ofFloat(0f, 1f);
        mMenuToArrowAnimator.setDuration(200);
        mMenuToArrowAnimator.addUpdateListener(animatorUpdateListener);

        mArrowToMenuAnimator = ValueAnimator.ofFloat(1f, 0f);
        mArrowToMenuAnimator.setDuration(200);
        mArrowToMenuAnimator.addUpdateListener(animatorUpdateListener);
    }

    public void setPosition(float position) {
        if (position >= 1f) {
            setVerticalMirror(true);
        } else if (position <= 0f) {
            setVerticalMirror(false);
        }
        setProgress(position);
    }

    public float getPosition() {
        return getProgress();
    }

    public void animateDrawable(boolean menuToArrow) {
        if (menuToArrow && getPosition() >= 1f) return;
        if (!menuToArrow && getPosition() <= 0f) return;

        ValueAnimator animator = menuToArrow ? mMenuToArrowAnimator : mArrowToMenuAnimator;
        if (animator.isRunning()) animator.end();
        animator.start();
    }
}
