package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/9
 * @description
 */
public class DIYBezierView extends BaseView {

    private static final String BEZIER_CIRCLE_COLOR = "#20A298";    //缁胯壊
    private static final String NATIVE_CIRCLE_COLOR = "#F6A010";    //姗欒壊
    private static final String CONTROL_LINE_COLOR = "#FA3096";     //鑹崇孩鑹?    private static final String SEL_POINT_COLOR = "#1296db";        //钃濊壊

    // 鍦嗙殑涓績鐐?    private PointF mCenterPoint;
    // 鍦嗗崐寰?    private float mRadius;

    // 鎺у埗鐐瑰垪琛紝椤哄簭涓猴細鍙充笂銆佸彸涓嬨€佸乏涓嬨€佸乏涓?    private List<PointF> mControlPointList;

    // 閫変腑鐨勭偣闆嗗悎锛屽彈 status 褰卞搷
    private final List<PointF> mCurSelectPointList = new ArrayList<>();
    // 閫変腑鐨勭偣
    private PointF mSelPoint;

    // 鎺у埗鐐瑰崰鍗婂緞鐨勬瘮渚?    private float mRatio;

    private Path mPath;

    private Path mControlPath;

    private Paint mPaint;
    private Paint mCirclePaint;
    private Paint mControlPaint;

    // 鏈夋晥瑙︾鐨勮寖鍥?    private int mTouchRegionWidth;

    // 绾跨殑瀹藉害
    private int LINE_WIDTH;
    // 鎺у埗鐐圭殑鍗婂緞
    private int POINT_RADIO_WIDTH;
    // 閫変腑鎺у埗鐐圭殑鍗婂緞
    private int SEL_POINT_RADIO_WIDTH;

    // 鎷藉姩鐘舵€?    private Status mStatus;

    // 鏄惁鏄剧ず杈呭姪绾?    private boolean mIsShowHelpLine;

    // 瑙︾鐨剎杞?    private float mLastX = -1;
    // 瑙︾鐨剏杞?    private float mLastY = -1;

    public DIYBezierView(Context context) {
        super(context);
    }

    public DIYBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DIYBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    /**
     * 鑾峰彇鎺у埗鐐?     *
     * @return
     */
    public List<PointF> getControlPointList() {
        return mControlPointList;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
    }

    public void setIsShowHelpLine(boolean isShowHelpLine) {
        this.mIsShowHelpLine = isShowHelpLine;
        invalidate();
    }

    @Override
    protected void init(Context context) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        mRadius = width / 4;

        LINE_WIDTH = dpToPx(2);
        POINT_RADIO_WIDTH = dpToPx(4);
        SEL_POINT_RADIO_WIDTH = dpToPx(6);
        mTouchRegionWidth = dpToPx(20);

        mCenterPoint = new PointF(0, 0);

        mControlPointList = new ArrayList<>();

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(BEZIER_CIRCLE_COLOR));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(LINE_WIDTH);
        mCirclePaint.setColor(Color.parseColor(NATIVE_CIRCLE_COLOR));

        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setStyle(Paint.Style.STROKE);
        mControlPaint.setStrokeWidth(LINE_WIDTH);
        mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));

        mControlPath = new Path();

        mStatus = Status.FREE;

        mIsShowHelpLine = true;

        mRatio = 0.55f;

        calculateControlPoint();
    }

    public void reset() {
        calculateControlPoint();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        mPath.reset();

        // 鐢诲渾
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

        // 涓嶉渶瑕佽緟鍔╃嚎锛屽垯鐢诲畬璐濆灏旀洸绾垮氨缁堟
        if(!mIsShowHelpLine){
            return;
        }

        // 缁樺埗鍦?        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclePaint);

        // 鎺у埗鍩虹嚎
        mControlPath.reset();
        for (int i = 0; i < 4; i++) {
            int startIndex = i * 3;
            if (i == 0) {
                mControlPath.moveTo(mControlPointList.get(mControlPointList.size() - 1).x,
                        mControlPointList.get(mControlPointList.size() - 1).y);
            } else {
                mControlPath.moveTo(mControlPointList.get(startIndex - 1).x, mControlPointList.get(startIndex - 1).y);
            }

            mControlPath.lineTo(mControlPointList.get(startIndex).x, mControlPointList.get(startIndex).y);
            mControlPath.lineTo(mControlPointList.get(startIndex + 1).x, mControlPointList.get(startIndex + 1).y);
        }
        mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));
        mControlPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mControlPath, mControlPaint);

        // 鎺у埗鐐?        mControlPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mControlPointList.size(); ++i) {
            PointF point = mControlPointList.get(i);
            float radio;
            if (mCurSelectPointList.contains(point)) {      // 缁樺埗閫変腑鐨勭偣
                mControlPaint.setColor(Color.parseColor(SEL_POINT_COLOR));
                radio = SEL_POINT_RADIO_WIDTH;
            } else {        // 缁樺埗涓洪€変腑鐨勭偣
                mControlPaint.setColor(Color.parseColor(CONTROL_LINE_COLOR));
                radio = POINT_RADIO_WIDTH;
            }
            canvas.drawCircle(point.x, point.y, radio, mControlPaint);
        }

        // 濡傛灉涓轰笁鐐规嫿鍔紝灏嗕笁鐐硅繛鎺?        if (mStatus == Status.THREE) {
            if (mCurSelectPointList.size() == 1) {
                return;
            }
            for (int i = 0; i < mCurSelectPointList.size() - 1; ++i) {
                PointF p1 = mCurSelectPointList.get(i);
                PointF p2 = mCurSelectPointList.get(i + 1);
                mControlPaint.setColor(Color.parseColor(SEL_POINT_COLOR));
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mControlPaint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 瑙︾鐨勫潗鏍?        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (selectControlPoint(x, y)) {
                    mLastX = x;
                    mLastY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastX == -1 || mLastY == -1) {
                    return true;
                }

                // 璁＄畻鍋忕Щ鍊?                float offsetX = x - mLastX;
                float offsetY = y - mLastY;

                if ((mStatus == Status.MIRROR_DIFF || mStatus == Status.MIRROR_SAME) && mSelPoint != null) {

                    mSelPoint.x = mSelPoint.x + offsetX;
                    mSelPoint.y = mSelPoint.y + offsetY;

                    PointF otherPoint = null;
                    for (PointF point : mCurSelectPointList) {
                        if (point != mSelPoint) {
                            otherPoint = point;
                            break;
                        }
                    }

                    if (mStatus == Status.MIRROR_DIFF) {
                        offsetX = -offsetX;
                        offsetY = -offsetY;
                    }

                    if (otherPoint != null) {
                        otherPoint.x = otherPoint.x + offsetX;
                        otherPoint.y = otherPoint.y + offsetY;
                    }

                } else {
                    // 鏇存柊閫変腑
                    for (PointF point : mCurSelectPointList) {
                        point.x = point.x + offsetX;
                        point.y = point.y + offsetY;
                    }
                }


                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                mCurSelectPointList.clear();
                mSelPoint = null;
                mLastX = -1;
                mLastY = -1;
                break;
        }

        invalidate();

        return true;
    }

    /**
     * 鏄惁鍦ㄦ湁鏁堢殑瑙︾鑼冨洿
     *
     * @param x
     * @param y
     * @return true 鏈夐€変腑锛沠alse 鏃犻€変腑
     */
    private boolean selectControlPoint(float x, float y) {

        // 閫変腑鐨勪笅鏍?        int selIndex = -1;

        for (int i = 0; i < mControlPointList.size(); ++i) {

            PointF controlPoint = mControlPointList.get(i);

            float resultX = controlPoint.x + mWidth / 2;
            float resultY = controlPoint.y + mHeight / 2;

            RectF pointRange = new RectF(resultX - mTouchRegionWidth,
                    resultY - mTouchRegionWidth,
                    resultX + mTouchRegionWidth,
                    resultY + mTouchRegionWidth);

            if (pointRange.contains(x, y)) {
                selIndex = i;
                break;
            }
        }

        // 濡傛灉娌℃湁閫変腑鐨勫氨杩斿洖
        if (selIndex == -1) {
            return false;
        }

        // 娓呯┖涔嬪墠鐨勯€変腑鐐?        mCurSelectPointList.clear();

        mSelPoint = mControlPointList.get(selIndex);

        switch (mStatus) {
            case FREE:  // 浠绘剰鐐规嫿鍔?                mCurSelectPointList.add(mControlPointList.get(selIndex));
                break;
            case THREE: // 涓夌偣鎷藉姩锛岄渶瑕佸悓鏃堕€変腑涓変釜

                // 杩涜鏁翠綋鐨勫亸绉讳笅鏍囷紝渚夸簬璁＄畻
                int offsetSelIndex = (selIndex + 1) % 12;
                int offsetRangeIndex = offsetSelIndex / 3;

                if (offsetRangeIndex == 0) {
                    mCurSelectPointList.add(mControlPointList.get(11));
                } else {
                    mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3 - 1));
                }

                mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3));
                mCurSelectPointList.add(mControlPointList.get(offsetRangeIndex * 3 + 1));

                break;
            // 闀滃儚锛岄渶瑕佸悓鏃堕€変腑涓や釜
            case MIRROR_DIFF:
            case MIRROR_SAME:
                if (selIndex == 0 || selIndex == 6) {
                    mCurSelectPointList.add(mControlPointList.get(0));
                    mCurSelectPointList.add(mControlPointList.get(6));
                } else {
                    mCurSelectPointList.add(mControlPointList.get(selIndex));
                    mCurSelectPointList.add(mControlPointList.get(12 - selIndex));
                }

                break;
        }

        return true;

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

    public enum Status {
        FREE,          // 鑷敱鎷藉姩
        THREE,         // 涓夌偣鎷藉姩
        MIRROR_DIFF,   // 闀滃儚寮傚悜
        MIRROR_SAME,   // 闀滃儚鍚屽悜
    }

}

