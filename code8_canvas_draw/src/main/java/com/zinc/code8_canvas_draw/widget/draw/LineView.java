package com.zinc.code8_canvas_draw.widget.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 涓婂崍9:23
 * desc         : 鐢荤嚎
 * version      :
 */
public class LineView extends BaseDrawView {

    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLine(-200, -200,
                0, 0, mPaint);

    }
}

