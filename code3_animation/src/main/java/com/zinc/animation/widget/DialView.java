package com.zinc.animation.widget;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/19
 * @description 琛ㄧ洏瑙嗗浘
 */
public class DialView extends View {

    // 琛ㄧ洏鐨勯鑹?    private static final String DEFAULT_DIAL_LINE_COLOR = "#3A5DFE";
    // 琛ㄧ洏鐨勬寚閽堥鑹?    private static final String DEFAULT_POINTER_COLOR = "#FE3171";
    // 榛樿鐢荤瑪瀹藉害
    private static final float LINE_WIDTH = dpToPx(5f);
    // 绾跨殑闂撮殧锛屾瘡鏍圭嚎鐩搁殧 10 搴?    private static final float LINE_INTERVAL = 10;
    // 绾跨殑鏉℃暟
    private static final int DEFAULT_LINE_COUNT = 5;
    // 鍔ㄧ敾鏃堕暱
    private static final int DURATION = 2500;

    // 琛ㄧ洏棰滆壊
    private int mDialLineColor;
    // 鎸囬拡棰滆壊
    private int mPointerColor;

    // 瀹?    private float mWidth;

    // 绾跨殑涓暟
    private int mLineCount;

    // 姣忎釜椤圭殑瑙掑害
    private float mEachAngle;

    // 瀹藉害鐨勬瘮渚?    private float mPointerCircleWidthRatio = 6;
    // 鎸囬拡闀垮害姣斾緥
    private float mPointerLengthRatio = 4;

    private float mPointerCircleRadius;
    private float mPointerLength;

    private Path mLinePath;
    private Path mPointerPath;

    private Paint mLinePaint;
    private Paint mPointerPaint;

    private RectF mRectF;

    private ObjectAnimator mAnimator;

    // 褰撳墠鐨勯」
    private int mValue = 4;

    // 褰撳墠鏃嬭浆瑙掑害
    private float mRotateAngle;

    public DialView(Context context) {
        this(context, null, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {

        setDialLineColor(Color.parseColor(DEFAULT_DIAL_LINE_COLOR));
        setPointerColor(Color.parseColor(DEFAULT_POINTER_COLOR));

        mLinePath = new Path();
        mPointerPath = new Path();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(LINE_WIDTH);

        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setStyle(Paint.Style.FILL);

        mRectF = new RectF();
        setLineCount(DEFAULT_LINE_COUNT);

//        mRotateAngle = 180 + mEachAngle / 2 + LINE_INTERVAL;

    }

    /**
     * 璁剧疆 绾跨殑棰滆壊
     *
     * @param dialLineColor 绾跨殑棰滆壊
     */
    public void setDialLineColor(int dialLineColor) {
        this.mDialLineColor = dialLineColor;
    }

    /**
     * 璁剧疆 鎸囬拡鐨勯鑹?     *
     * @param pointerColor 鎸囬拡鐨勯鑹?     */
    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
    }

    /**
     * 璁剧疆绾挎潯鐨勬暟閲?     *
     * @param lineCount 绾挎暟閲?     */
    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount + 1;
        mEachAngle = 360 / mLineCount - LINE_INTERVAL;
        mRotateAngle = 180 + mEachAngle / 2 + LINE_INTERVAL;
        createLinePath();
    }

    public void start() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator = ObjectAnimator.ofFloat(this,
                "curPointAngle",
                180 + mEachAngle / 2 + LINE_INTERVAL,
                mValue * (mEachAngle + LINE_INTERVAL) + 180);
        mAnimator.setDuration(DURATION);
        mAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float x) {
                float factor = 0.45f;
                return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1);
            }
        });
        mAnimator.start();
    }

    /**
     * 閲嶇疆
     */
    public void reset() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mRotateAngle = 180 + mEachAngle / 2 + LINE_INTERVAL;
        invalidate();

    }

    public void setValue(int value) {
        this.mValue = value;
    }

    private void setCurPointAngle(float angle) {
        this.mRotateAngle = angle;
        Log.i("dial", "setCurPointAngle: " + mRotateAngle);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = Math.min(w, h);

        mPointerCircleRadius = mWidth / mPointerCircleWidthRatio / 2;
        mPointerLength = mWidth / mPointerLengthRatio;

        mRectF.top = LINE_WIDTH / 2;
        mRectF.right = mWidth - LINE_WIDTH / 2;
        mRectF.bottom = mWidth - LINE_WIDTH / 2;
        mRectF.left = LINE_WIDTH / 2;

        createLinePath();
        createPointerPath();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawDialLine(canvas);

        drawPointer(canvas);

    }

    /**
     * 鐢绘寚閽?     *
     * @param canvas 鐢诲竷
     */
    private void drawPointer(Canvas canvas) {
        canvas.save();

        canvas.translate(mWidth / 2, mWidth / 2);

        canvas.rotate(mRotateAngle);

        mPointerPaint.setColor(mPointerColor);
        canvas.drawPath(mPointerPath, mPointerPaint);

        canvas.restore();
    }

    /**
     * 鐢昏〃鐩樼嚎
     *
     * @param canvas 鐢诲竷
     */
    private void drawDialLine(Canvas canvas) {
        mLinePaint.setColor(mDialLineColor);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    /**
     * 鏋勫缓 鎸囨爣 鐨勮矾寰?     */
    private void createLinePath() {

        mLinePath.reset();
        for (int i = 0; i < mLineCount - 1; ++i) {
            mLinePath.addArc(mRectF,
                    getRealOffsetAngle() + i * (mEachAngle + LINE_INTERVAL),
                    mEachAngle);
        }

    }

    private void createPointerPath() {
        RectF rectF = new RectF(
                -mPointerCircleRadius,
                -mPointerCircleRadius,
                mPointerCircleRadius,
                mPointerCircleRadius
        );

        mPointerPath.moveTo(mPointerCircleRadius, 0);

        mPointerPath.addArc(rectF, 0, 180);

        mPointerPath.lineTo(0, -mPointerLength);
        mPointerPath.lineTo(mPointerCircleRadius, 0
        );
        mPointerPath.close();
    }

    private float getRealOffsetAngle() {
        return 90 + mEachAngle / 2 + LINE_INTERVAL;
    }

    /**
     * 杞崲 sp 鑷?px
     *
     * @param spValue sp鍊?     * @return px鍊?     */
    protected static int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 杞崲 dp 鑷?px
     *
     * @param dpValue dp鍊?     * @return px鍊?     */
    protected static int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

}

