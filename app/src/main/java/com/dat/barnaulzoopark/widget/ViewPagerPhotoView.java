package com.dat.barnaulzoopark.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by DAT on 5/15/2017.
 */
//Custom viewpager to deal with java.lang.IllegalArgumentException: pointerIndex out of range
public class ViewPagerPhotoView extends ViewPager {
    public ViewPagerPhotoView(Context context) {
        super(context);
    }

    public ViewPagerPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
