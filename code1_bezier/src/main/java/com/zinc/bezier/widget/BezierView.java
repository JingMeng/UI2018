package com.zinc.bezier.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zinc.bezier.utils.BezierUtils;
import com.zinc.bezier.activity.BezierActivity;
import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/30
 * @description
 */
public class BezierView extends BaseView {

    // 甯ф暟锛?000锛屽嵆1000涓偣鏉ョ粯鍒朵竴鏉＄嚎
    private static final int FRAME = 1000;

    // handler 浜嬩欢
    private static final int HANDLE_EVENT = 12580;

    // 鍑嗗鐘舵€?    public static final int PREPARE = 0x0001;
    // 杩愯鐘舵€?    public static final int RUNNING = 0x0002;
    // 鍋滄鐘舵€?    public static final int STOP = 0x0004;

    // 榛樿鐨勬帶鍒剁偣
    private List<PointF> DEFAULT_POINT;

    // 鏈夋晥瑙︾鐨勮寖鍥?    private int mTouchRegionWidth;

    // 褰撳墠鐘舵€?    private int mState = PREPARE;
    // 鏅€氱嚎鐨勫搴?    private int LINE_WIDTH;
    // 璐濆灏旀洸绾跨殑瀹藉害
    private int BEZIER_LINE_WIDTH;
    // 鎺у埗鐐圭殑鍗婂緞
    private int POINT_RADIO_WIDTH;

    // 閫熺巼锛屾瘡娆＄粯鍒惰烦杩囩殑甯ф暟锛岀瓑浜?0锛屽嵆琛ㄧず姣忔缁樺埗璺宠繃10甯?    private int mRate = 10;

    // 缁樺埗璐濆灏旀洸绾跨殑鐢荤瑪
    private Paint mBezierPaint;
    // 璐濆灏旀洸绾跨殑璺緞
    private Path mBezierPath;
    // 鎺у埗鐐圭殑鐢荤瑪
    private Paint mControlPaint;
    // 缁樺埗绔偣鐨勭敾绗?    private Paint mPointPaint;
    // 涓棿闃跺眰鐨勭嚎鐢荤瑪
    private Paint mIntermediatePaint;
    // 缁樺瓧绗?    private Paint mTextPaint;

    // 褰撳墠鐨勬瘮渚?    private float mCurRatio;

    // 鎺у埗鐐圭殑鍧愭爣
    private List<PointF> mControlPointList;
    // 璐濆灏旀洸绾跨殑璺緞鐐?    private List<PointF> mBezierPointList;
    // 鑹插€硷紝姣忎竴闃剁殑鑹插€?    private List<String> mLineColor;

    private Handler mHandler;

    // 鏈€楂橀樁鐨勬帶鍒剁偣涓暟
    private int mPointCount;
    // 鏄惁缁樺埗闄嶉樁绾?    private boolean mIsShowReduceOrderLine;
    // 鏄惁寰幆鎾斁
    private boolean mIsLoop;

    /**
     * 灞傜骇璇存槑锛?     * 绗?灞俵ist.瀛樻斁姣忎竴闃剁殑鍊?     * 鍗筹細mIntermediateList.get(0) 鍗充负绗?n-1)闃剁殑璐濆灏旀洸绾跨殑鏁板€?     * mIntermediateList.get(1) 鍗充负绗?n-2)闃剁殑璐濆灏旀洸绾跨殑鏁板€?     * 绗?灞俵ist.瀛樻斁璇ラ樁鐨勬瘡鏉¤竟鐨勬暟鎹?     * 绗?灞俵ist.瀛樻斁杩欐潯杈圭偣鐨勬暟鎹?     */
    private final List<List<List<PointF>>> mIntermediateList = new ArrayList<>();

    /**
     * 灞傜骇璇存槑锛?     * 绗?灞傦細杈圭殑鏁版嵁
     * 绗?灞傦細杈逛腑鐨勭偣鏁版嵁
     */
    private final List<List<PointF>> mIntermediateDrawList = new ArrayList<>();

    // 缁樺埗鏃讹紝璐濆灏旀洸绾跨殑鐐?    private PointF mCurBezierPoint;
    // 褰撳墠閫変腑鐨勭偣
    private PointF mCurSelectPoint;

    public BezierView(Context context) {
        super(context);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 璁剧疆閫熺巼
     *
     * @param rate 閫熺巼
     */
    public void setRate(int rate) {
        this.mRate = rate;
    }

    /**
     * 璁剧疆闃?[2-7]
     *
     * @param order
     */
    public void setOrder(int order) {
        this.mPointCount = order + 1;
        mControlPointList.clear();
        for (int i = 0; i < mPointCount; ++i) {
            if (i >= DEFAULT_POINT.size()) {
                break;
            }
            mControlPointList.add(DEFAULT_POINT.get(i));
        }
    }

    /**
     * 璁剧疆鏄惁鏄剧ず闄嶉樁绾?     *
     * @param isShowReduceOrderLine
     */
    public void setIsShowReduceOrderLine(boolean isShowReduceOrderLine) {
        this.mIsShowReduceOrderLine = isShowReduceOrderLine;
    }

    public boolean isShowReduceOrderLine() {
        return mIsShowReduceOrderLine;
    }

    /**
     * 璁剧疆鏄惁寰幆鎾斁
     *
     * @param isLoop
     */
    public void setIsLoop(boolean isLoop) {
        this.mIsLoop = isLoop;
    }

    public boolean isLoop() {
        return mIsLoop;
    }

    public int getState() {
        return mState;
    }

    public void setCurRatio(float curRatio) {
        this.mCurRatio = curRatio;
    }

    @Override
    protected void init(Context context) {
        LINE_WIDTH = dpToPx(2);
        BEZIER_LINE_WIDTH = dpToPx(3);
        POINT_RADIO_WIDTH = dpToPx(4);

        // 鍒濆鍖栦负鍑嗗鐘舵€?        mState = PREPARE;

        mHandler = new MyHandler(this);

        // 鍒濆鍖栭鑹?        mLineColor = new ArrayList<>();
        mLineColor.add("#f4ea2a");    //榛勮壊
        mLineColor.add("#1afa29");    //缁胯壊
        mLineColor.add("#13227a");    //钃濊壊
        mLineColor.add("#515151");    //榛戣壊
        mLineColor.add("#e89abe");    //绮夎壊
        mLineColor.add("#efb336");    //姗欒壊

        int width = context.getResources().getDisplayMetrics().widthPixels;
        DEFAULT_POINT = new ArrayList<>();
        DEFAULT_POINT.add(new PointF(width / 5, width / 5));
        DEFAULT_POINT.add(new PointF(width / 3, width / 2));
        DEFAULT_POINT.add(new PointF(width / 3 * 2, width / 4));
        DEFAULT_POINT.add(new PointF(width / 2, width / 3));
        DEFAULT_POINT.add(new PointF(width / 4 * 2, width / 8));
        DEFAULT_POINT.add(new PointF(width / 5 * 4, width / 12));
        DEFAULT_POINT.add(new PointF(width / 5 * 4, width));
        DEFAULT_POINT.add(new PointF(width / 2, width));

        // 鍒濆鍖?鎺у埗鐐?        mControlPointList = new ArrayList<>();
        mPointCount = 8;
        for (int i = 0; i < mPointCount; ++i) {
            if (i >= DEFAULT_POINT.size()) {
                break;
            }
            mControlPointList.add(DEFAULT_POINT.get(i));
        }

        // 鍒濆鍖栬礉濉炲皵鐨勮矾寰勭殑鐢荤瑪
        mBezierPaint = new Paint();
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setColor(getBezierLineColor());
        mBezierPaint.setStrokeWidth(BEZIER_LINE_WIDTH);
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);

        // 鍒濆 鎺у埗鐐圭敾绗?        mControlPaint = new Paint();
        mControlPaint.setAntiAlias(true);
        mControlPaint.setColor(getControlLineColor());
        mControlPaint.setStrokeWidth(LINE_WIDTH);

        // 鍒濆鍖?绔偣鐢荤瑪
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(spToPx(15));

        // 鍒濆鍖栦腑闂撮樁绾х殑鐢荤瑪
        mIntermediatePaint = new Paint();
        mIntermediatePaint.setAntiAlias(true);
        mIntermediatePaint.setStrokeWidth(LINE_WIDTH);

        // 鍒濆鍖栧瓨鏀捐礉濉炲皵鏇茬嚎鏈€缁堢粨鏋滅殑璺緞
        mBezierPath = new Path();

        // 瑙︾鑼冨洿
        mTouchRegionWidth = dpToPx(20);

    }


    public void start() {

        // 閲嶇疆 璐濆灏旀洸绾跨粨鏋?鐨勮矾寰?        mBezierPath.reset();

        // 鐘舵€佽嚦涓鸿繍琛屼腑
        mState = RUNNING;

        // 璁＄畻 璐濆灏旀洸绾跨粨鏋?鐨勬瘡涓偣
        mBezierPointList = BezierUtils.buildBezierPoint(mControlPointList, FRAME);
        // 灏嗚绠楀ソ鐨?璐濆灏旀洸绾跨殑鐐?缁勮鎴愯矾寰?        prepareBezierPath();

        if (mIsShowReduceOrderLine) {
            // 璁＄畻 涓棿闃剁骇鐨勬帶鍒剁偣
            BezierUtils.calculateIntermediateLine(mIntermediateList, mControlPointList, FRAME);
        }

        mCurRatio = 0;

        setCurBezierPoint(mBezierPointList.get(0));

        invalidate();

    }

    /**
     * 鏆傚仠 鎴?缁х画
     */
    public void pause() {
        if (mState == RUNNING) {
            mState = STOP;
        } else if (mState == STOP) {
            mState = RUNNING;
            mHandler.sendEmptyMessage(HANDLE_EVENT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 鐢诲潗鏍囧拰缃戞牸
        drawCoordinate(canvas);

        // 缁樺埗鎺у埗鍩虹嚎鍜岀偣
        drawControlLine(canvas);

        // 缁樺埗璐濆灏旀洸绾?        canvas.drawPath(mBezierPath, mBezierPaint);

        if (mState != PREPARE) {
            mPointPaint.setStyle(Paint.Style.FILL);
            if (mIsShowReduceOrderLine) {
                // 鐢讳腑闂撮樁灞傜殑绾?                for (int i = 0; i < mIntermediateDrawList.size(); ++i) {

                    List<PointF> lineList = mIntermediateDrawList.get(i);
                    mIntermediatePaint.setColor(getColor(i));
                    mPointPaint.setColor(getColor(i));

                    for (int j = 0; j < lineList.size() - 1; ++j) {

                        // 鐢荤嚎
                        canvas.drawLine(lineList.get(j).x,
                                lineList.get(j).y,
                                lineList.get(j + 1).x,
                                lineList.get(j + 1).y,
                                mIntermediatePaint);

                        // 鐢荤偣
                        canvas.drawCircle(lineList.get(j).x,
                                lineList.get(j).y,
                                POINT_RADIO_WIDTH,
                                mPointPaint);
                    }

                    canvas.drawCircle(lineList.get(lineList.size() - 1).x,
                            lineList.get(lineList.size() - 1).y,
                            POINT_RADIO_WIDTH,
                            mPointPaint);
                }
            }

            mPointPaint.setColor(getBezierLineColor());
            canvas.drawCircle(mCurBezierPoint.x,
                    mCurBezierPoint.y,
                    POINT_RADIO_WIDTH,
                    mPointPaint);

            mHandler.sendEmptyMessage(HANDLE_EVENT);

        }

        if (mCurRatio == 1 && !mIsLoop && getContext() instanceof BezierActivity) {
            ((BezierActivity) getContext()).resetPlayBtn();
        }

        canvas.drawText("u = " + mCurRatio, mWidth / 4, mHeight * 11 / 12, mTextPaint);

    }

    /**
     * 缁樺埗 鎺у埗鍩虹嚎 鍜?鐐?     */
    private void drawControlLine(Canvas canvas) {
        mPointPaint.setColor(getControlLineColor());

        // 缁樺埗 鎺у埗鐐?        for (PointF point : mControlPointList) {
            mPointPaint.setStyle(Paint.Style.FILL);
            mPointPaint.setStrokeWidth(0);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH, mPointPaint);

            mPointPaint.setStyle(Paint.Style.STROKE);
            mPointPaint.setStrokeWidth(1);
            canvas.drawCircle(point.x, point.y, POINT_RADIO_WIDTH + 2, mPointPaint);
        }

        // 缁樺埗绗?n 闃剁殑鎺у埗鍩虹嚎
        for (int i = 0; i < mControlPointList.size() - 1; ++i) {
            canvas.drawLine(mControlPointList.get(i).x,
                    mControlPointList.get(i).y,
                    mControlPointList.get(i + 1).x,
                    mControlPointList.get(i + 1).y,
                    mControlPaint);
        }
    }

    /**
     * 灏嗚绠楀ソ鐨?璐濆灏旀洸绾跨殑鐐?缁勮鎴愯矾寰?     * 鑷充簬杩欒矾寰勪腑鏈夊灏戜釜鐐癸紝鍙栧喅浜巤@link #FRAME}灞炴€х殑鍊?     */
    private void prepareBezierPath() {
        for (int i = 0; i < mBezierPointList.size(); ++i) {
            PointF point = mBezierPointList.get(i);
            if (i == 0) {
                mBezierPath.moveTo(point.x, point.y);
            } else {
                mBezierPath.lineTo(point.x, point.y);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 娌℃湁鍦ㄥ噯澶囩姸鎬佷笉鑳借繘琛屾搷浣?        if (mState != PREPARE) {
            return true;
        }

        // 瑙︾鐨勫潗鏍?        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLegalControlPoint(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mCurSelectPoint == null) {
                    return true;
                }

                mCurSelectPoint.x = x;
                mCurSelectPoint.y = y;

                mIntermediateList.clear();
                mIntermediateDrawList.clear();
                if (mBezierPointList != null) {
                    mBezierPointList.clear();
                }
                mBezierPath.reset();

                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                mCurSelectPoint = null;
                break;
        }

        return true;
    }

    /**
     * 鑾峰彇 瑙︾鐐?鑼冨洿鍐呮湁鏁堢殑 鎺у埗鐐?     *
     * @param x
     * @param y
     */
    private void isLegalControlPoint(float x, float y) {

        if (mCurSelectPoint == null) {

            for (PointF controlPoint : mControlPointList) {
                RectF pointRange = new RectF(controlPoint.x - mTouchRegionWidth,
                        controlPoint.y - mTouchRegionWidth,
                        controlPoint.x + mTouchRegionWidth,
                        controlPoint.y + mTouchRegionWidth);

                // 濡傛灉鍖呭惈浜嗗氨锛岃繑鍥瀟rue
                if (pointRange.contains(x, y)) {
                    mCurSelectPoint = controlPoint;
                    return;
                }

            }
        }

    }

    /**
     * 鑾峰彇鎺у埗绾裤€佹帶鍒剁偣鐨勮壊鍊?     *
     * @return
     */
    private int getControlLineColor() {
        return getColor("#1296db");
    }

    /**
     * 鑾峰彇璐濆灏旀洸绾跨殑鑹插€?     *
     * @return
     */
    private int getBezierLineColor() {
        return getColor("#d81e06");
    }

    /**
     * 鑾峰彇 {@link #mLineColor} 鐨勫搴斾笅鏍囪壊鍊硷紝濡傛灉瓒婄晫鍒欏彇浣?     *
     * @param index 鑹插€间笅鏍?     * @return
     */
    private int getColor(int index) {
        return getColor(mLineColor.get(index % mLineColor.size()));
    }

    /**
     * 鑾峰彇棰滆壊
     *
     * @param color 鑹插€硷紝鏍煎紡锛?xxxxxx
     * @return
     */
    private int getColor(String color) {
        return Color.parseColor(color);
    }

    /**
     * 璁剧疆缁樺埗鍊?     *
     * @param intermediateDrawList
     */
    private void setIntermediateDrawList(List<List<PointF>> intermediateDrawList) {
        mIntermediateDrawList.clear();
        mIntermediateDrawList.addAll(intermediateDrawList);
    }

    private void setState(int state) {
        this.mState = state;
    }

    private int getRate() {
        return mRate;
    }

    private int getSize() {
        return mBezierPointList.size();
    }

    private void setCurBezierPoint(PointF curBezierPoint) {
        this.mCurBezierPoint = curBezierPoint;
    }

    private List<PointF> getBezierPointList() {
        return mBezierPointList;
    }

    private List<List<List<PointF>>> getIntermediateList() {
        return mIntermediateList;
    }

    private static final class MyHandler extends Handler {

        // 璐濆灏旀洸绾跨殑瑙嗗浘
        private final BezierView mView;
        // 褰撳墠甯ф暟
        private int mCurFrame;

        public MyHandler(BezierView bezierView) {
            this.mView = bezierView;
            this.mCurFrame = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_EVENT) {

                // 鎸変簡 鏆傚仠锛屽垯涓嶅湪杩涜
                if (mView.getState() == STOP) {
                    return;
                }

                // 澧炲姞 甯ф暟锛岃绾跨Щ鍔ㄨ捣鏉?                mCurFrame += mView.getRate();
                // 褰撳抚鏁拌秴鍑虹晫闄愬垯涓嶅湪杩愯锛岃褰撳墠 甯ф暟 鍜?鐘舵€佸浣嶏紝骞朵笖娓呯┖闄嶉樁绾跨殑鏁版嵁
                if (mCurFrame >= mView.getSize()) {

                    mCurFrame = 0;

                    if (!mView.isLoop()) {
                        mView.setState(PREPARE);
                        mView.setIntermediateDrawList(new ArrayList<List<PointF>>());
                        mView.setCurRatio(1);
                        mView.invalidate();
                        return;
                    }

                }

                // 鑾峰彇褰撳墠鐨勮礉濉炲皵鏇茬嚎鐐?                List<PointF> bezierPointList = mView.getBezierPointList();
                mView.setCurBezierPoint(bezierPointList.get(mCurFrame));

                // 鏄惁瑕佹樉绀?闄嶉樁绾?                if (mView.isShowReduceOrderLine()) {
                    List<List<List<PointF>>> intermediateList = mView.getIntermediateList();

                    // 瀹炴椂鍙樺姩鐨勭嚎
                    List<List<PointF>> intermediateDrawList = new ArrayList<>();

                    for (int i = 0; i < intermediateList.size(); ++i) {

                        List<List<PointF>> lineList = intermediateList.get(i);
                        List<PointF> intermediatePoint = new ArrayList<>();

                        for (int j = 0; j < lineList.size(); ++j) {
                            float x = lineList.get(j).get(mCurFrame).x;
                            float y = lineList.get(j).get(mCurFrame).y;
                            intermediatePoint.add(new PointF(x, y));
                        }

                        intermediateDrawList.add(intermediatePoint);

                    }

                    mView.setIntermediateDrawList(intermediateDrawList);
                }


//                if (mCurFrame >= mView.getSize() - 1 && !mView.isLoop()) {
//                    mCurFrame = 0;
//                    mView.setState(PREPARE);
//                    mView.setIntermediateDrawList(new ArrayList<List<PointF>>());
//                }

                float ratio = (int) (((float) mCurFrame / mView.getSize()) * 100) / 100f;
                mView.setCurRatio(ratio > 1 ? 1 : ratio);

                // 鍒锋柊瑙嗗浘
                mView.invalidate();

            }

        }
    }

}

