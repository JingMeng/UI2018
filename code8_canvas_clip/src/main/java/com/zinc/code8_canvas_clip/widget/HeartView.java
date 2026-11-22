package com.zinc.code8_canvas_clip.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.code8_canvas_clip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/20 涓婂崍10:05
 * desc         :
 * version      :
 */
public class HeartView extends View {

    private static final String SHOW_CONTENT = "鐚涚寷鐨勫皬鐩嗗弸";

    private Paint mPaint;

    private Path mHeartPath;

    private RectF mHeartRect;

    private int mTextSize;

    private float mCurPos;

    private ObjectAnimator mUpAnim;

    private ValueAnimator mBezierAnim;

    private AnimatorSet mAnimSet;

    private int mTopBgColor;
    private int mBottomBgColor;

    // 褰撳墠鐨勫亸绉婚噺
    private int mCurOffset = 0;
    // 姣忔鐨勫亸绉婚噺
    private int mOffset;

    private Path mTopPath;
    private Path mBottomPath;

    private int mBezierHeight;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 鍒濆鍖栧彉閲?        mTextSize = dpToPx(16);
        mOffset = dpToPx(2.5f);
        mBezierHeight = dpToPx(10);

        mTopBgColor = ContextCompat.getColor(context, R.color.canvas_pink_color);
        mBottomBgColor = ContextCompat.getColor(context, R.color.canvas_light_blue_color);

        // 鍒濆鍖栧璞?        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHeartRect = new RectF();
        mTopPath = new Path();
        mBottomPath = new Path();

        // 鍒濆鍖栧績褰㈣矾寰?        mHeartPath = new Path();
        createHeart(mHeartPath);
        mCurPos = mHeartRect.bottom;

        // 璐濆灏旀洸绾垮姩鐢?        mBezierAnim = ValueAnimator.ofFloat(0, 1f);
        mBezierAnim.setDuration(4_000);
        mBezierAnim.setRepeatCount(ValueAnimator.INFINITE);
        mBezierAnim.setInterpolator(new LinearInterpolator());
        mBezierAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurOffset = (mCurOffset + mOffset) % (int) mWidth;
            }
        });

        // 鍒濆鍖?澧炲姞鐨勫姩鐢?        mUpAnim = ObjectAnimator.ofFloat(this, "process", 0, 1);
        mUpAnim.setInterpolator(new LinearInterpolator());
        mUpAnim.setDuration(6_000);
    }

    /**
     * 鏋勫缓蹇冨舰
     */
    private void createHeart(Path path) {
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(0, dpToPx(-38)));
        pointList.add(new PointF(dpToPx(50), dpToPx(-103)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(51), dpToPx(90)));
        pointList.add(new PointF(0, dpToPx(129)));
        pointList.add(new PointF(dpToPx(-51), dpToPx(90)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(37)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-12)));
        pointList.add(new PointF(dpToPx(-112), dpToPx(-61)));
        pointList.add(new PointF(dpToPx(-50), dpToPx(-103)));

        path.reset();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                path.moveTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            } else {
                path.lineTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            path.cubicTo(pointList.get(i * 3 + 1).x, pointList.get(i * 3 + 1).y,
                    pointList.get(i * 3 + 2).x, pointList.get(i * 3 + 2).y,
                    pointList.get(endPointIndex).x, pointList.get(endPointIndex).y);
        }
        path.close();

        path.computeBounds(mHeartRect, false);
    }

    public void start() {
        if (mAnimSet != null) {
            mAnimSet.cancel();
        }

        mAnimSet = new AnimatorSet();
        mAnimSet.play(mUpAnim)
                .with(mBezierAnim);
        mAnimSet.start();

    }

    /**
     * 褰撳墠杩涘害
     *
     * @param process 鑼冨洿 [0-1]
     */
    public void setProcess(float process) {
        // 濡傛灉蹇冨舰鐨剅ect涓虹┖锛岀洿鎺ヤ腑姝?        if (mHeartRect.isEmpty()) {
            if (mUpAnim != null) {
                mUpAnim.cancel();
            }

            return;
        }

        float curHeight = mHeartRect.height() * process;

        mCurPos = mHeartRect.bottom - curHeight;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int waveLength = width / 3;
        int totalLength = width * 2 + waveLength;

        // 鍒濆鍖?娴殑璺緞
        initPath(mTopPath,
                waveLength,
                mBezierHeight,
                totalLength,
                width,
                height,
                true);

        // 鍒濆鍖?娴殑璺緞
        initPath(mBottomPath, waveLength,
                mBezierHeight,
                totalLength,
                width,
                height,
                false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.translate(width / 2, height / 2);

        canvas.clipPath(mHeartPath);

        canvas.save();
        canvas.translate(-mCurOffset, mCurPos);

        canvas.clipPath(mTopPath);
        mPaint.setColor(mTopBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mTopPath, mPaint);

        canvas.translate(mCurOffset, -mCurPos);
        drawText(canvas, mBottomBgColor);
        canvas.restore();

        canvas.save();
        canvas.translate(-mCurOffset, mCurPos);
        canvas.clipPath(mBottomPath);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.canvas_light_blue_color));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mBottomPath, mPaint);

        canvas.translate(mCurOffset, -mCurPos);
        drawText(canvas, mTopBgColor);
        canvas.restore();

    }

    private float mWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    private void drawText(Canvas canvas, int textColor) {
        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(Typeface.SANS_SERIF);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float y = fontMetrics.bottom;

        canvas.drawText(SHOW_CONTENT, 0, y, mPaint);
    }

    /**
     * @param path         璺緞
     * @param length       鏇茬嚎鐨勫搴?     * @param height       璐濆灏旀洸绾跨殑楂樺害
     * @param totalLength  鎬婚暱搴?     * @param screenWidth  灞忓箷瀹?     * @param screenHeight 灞忓箷楂?     * @param isTop        鏄惁涓轰笂閮ㄤ唤
     */
    private void initPath(Path path,
                          int length,
                          int height,
                          int totalLength,
                          int screenWidth,
                          int screenHeight,
                          boolean isTop) {

        int left;
        int right;
        int top;
        int bottom;

        if (isTop) {
            left = -length - screenWidth / 2;
            right = screenWidth + screenWidth / 2;
            top = -screenHeight;
            bottom = 0;
            path.moveTo(left, bottom);
        } else {
            left = -length - screenWidth / 2;
            right = screenWidth + screenWidth / 2;
            top = 0;
            bottom = screenHeight;
            path.moveTo(left, top);
        }

        for (int i = -length; i < totalLength; i += length) {
            // rQuadTo 鍜?quadTo 鍖哄埆鍦ㄤ簬
            // rQuadTo 鏄浉瀵逛笂涓€涓偣 鑰?quadTo鏄浉瀵逛簬鐢诲竷
            path.rQuadTo(length / 4,
                    -height,
                    length / 2,
                    0);
            path.rQuadTo(length / 4,
                    height,
                    length / 2,
                    0);
        }

        if (isTop) {
            path.lineTo(right, top);
            path.lineTo(left, top);
        } else {
            path.lineTo(right, bottom);
            path.lineTo(left, bottom);
        }

        path.close();
    }

    /**
     * 杞崲 dp 鑷?px
     *
     * @param dpValue dp鍊?     * @return px鍊?     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }
}

