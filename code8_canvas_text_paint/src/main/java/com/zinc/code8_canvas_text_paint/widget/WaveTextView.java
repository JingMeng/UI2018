package com.zinc.code8_canvas_text_paint.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.zinc.code8_canvas_text_paint.R;
import com.zinc.lib_base.BaseView;
import com.zinc.lib_base.UIUtils;

/**
 * author       : zinc
 * time         : 2019/5/15 涓嬪崍11:07
 * desc         :
 * version      :
 */
public class WaveTextView extends BaseView {

    private static final int LENGTH_TIMES = 3;
    private static final int POINT_COUNT = 200;
    private static final float A = 200;

    private String mContent;

    private Paint mPaint;
    private Path mPath;

    private float mA = 0;

    private float mLength;
    private float mStepLength;
    private boolean mIsShowLine;

    private float m = 0;

    public WaveTextView(Context context) {
        super(context);
    }

    private float mLineWidth;
    private float mTextWidth;
    private int mLineColor;
    private int mTextColor;
    private int mBgColor;

    private float mTextSize;
    private ValueAnimator mAnimator;

    public WaveTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {

        mTextColor = ContextCompat.getColor(context, R.color.canvas_light_blue_color);
        mLineColor = ContextCompat.getColor(context, R.color.canvas_red_color);

        mBgColor = ContextCompat.getColor(context, R.color.canvas_pink_color);

        mTextWidth = UIUtils.dip2px(context, 1);
        mLineWidth = UIUtils.dip2px(context, 2);

        mTextSize = UIUtils.dip2px(context, 18);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(mTextWidth);

        mPath = new Path();

        mAnimator = ValueAnimator.ofFloat(0, (float) (2 * Math.PI));
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                m = progress;
                mA = (float) (1 - progress / (2 * Math.PI)) * A;
                invalidate();
            }
        });
        mAnimator.setDuration(1000);
    }

    public void setContent(String content) {
        this.mContent = content;

        this.mLength = mPaint.measureText(content) * LENGTH_TIMES;
        this.mStepLength = mLength / (POINT_COUNT);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(mBgColor);

        // 缁樺埗缃戞牸绾?        if (mIsShowLine) {
            drawCoordinate(canvas);
        }

        // 绌哄唴瀹瑰氨涓嶇粯鍒朵簡
        if (TextUtils.isEmpty(mContent)) {
            return;
        }

        // 鏋勫缓璺緞
        mPath.reset();
        for (int i = 0; i < POINT_COUNT; ++i) {

            float x = -mLength / LENGTH_TIMES + i * mStepLength;
            float y = calculateY(x);

            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }

        }

        // 绉昏嚦涓績
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 灏嗗瓧浣撴斁涓棿
        canvas.translate(mLength / LENGTH_TIMES / 2, 0);

        // 鏄惁鏄剧ずpath
        if (mIsShowLine) {
            mPaint.setColor(mLineColor);
            mPaint.setStrokeWidth(mLineWidth);
            mPaint.setStyle(Paint.Style.STROKE);

            canvas.drawPath(mPath, mPaint);

            mPaint.setColor(mTextColor);
            mPaint.setStrokeWidth(mTextWidth);
            mPaint.setStyle(Paint.Style.FILL);
        }

        // 鍦ㄨ矾寰勪笂鐢绘枃瀛?        canvas.drawTextOnPath(mContent, mPath, 0, 0, mPaint);

    }

    /**
     * 璁＄畻 y 杞村€?     * <p>
     * 涓夎鍑芥暟锛欰*sin(w*x+m)+k
     */
    private float calculateY(float x) {
        double a = Math.pow(4 / (4 + Math.pow(4 * x / mLength, 4)), 2.5f) * mA;
        return (float) (a * Math.sin(Math.PI * x / 200 - m));
    }

    /**
     * 鏄惁鏄剧ず鎻愮ず绾?     */
    public void setIsShowLine(boolean isShowLine) {
        this.mIsShowLine = isShowLine;
    }

    /**
     * 寮€濮嬪姩鐢?     */
    public void start() {
        if (mAnimator == null) {
            return;
        }

        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mAnimator.start();
    }

}

