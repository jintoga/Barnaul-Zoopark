package com.dat.barnaulzoopark.Gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by DAT on 08-Feb-16.
 */
public class DynamicHeightImageView extends ImageView {

    private double mHeightRatio;
    private double mWidthRatio;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public void setWidthRatio(double ratio) {
        if (ratio != mWidthRatio) {
            mWidthRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    public double getWidthRatio() {
        return mWidthRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        } */
        if (mWidthRatio > 0.0) {
            // set the image views size
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = (int) (height * mWidthRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}