package com.dat.barnaulzoopark.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by DAT on 3/8/2017.
 */

public class PrefixEditText extends android.support.v7.widget.AppCompatEditText {
    private String prefix = "";
    private Rect prefixRect = new Rect(); // actual prefix size

    public PrefixEditText(Context context) {
        super(context);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getPaint().getTextBounds(prefix, 0, prefix.length(), prefixRect);
        prefixRect.right += getPaint().measureText(" "); // add some offset
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(prefix, super.getCompoundPaddingLeft(), getBaseline(), getPaint());
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public int getCompoundPaddingLeft() {
        return super.getCompoundPaddingLeft() + prefixRect.width();
    }
}
