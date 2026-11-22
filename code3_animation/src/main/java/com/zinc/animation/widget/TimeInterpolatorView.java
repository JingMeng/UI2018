package com.zinc.animation.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/24
 * @description 鎻掑€煎櫒鐨勫潗鏍囨樉绀? */
public class TimeInterpolatorView extends View {

    // 澶栬竟璺?    private static final int PADDING = dpToPx(5f);
    // 瀛椾綋澶у皬
    private static final int TEXT_SIZE = dpToPx(8f);
    // 鐐圭殑鍗婂緞
    private static final int CUR_POINT_RADIUS = dpToPx(4.5f);

    // X銆乊 杞磋壊
    private static final int COORDINATION_LINE_COLOR = Color.BLACK;
    // 缃戞牸绾胯壊
    private static final int GRID_LINE_COLOR = Color.LTGRAY;
    // 鏁版嵁绾胯壊
    private static final int DATA_LINE_COLOR = Color.parseColor("#DB001B");
    // 褰撳墠鐐圭殑鑹?    private static final int CUR_POINT_COLOR = Color.parseColor("#DC143C");
    // 榛樿鐨勬渶浣庣偣
    private static final PointF DEFAULT_MIN_POINT = new PointF(0, 0);
    // 榛樿鐨勬渶楂樼偣
    private static final PointF DEFAULT_MAX_POINT = new PointF(0, 1);
    // 10涓棿闅?    private static final int GRID_INTERVAL_COUNT = 10;
    // 姣忎釜闂撮殧鐨勮法骞?    private static final float GRID_INTERVAL_LENGTH = 0.1f;

    // 閫熺巼鐨勬暟鎹?    private final List<PointF> mLineDataList = new ArrayList<>();

    // 鍧愭爣鐨勭敾绗?    private Paint mLinePaint;
    // 閫熺巼鐨勮建杩?    private Path mDataPath = new Path();
    // 瀛椾綋鐢荤瑪
    private Paint mTextPaint;
    // 鐐圭殑绗?    private Paint mPointPaint;

    // 鏁版嵁鐨勬渶浣庣偣
    private PointF mMinPoint = DEFAULT_MIN_POINT;
    // 鏁版嵁鐨勬渶楂樼偣
    private PointF mMaxPoint = DEFAULT_MAX_POINT;

    // 瑙嗗浘鐨勫
    private float mViewWidth;
    // 瑙嗗浘鐨勯珮
    private float mViewHeight;
    // 鍧愭爣鐨勫
    private float mWidth;

    // 鍧愭爣涓瘡涓笅鏍?鐨勫搴?    private float mEachItemWidth;

    private int mPositiveCount;
    private int mNegativeCount;

    // 褰撳墠鐨勭偣
    private PointF mCurPoint;

    public TimeInterpolatorView(Context context) {
        this(context, null, 0);
    }

    public TimeInterpolatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeInterpolatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TEXT_SIZE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

    }

    /**
     * 璁剧疆褰撳墠绉诲姩鐨勭偣
     *
     * @param curPoint
     */
    public void setCurPoint(PointF curPoint) {
        this.mCurPoint = curPoint;
        invalidate();
    }

    public void setLineData(List<PointF> lineDataList) {
        mLineDataList.clear();
        mLineDataList.addAll(lineDataList);

        mMinPoint = DEFAULT_MIN_POINT;
        mMaxPoint = DEFAULT_MAX_POINT;
        // 鏋勫缓 璺緞锛屽苟閫夊嚭鏈€楂樺拰鏈€浣庣殑point
        for (int i = 0; i < mLineDataList.size(); i++) {
            PointF curPoint = mLineDataList.get(i);

            // 閫夋渶浣庣偣
            if (curPoint.y < mMinPoint.y) {
                mMinPoint = curPoint;
            }

            // 閫夋渶楂樼偣
            if (curPoint.y > mMaxPoint.y) {
                mMaxPoint = curPoint;
            }

        }

        calculateEachItemWidth();

        invalidate();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        // 闇€瑕佸噺鍘?padding 鐨勫搴?鍜?瀛椾綋鐨勫ぇ灏?        mWidth = Math.min(w, h) - 2 * PADDING - TEXT_SIZE;

        calculateEachItemWidth();
    }

    /**
     * 璁＄畻姣忎釜鏍煎瓙鐨勫ぇ灏?     */
    private void calculateEachItemWidth() {

        // 鑾峰彇 y姝ｅ崐杞?鐨勫垎鍓蹭釜鏁?        mPositiveCount = (int) Math.abs(Math.ceil(mMaxPoint.y / GRID_INTERVAL_LENGTH));
        // 鑾峰彇 y璐熷崐杞?鐨勫垎鍓蹭釜鏁?        mNegativeCount = (int) Math.abs(Math.floor(mMinPoint.y / GRID_INTERVAL_LENGTH));

        // 璁＄畻闇€瑕佸垎鍓茬殑鏁伴噺锛屾渶灏戝崄涓?        int intervalCount = mPositiveCount + mNegativeCount;
        intervalCount = Math.max(intervalCount, GRID_INTERVAL_COUNT);

        mEachItemWidth = mWidth / intervalCount;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 鏋勫缓杞ㄨ抗
        buildDataPath();

        canvas.save();

        // 绉昏嚦鍘熺偣
        moveToTheOrigin(canvas);

        // 鐢诲潗鏍?        drawCoordination(canvas);
        // 鐢荤綉鏍?        drawGrid(canvas);

        // 鐢绘暟鎹嚎
        drawDataLine(canvas);

        // 鐢讳笅鏍?        drawText(canvas);

        // 鐢诲綋鍓嶇殑鐐?        drawPoint(canvas);

        canvas.restore();

    }

    /**
     * 鐢荤偣
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        if (mCurPoint == null) {
            return;
        }

        mPointPaint.setColor(CUR_POINT_COLOR);
        canvas.drawCircle(mCurPoint.x * mEachItemWidth * GRID_INTERVAL_COUNT,
                -mCurPoint.y * mEachItemWidth * GRID_INTERVAL_COUNT,
                CUR_POINT_RADIUS,
                mPointPaint);
    }

    /**
     * 鐢讳笅鏍?     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        canvas.drawText("0", -PADDING,
                0,
                mTextPaint);

        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 1; i <= mPositiveCount; ++i) {
            if (i <= 10) {
                mTextPaint.setColor(COORDINATION_LINE_COLOR);
            } else {
                mTextPaint.setColor(DATA_LINE_COLOR);
            }
            canvas.drawText(getNumString(i * 0.1f), -PADDING / 2,
                    -i * mEachItemWidth,
                    mTextPaint);
        }

        mTextPaint.setColor(DATA_LINE_COLOR);
        for (int i = 1; i <= mNegativeCount; ++i) {
            canvas.drawText(getNumString(i * -0.1f), -PADDING / 2,
                    i * mEachItemWidth,
                    mTextPaint);
        }

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(COORDINATION_LINE_COLOR);
        for (int i = 1; i <= GRID_INTERVAL_COUNT; ++i) {
            canvas.drawText(getNumString(i * 0.1f), i * mEachItemWidth,
                    PADDING / 2 + TEXT_SIZE,
                    mTextPaint);
        }
    }

    private String getNumString(float num) {
        return String.format("%.1f", num);
    }

    /**
     * 鐢荤綉鏍肩嚎
     *
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {
        mLinePaint.setStrokeWidth(dpToPx(0.5f));
        mLinePaint.setColor(GRID_LINE_COLOR);

        // 鐢粂姝ｈ酱妯嚎
        for (int i = 1; i <= mPositiveCount; ++i) {
            canvas.drawLine(0,
                    -i * mEachItemWidth,
                    GRID_INTERVAL_COUNT * mEachItemWidth,
                    -i * mEachItemWidth,
                    mLinePaint);
        }

        // 鐢粂璐熻酱妯嚎
        for (int i = 1; i <= mNegativeCount; ++i) {
            canvas.drawLine(0,
                    i * mEachItemWidth,
                    GRID_INTERVAL_COUNT * mEachItemWidth,
                    i * mEachItemWidth,
                    mLinePaint);
        }

        // 鐢粁姝ｈ酱绔栫嚎
        for (int i = 1; i <= GRID_INTERVAL_COUNT; ++i) {
            canvas.drawLine(i * mEachItemWidth,
                    -mPositiveCount * mEachItemWidth,
                    i * mEachItemWidth,
                    mNegativeCount * mEachItemWidth,
                    mLinePaint);
        }


    }

    /**
     * 鐢绘暟鎹嚎
     *
     * @param canvas
     */
    private void drawDataLine(Canvas canvas) {

        mLinePaint.setStrokeWidth(dpToPx(1f));
        mLinePaint.setColor(DATA_LINE_COLOR);

        canvas.drawPath(mDataPath, mLinePaint);

    }

    /**
     * 鐢?x銆亂 杞?     *
     * @param canvas
     */
    private void drawCoordination(Canvas canvas) {

        mLinePaint.setStrokeWidth(dpToPx(1f));
        mLinePaint.setColor(COORDINATION_LINE_COLOR);

        // 鐢?y 杞?        canvas.drawLine(0,
                -mPositiveCount * mEachItemWidth,
                0,
                mNegativeCount * mEachItemWidth,
                mLinePaint);

        // 鐢?x 杞?        canvas.drawLine(0,
                0,
                GRID_INTERVAL_COUNT * mEachItemWidth,
                0,
                mLinePaint);

    }

    /**
     * 鏋勫缓鏁版嵁璺緞
     */
    private void buildDataPath() {
        mDataPath.reset();
        float width = mEachItemWidth * GRID_INTERVAL_COUNT;
        // 鏋勫缓 璺緞锛屽苟閫夊嚭鏈€楂樺拰鏈€浣庣殑point
        for (int i = 0; i < mLineDataList.size(); i++) {
            PointF curPoint = mLineDataList.get(i);
            if (i == 0) {
                mDataPath.moveTo(curPoint.x * width, -curPoint.y * width);
            } else {
                mDataPath.lineTo(curPoint.x * width, -curPoint.y * width);
            }
        }
    }

    /**
     * 灏嗙敾甯冪Щ鑷?鍘熺偣
     */
    private void moveToTheOrigin(Canvas canvas) {

        float verHeight = mEachItemWidth * mPositiveCount;

        // 璁＄畻 妯悜绉诲姩璺濈
        float horPadding = mViewWidth - mEachItemWidth * GRID_INTERVAL_COUNT - 2 * PADDING;
        float verPadding = mViewHeight - mEachItemWidth * (mPositiveCount + mNegativeCount) - 2 * PADDING - TEXT_SIZE / 2;

        canvas.translate(horPadding / 2 + PADDING, verPadding / 2 + verHeight + PADDING);

    }

    /**
     * 杞崲 dp 鑷?px
     *
     * @param dpValue dp鍊?     * @return px鍊?     */
    protected static int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

    /**
     * 鑾峰彇瑙嗗浘鐨勫
     *
     * @return 瑙嗗浘瀹?- 宸﹀彸鐨勫唴杈硅窛
     */
    private float getViewEnableWidth() {
        return mViewWidth - PADDING * 2;
    }

    /**
     * 鑾峰彇瑙嗗浘鐨勯珮
     *
     * @return 瑙嗗浘楂?- 涓婁笅鐨勫唴杈硅窛
     */
    private float getViewEnableHeight() {
        return mViewHeight - PADDING * 2;
    }

}

