package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/9
 * @description 鐢ㄨ礉濉炲皵鏇茬嚎缁樺埗鍦嗙殑杩囩▼
 */
public class CircleBezierView extends BaseView {

    // 鍦嗙殑涓績鐐?    private PointF mCenterPoint;
    // 鍦嗗崐寰?    private float mRadius;

    // 鎺у埗鐐瑰垪琛紝椤哄簭涓猴細鍙充笂銆佸彸涓嬨€佸乏涓嬨€佸乏涓?    private List<PointF> mControlPointList;

    // 鎺у埗鐐瑰崰鍗婂緞鐨勬瘮渚?    private float mRatio;

    // 鍦嗙殑璺緞
    private Path mPath;

    // 缁樺埗璐濆灏旀洸绾跨殑鐢荤瑪
    private Paint mPaint;
    // 缁樺埗鍦嗙殑鐢荤瑪
    private Paint mCirclePaint;
    // 缁樺埗鎺у埗绾跨殑鐢荤瑪
    private Paint mLinePaint;

    // 鎺у埗绾跨殑棰滆壊
    private int[] mLineColor;

    // 绾跨殑瀹藉害
    private int LINE_WIDTH;

    public CircleBezierView(Context context) {
        super(context);
    }

    public CircleBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 璁剧疆姣斾緥
     *
     * @param ratio 姣斾緥锛?-1
     */
    public void setRatio(float ratio) {
        this.mRatio = ratio;
        calculateControlPoint();
        invalidate();
    }

    @Override
    protected void init(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mRadius = width / 3;

        LINE_WIDTH = dpToPx(2);

        mCenterPoint = new PointF(0, 0);

        mControlPointList = new ArrayList<>();

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#1296db"));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(LINE_WIDTH);
        mCirclePaint.setColor(Color.RED);

        mRatio = 0.55f;

        mLineColor = new int[4];
        mLineColor[0] = Color.parseColor("#f4ea2a");    //榛勮壊
        mLineColor[1] = Color.parseColor("#1afa29");    //缁胯壊
        mLineColor[2] = Color.parseColor("#efb336");    //姗欒壊
        mLineColor[3] = Color.parseColor("#e89abe");    //绮夎壊

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(dpToPx(2));

        calculateControlPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                mPath.moveTo(mControlPointList.get(i * 3).x, mControlPointList.get(i * 3).y);
            } else {
                mPath.lineTo(mControlPointList.get(i * 3).x, mControlPointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            mPath.cubicTo(mControlPointList.get(i * 3 + 1).x, mControlPointList.get(i * 3 + 1).y,
                    mControlPointList.get(i * 3 + 2).x, mControlPointList.get(i * 3 + 2).y,
                    mControlPointList.get(endPointIndex).x, mControlPointList.get(endPointIndex).y);
        }

        // 缁樺埗璐濆灏旀洸绾?        canvas.drawPath(mPath, mPaint);

        // 缁樺埗鍦?        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

        // 缁樺埗鎺у埗绾?        for (int i = 0; i < mControlPointList.size(); ++i) {
            // 璁剧疆棰滆壊
            mLinePaint.setColor(mLineColor[i / 3]);

            int endPointIndex = (i == mControlPointList.size() - 1) ? 0 : i + 1;

            canvas.drawLine(mControlPointList.get(i).x,
                    mControlPointList.get(i).y,
                    mControlPointList.get(endPointIndex).x,
                    mControlPointList.get(endPointIndex).y,
                    mLinePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 璁＄畻鍦嗙殑鎺у埗鐐?     */
    private void calculateControlPoint() {
        // 璁＄畻 涓棿鎺у埗鐐瑰埌绔偣鐨勮窛绂?        float controlWidth = mRatio * mRadius;

        mControlPointList.clear();

        // 鍙充笂
        mControlPointList.add(new PointF(0, -mRadius));
        mControlPointList.add(new PointF(controlWidth, -mRadius));
        mControlPointList.add(new PointF(mRadius, -controlWidth));

        // 鍙充笅
        mControlPointList.add(new PointF(mRadius, 0));
        mControlPointList.add(new PointF(mRadius, controlWidth));
        mControlPointList.add(new PointF(controlWidth, mRadius));

        // 宸︿笅
        mControlPointList.add(new PointF(0, mRadius));
        mControlPointList.add(new PointF(-controlWidth, mRadius));
        mControlPointList.add(new PointF(-mRadius, controlWidth));
        // 宸︿笂
        mControlPointList.add(new PointF(-mRadius, 0));
        mControlPointList.add(new PointF(-mRadius, -controlWidth));
        mControlPointList.add(new PointF(-controlWidth, -mRadius));

    }

}

