package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 涓婂崍9:23
 * desc         : 鐢荤偣
 * version      :
 */
public class PointView extends BaseDrawView {

    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor1);
        mPaint.setStrokeWidth(dpToPx(5));
        canvas.drawPoint(100, 100, mPaint);
    }
}

