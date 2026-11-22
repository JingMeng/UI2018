package com.zinc.code8_canvas_text_paint.text;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * author       : zinc
 * time         : 2019/4/22 涓婂崍9:23
 * desc         : 鐢荤煩闃? * version      :
 */
public class TextView extends BaseDrawView {

    private static final String CONTENT = "zinc 鐚涚寷鐨勫皬鐩嗗弸";

    private static final CharSequence SEQ = "https://blog.csdn.net/weixin_37625173";

    private static final char[] C = "https://github.com/zincPower/UI2018".toCharArray();

    private int mTextSize;

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        setPaint(mColor1, mLineWidth);
        mPaint.setTextSize(mTextSize);

        int x = -300;

        canvas.drawText(CONTENT, x, -500, mPaint);
        canvas.drawText(CONTENT, 3, CONTENT.length(), x, -400, mPaint);


        canvas.drawText(C, 0, C.length, x, -100, mPaint);
        canvas.drawText(C, 5, 10, x, 0, mPaint);


        canvas.drawText(SEQ, 0, SEQ.length(), x, 300, mPaint);
        canvas.drawText(SEQ, 6, 20, x, 400, mPaint);

    }
}

