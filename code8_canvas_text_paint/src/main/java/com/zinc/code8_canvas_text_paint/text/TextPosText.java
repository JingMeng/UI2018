package com.zinc.code8_canvas_text_paint.text;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/25 涓嬪崍11:19
 * desc         :
 * version      :
 */
public class TextPosText extends BaseDrawView {

    private static final String CONTENT = "鐚涚寷鐨勫皬鐩嗗弸";

    private static final float[] pos1 = new float[]{
            -300, -600,
            -250, -500,
            -200, -400,
            -150, -300,
            -100, -200,
            -50, -100,
    };

    private static final float[] pos2 = new float[]{
            -300, 100,
            -250, 200,
            -200, 300,
            -150, 400,
            -100, 500,
    };

    private int mTextSize;

    public TextPosText(Context context) {
        super(context);
    }

    public TextPosText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextPosText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mTextSize = dpToPx(14);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mColor1);
        mPaint.setTextSize(mTextSize);

        canvas.drawPosText(CONTENT, pos1, mPaint);

        mPaint.setColor(mColor2);
        mPaint.setTextSize(mTextSize);
        canvas.drawPosText(CONTENT.toCharArray(), 1, 4, pos2, mPaint);

    }

}

