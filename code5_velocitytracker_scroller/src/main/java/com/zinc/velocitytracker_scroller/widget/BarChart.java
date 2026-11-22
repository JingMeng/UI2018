package com.zinc.velocitytracker_scroller.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * author : zinc
 * email : 56002982@qq.com
 * time : 2019/3/1 涓嬪崍5:04
 * desc : 甯︽粦鍔ㄦ晥鏋滅殑鏌卞舰鍥? * version :
 */
public class BarChart extends View {

    private static final String TAG = "BarChart";
    /**
     * 鍐呭渾鐨勯鑹?     */
    private static final String INNER_DOT_COLOR = "#99E35B5B";
    /**
     * 澶栧渾鐨勯鑹?     */
    private static final String OUTER_DOT_COLOR = "#28E35B5B";
    /**
     * 鏌辩殑棰滆壊
     */
    private static final String BAR_COLOR = "#bb434343";
    /**
     * 鏂囧瓧棰滆壊
     */
    private static final String TEXT_COLOR = "#64C5C5C5";
    /**
     * 鍔ㄧ敾鏃堕暱
     */
    private static final int ANIM_DURATION = 2000;

    /**
     * 鏌卞瓙鐨勬暟鎹?     */
    private List<BarInfo> mBarInfoList = new ArrayList<>();
    /**
     * 鎻忚堪瀛椾綋鐨勫ぇ灏?     */
    private float mDescTextSize;
    /**
     * 鐐圭殑鍐呭崐寰?     */
    private float mDotInnerRadius;
    /**
     * 鐐圭殑澶栧崐寰?     */
    private float mDotOuterRadius;
    /**
     * 搴曢儴杈硅窛
     */
    private float mBottomSpacing;
    /**
     * 鏌变笌鏂囧瓧鐨勮窛绂?     */
    private float mBarTextSpacing;
    /**
     * 鏌卞瓙涓庢煴瀛愮殑闂撮殧
     */
    private float mBarInterval;
    /**
     * 鏌卞瓙涓庝笂杈硅窛鐨勮窛绂?     */
    private float mTopSpacing;
    /**
     * 鏌卞瓙鐨勯珮搴?     */
    private float mBarHeight;
    /**
     * 姣忔牴鏌卞瓙鐨勫搴?     */
    private float mBarWidth;
    /**
     * 鏈夋暟鎹殑鐢诲竷瀹?     */
    private float mCanvasWidth;
    /**
     * 鐢ㄦ埛鍙鐨勮鍥惧
     */
    private float mViewWidth;
    /**
     * 鏌卞瓙璺緞
     */
    private Path mBarPath;
    /**
     * 鐢荤瑪
     */
    private Paint mPaint;
    /**
     * 褰撳墠鍔ㄧ敾鐨勮繘搴?     */
    private float mAnimRate = 0;
    /**
     * 鏌卞瓙棰滆壊
     */
    private int mBarColor;
    /**
     * 鍐呭渾棰滆壊
     */
    private int mInnerDotColor;
    /**
     * 澶栧渾棰滆壊
     */
    private int mOuterDotColor;
    /**
     * 瀛椾綋澶у皬
     */
    private int mTextColor;
    /**
     * 鏈€鍚庤Е纰扮殑x鍧愭爣
     */
    private float mLastTouchX;
    /**
     * 鍔ㄧ敾
     */
    private ValueAnimator mAnim;

    /**
     * 婊戝姩閫熷害杩借釜
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 婊戝姩鐨勬渶澶ч€熷害
     */
    private int mMaximumVelocity;
    /**
     * 婊戝姩鐨勬渶灏忛€熷害
     */
    private int mMinimumVelocity;

    /**
     * 婊戝姩绾跨▼
     */
    private FlingRunnable mFling;

    public BarChart(Context context) {
        this(context, null, 0);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        setClickable(true);

        mDescTextSize = dip2px(context, 12f);
        mDotInnerRadius = dip2px(context, 3.5f);
        mDotOuterRadius = dip2px(context, 5f);
        mBarInterval = dip2px(context, 40f);
        mBottomSpacing = dip2px(context, 10f);
        mBarTextSpacing = dip2px(context, 12f);
        mTopSpacing = dip2px(context, 10f);
        mBarWidth = dip2px(context, 1.25f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mBarPath = new Path();

        mBarColor = Color.parseColor(BAR_COLOR);
        mInnerDotColor = Color.parseColor(INNER_DOT_COLOR);
        mOuterDotColor = Color.parseColor(OUTER_DOT_COLOR);
        mTextColor = Color.parseColor(TEXT_COLOR);

        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        mFling = new FlingRunnable(context);

        mAnim = ValueAnimator.ofFloat(0, 1);
        mAnim.setDuration(ANIM_DURATION);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimRate = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    /**
     * 璁剧疆鍔ㄧ敾鏁版嵁
     *
     * @param barInfoList
     */
    public void setBarInfoList(List<BarInfo> barInfoList) {
        this.mBarInfoList.clear();
        this.mBarInfoList.addAll(barInfoList);
        this.mCanvasWidth = (this.mBarInfoList.size() + 1) * this.mBarInterval;

        // 鍋滄姝ｅ湪鎵ц鐨勫姩鐢?        if (mAnim != null && mAnim.isRunning()) {
            mAnim.cancel();
        }

        // 鍋滄婊氬姩
        if (mFling != null) {
            mFling.stop();
        }

        // 閲嶇疆鍔ㄧ敾杩涘害
        mAnimRate = 0;

        // 婊氬洖鏈€寮€濮嬬殑鍧愭爣
        scrollTo(0, 0);

        // 鎻愪氦鍒锋柊
        postInvalidate();
    }

    /**
     * 鍚姩鍔ㄧ敾
     */
    public void start() {
        if (mBarInfoList == null || mBarInfoList.size() == 0) {
            Log.e(TAG, "鍚姩鍔ㄧ敾鍓嶏紝璇峰厛璁剧疆鏁版嵁");
            return;
        }

        mAnimRate = 0;

        if (mAnim.isRunning()) {
            mAnim.cancel();
        }

        mAnim.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 鏌卞瓙鐨勯珮搴?= 鎺т欢楂樺害 - 涓婂唴杈硅窛 - 涓嬪唴杈硅窛 - 瀛椾綋澶у皬 - 瀛椾綋涓庢煴瀛愮殑闂磋窛
        this.mBarHeight = h - mTopSpacing - mBottomSpacing - mDescTextSize - mBarTextSpacing;
        this.mViewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBar(canvas);
        drawDot(canvas);
        drawText(canvas);
    }

    /**
     * 鎺у埗灞忓箷涓嶈秺鐣?     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 褰撴暟鎹殑闀垮害涓嶈冻浠ユ粦鍔ㄦ椂锛屼笉鍋氭粦鍔ㄥ鐞?        if (mCanvasWidth < mViewWidth) {
            return true;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mLastTouchX = event.getX();

            mFling.stop();
        } else if (MotionEvent.ACTION_MOVE == event.getAction()) {
            // 婊戝姩鐨勮窛绂?            float scrollLengthX = event.getX() - mLastTouchX;
            // getScrollX() 灏忎簬0锛岃鏄庣敾甯冨彸绉讳簡
            // getScrollX() 澶т簬0锛岃鏄庣敾甯冨乏绉讳簡
            float endX = getScrollX() - scrollLengthX;

            if (scrollLengthX > 0) {    // 鐢诲竷寰€鍙崇Щ鍔?-->

                // 娉ㄦ剰锛氳繖閲岀殑绛夊彿涓嶈兘鍘婚櫎锛屽惁鍒欎細鏈夐棯鍔?                if (endX <= 0) {
                    scrollTo(0, 0);
                } else {
                    scrollBy((int) -scrollLengthX, 0);
                }

            } else if (scrollLengthX < 0) {                    // 鐢诲竷寰€宸︾Щ鍔? <--

                if (endX >= mCanvasWidth - mViewWidth) {     // 闇€瑕佽€冭檻鏄惁鍙宠秺鐣?                    scrollTo((int) (mCanvasWidth - mViewWidth), 0);
                } else {
                    scrollBy((int) -scrollLengthX, 0);
                }

            }
            mLastTouchX = event.getX();
        } else if (MotionEvent.ACTION_UP == event.getAction()) {
            // 璁＄畻褰撳墠閫熷害锛?1000琛ㄧず姣忕鍍忕礌鏁扮瓑
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

            // 鑾峰彇妯悜閫熷害
            int velocityX = (int) mVelocityTracker.getXVelocity();

            // 閫熷害瑕佸ぇ浜庢渶灏忕殑閫熷害鍊硷紝鎵嶅紑濮嬫粦鍔?            if (Math.abs(velocityX) > mMinimumVelocity) {

                int initX = getScrollX();

                int maxX = (int) (mCanvasWidth - mViewWidth);
                if (maxX > 0) {
                    mFling.start(initX, velocityX, initX, maxX);
                }
            }

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }

        }

        return super.onTouchEvent(event);

    }

    /**
     * 鐢绘煴
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        mBarPath.reset();
        for (int i = 0; i < mBarInfoList.size(); ++i) {

            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {
                mBarPath.moveTo(x, mTopSpacing);
                mBarPath.lineTo(x, mBarHeight + mTopSpacing);
            }

        }

        mPaint.setColor(mBarColor);
        mPaint.setStrokeWidth(mBarWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mBarPath, mPaint);
    }

    /**
     * 鐢绘暟鎹偣
     *
     * @param canvas
     */
    private void drawDot(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mBarInfoList.size(); ++i) {
            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {

                BarInfo barInfo = mBarInfoList.get(i);

                float curBarDotY = (float) (mBarHeight * (1 - barInfo.percent * mAnimRate) + mTopSpacing);

                // 鐢诲鍦?                mPaint.setColor(mOuterDotColor);
                canvas.drawCircle(x, curBarDotY, mDotOuterRadius, mPaint);

                // 鐢诲唴鍦?                mPaint.setColor(mInnerDotColor);
                canvas.drawCircle(x, curBarDotY, mDotInnerRadius, mPaint);
            }

        }
    }

    /**
     * 鐢绘枃瀛?     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        float textY = mTopSpacing + mBarHeight + mBarTextSpacing + mDescTextSize / 2;

        for (int i = 0; i < mBarInfoList.size(); ++i) {
            float x = (i + 1) * mBarInterval;

            if (isInVisibleArea(x)) {
                BarInfo barInfo = mBarInfoList.get(i);

                mPaint.setColor(mTextColor);
                mPaint.setTextSize(mDescTextSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(barInfo.desc, x, textY, mPaint);
            }
        }
    }

    /**
     * 鏄惁鍦ㄥ彲瑙嗙殑鑼冨洿鍐?     *
     * @param x
     * @return true锛氬湪鍙鐨勮寖鍥村唴锛沠alse锛氫笉鍦ㄥ彲瑙嗙殑鑼冨洿鍐?     */
    private boolean isInVisibleArea(float x) {
        float dx = x - getScrollX();

        return -mBarInterval <= dx && dx <= mViewWidth + mBarInterval;
    }

    private int dip2px(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    /**
     * author : Jiang zinc
     * email : 56002982@qq.com
     * time : 2019/3/1 涓嬪崍5:08
     * desc : 鏌卞舰鍥剧殑鏁版嵁
     * version :
     */
    public static final class BarInfo {
        /**
         * 璇ユ煴鐨勬弿杩?         */
        private String desc;
        /**
         * 璇ユ煴鐨勫崰姣?         */
        private double percent;

        public BarInfo(String desc, double percent) {
            this.desc = desc;
            this.percent = percent;
        }
    }

    /**
     * 婊氬姩绾跨▼
     */
    private class FlingRunnable implements Runnable {

        private Scroller mScroller;

        private int mInitX;
        private int mMinX;
        private int mMaxX;
        private int mVelocityX;

        FlingRunnable(Context context) {
            this.mScroller = new Scroller(context, null, false);
        }

        void start(int initX,
                   int velocityX,
                   int minX,
                   int maxX) {
            this.mInitX = initX;
            this.mVelocityX = velocityX;
            this.mMinX = minX;
            this.mMaxX = maxX;

            // 鍏堝仠姝笂涓€娆＄殑婊氬姩
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }

            // 寮€濮?fling
            mScroller.fling(initX, 0, velocityX,
                    0, 0, maxX, 0, 0);
            post(this);
        }

        @Override
        public void run() {

            // 濡傛灉宸茬粡缁撴潫锛屽氨涓嶅啀杩涜
            if (!mScroller.computeScrollOffset()) {
                return;
            }

            // 璁＄畻鍋忕Щ閲?            int currX = mScroller.getCurrX();
            int diffX = mInitX - currX;

            Log.i(TAG, "run: [currX: " + currX + "]\n"
                    + "[diffX: " + diffX + "]\n"
                    + "[initX: " + mInitX + "]\n"
                    + "[minX: " + mMinX + "]\n"
                    + "[maxX: " + mMaxX + "]\n"
                    + "[velocityX: " + mVelocityX + "]\n"
            );

            // 鐢ㄤ簬璁板綍鏄惁瓒呭嚭杈圭晫锛屽鏋滃凡缁忚秴鍑鸿竟鐣岋紝鍒欎笉鍐嶈繘琛屽洖璋冿紝鍗充娇婊氬姩杩樻病鏈夊畬鎴?            boolean isEnd = false;

            if (diffX != 0) {

                // 瓒呭嚭鍙宠竟鐣岋紝杩涜淇
                if (getScrollX() + diffX >= mCanvasWidth - mViewWidth) {
                    diffX = (int) (mCanvasWidth - mViewWidth - getScrollX());
                    isEnd = true;
                }

                // 瓒呭嚭宸﹁竟鐣岋紝杩涜淇
                if (getScrollX() <= 0) {
                    diffX = -getScrollX();
                    isEnd = true;
                }

                if (!mScroller.isFinished()) {
                    scrollBy(diffX, 0);
                }
                mInitX = currX;
            }

            if (!isEnd) {
                post(this);
            }
        }

        /**
         * 杩涜鍋滄
         */
        void stop() {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
        }
    }
}

