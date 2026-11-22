package com.zinc.bezier.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.bezier.R;
import com.zinc.bezier.utils.MathUtils;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/11
 * @description 绮樻€у皬绾㈢偣
 */

public class StickDotView extends android.support.v7.widget.AppCompatTextView {

    public DragView mDragView;
    private float mWidth, mHeight;

    private onDragStatusListener onDragListener;

    public StickDotView(Context context) {
        this(context, null, 0);
    }

    public StickDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //鑾峰緱鏍筕iew
        View rootView = getRootView();

        //鑾峰緱瑙︽懜浣嶇疆鍦ㄥ叏灞忔墍鍦ㄤ綅缃?        float mRawX = event.getRawX();
        float mRawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //璇锋眰鐖禫iew涓嶆嫤鎴?                getParent().requestDisallowInterceptTouchEvent(true);

                //鑾峰緱褰撳墠View鍦ㄥ睆骞曚笂鐨勪綅缃?                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);

                if (rootView instanceof ViewGroup) {
                    //鍒濆鍖栨嫋鎷芥椂鏄剧ず鐨刅iew
                    mDragView = new DragView(getContext());

                    //璁剧疆鍥哄畾鍦嗙殑鍦嗗績鍧愭爣
                    mDragView.setStickyPoint(cLocation[0] + mWidth / 2,
                            cLocation[1] + mHeight / 2,
                            mRawX,
                            mRawY);

                    //鑾峰緱缂撳瓨鐨刡itmap锛屾粦鍔ㄦ椂鐩存帴閫氳繃drawBitmap缁樺埗鍑烘潵
                    setDrawingCacheEnabled(true);
                    Bitmap bitmap = getDrawingCache();

                    if (bitmap != null) {
                        mDragView.setCacheBitmap(bitmap);
                        //灏咲ragView娣诲姞鍒癛ootView涓紝杩欐牱灏卞彲浠ュ叏灞忔粦鍔ㄤ簡
                        ((ViewGroup) rootView).addView(mDragView);
                        setVisibility(INVISIBLE);
                    }

                }

                break;

            case MotionEvent.ACTION_MOVE:

                //璇锋眰鐖禫iew涓嶆嫤鎴?                getParent().requestDisallowInterceptTouchEvent(true);
                if (mDragView != null) {
                    //鏇存柊DragView鐨勪綅缃?                    mDragView.setDragViewLocation(mRawX, mRawY);
                }

                break;

            case MotionEvent.ACTION_UP:

                getParent().requestDisallowInterceptTouchEvent(false);
                if (mDragView != null) {
                    //鎵嬫姮璧锋椂鏉ュ垽鏂悇绉嶆儏鍐?                    mDragView.setDragUp();
                }

                break;

        }
        return true;
    }

    public interface onDragStatusListener {
        void onDrag();

        void onMove();

        void onRestore();

        void onDismiss();
    }

    public void setOnDragListener(onDragStatusListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    /**
     * 鎷栨嫿鏃剁殑妞渾
     */
    public class DragView extends View {
        private static final int STATE_INIT = 0;//榛樿闈欐鐘舵€?        private static final int STATE_DRAG = 1;//鎷栨嫿鐘舵€?        private static final int STATE_MOVE = 2;//绉诲姩鐘舵€?        private static final int STATE_DISMISS = 3;//娑堝け鐘舵€?
        private Path mPath;
        private Paint mPaint;
        private Bitmap mCacheBitmap;
        // 鎷栨嫿鍦嗙殑鍦嗙偣
        private PointF mDragPointF;
        // 鍥哄畾鍦嗙殑鍦嗙偣
        private PointF mStickyPointF;
        // 浜岄樁璐濆灏旀洸绾挎帶鍒剁偣
        private PointF mControlPoint;
        // 鎷栨嫿鐨勮窛绂?        private float mDragDistance;
        // 鏈€澶ф嫋鎷借窛绂?        private float mMaxDistance = dpToPx(100);
        // View鐨勫鍜岄珮
        private float mWidth, mHeight;

        // 褰撳墠绾㈢偣鐨勭姸鎬?        private int mState;

        // 鍥哄畾鍦嗙殑鍗婂緞
        private int mStickRadius;
        // 鍥哄畾灏忓渾鐨勯粯璁ゅ崐寰?        private int mDefaultRadius = dpToPx(10);
        // 鎷栨嫿鍦嗙殑鍗婂緞
        private int mDragRadius = dpToPx(15);

        private int[] mExplodeRes = {R.drawable.explode1,
                R.drawable.explode2,
                R.drawable.explode3,
                R.drawable.explode4,
                R.drawable.explode5};

        private Bitmap[] mBitmaps;

        private int mExplodeIndex;

        public DragView(Context context) {
            this(context, null, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            // 鍒濆鍖朠ath
            mPath = new Path();

            // 鍒濆鍖朠aint
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);

            // 鍒濆鍖栨嫋鎷界偣
            mDragPointF = new PointF();
            // 鍒濆鍖栧浐瀹氱偣
            mStickyPointF = new PointF();

            // 鍒濆鍖栫姸鎬?            mState = STATE_INIT;

            // 鍒濆鍖栨秷澶卞姩鐢昏祫婧?            mBitmaps = new Bitmap[mExplodeRes.length];
            for (int i = 0; i < mExplodeRes.length; i++) {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mExplodeRes[i]);
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {

            if (isInsideRange() && mState == STATE_DRAG) {

                // 缁樺埗鍥哄畾鐨勫皬鍦?                canvas.drawCircle(mStickyPointF.x, mStickyPointF.y, mStickRadius, mPaint);

                // 棣栧厛鑾峰緱涓ゅ渾蹇冧箣闂寸殑鏂滅巼
                Float linK = MathUtils.getLineSlope(mDragPointF, mStickyPointF);

                // 鐒跺悗閫氳繃涓や釜鍦嗗績鍜屽崐寰勩€佹枩鐜囨潵鑾峰緱澶栧垏绾跨殑鍒囩偣
                PointF[] stickyPoints = MathUtils.getIntersectionPoints(mStickyPointF, mStickRadius, linK);
                mDragRadius = (int) Math.min(mWidth, mHeight) / 2;
                PointF[] dragPoints = MathUtils.getIntersectionPoints(mDragPointF, mDragRadius, linK);

                // 浜岄樁璐濆灏旀洸绾跨殑鎺у埗鐐瑰彇寰椾袱鍦嗗績鐨勪腑鐐?                mControlPoint = MathUtils.getMiddlePoint(mDragPointF, mStickyPointF);

                // 缁樺埗璐濆灏旀洸绾?                mPath.reset();
                mPath.moveTo(stickyPoints[0].x, stickyPoints[0].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, stickyPoints[1].x, stickyPoints[1].y);
                mPath.lineTo(stickyPoints[0].x, stickyPoints[0].y);

                canvas.drawPath(mPath, mPaint);
            }

            // 缁樺埗 StickDotView 鍐呭
            if (mCacheBitmap != null && mState != STATE_DISMISS) {
                canvas.drawBitmap(mCacheBitmap,
                        mDragPointF.x - mWidth / 2,
                        mDragPointF.y - mHeight / 2,
                        mPaint);
            }

            if (mState == STATE_DISMISS && mExplodeIndex < mExplodeRes.length) {
                // 缁樺埗灏忕孩鐐规秷澶辨椂鐨勭垎鐐稿姩鐢?                canvas.drawBitmap(mBitmaps[mExplodeIndex],
                        mDragPointF.x - mWidth / 2,
                        mDragPointF.y - mHeight / 2,
                        mPaint);
            }
        }

        /**
         * 璁剧疆缂撳瓨Bitmap锛屽嵆 StickDotView 鐨勮鍥?         *
         * @param mCacheBitmap 缂撳瓨Bitmap
         */
        public void setCacheBitmap(Bitmap mCacheBitmap) {
            this.mCacheBitmap = mCacheBitmap;
            mWidth = mCacheBitmap.getWidth();
            mHeight = mCacheBitmap.getHeight();
            mDefaultRadius = (int) Math.min(mWidth, mHeight) / 2;
        }


        /**
         * 璁剧疆鍥哄畾鍦嗙殑鍦嗗績鍜屽崐寰?         *
         * @param stickyX 鍥哄畾鍦嗙殑X鍧愭爣
         * @param stickyY 鍥哄畾鍦嗙殑Y鍧愭爣
         */
        public void setStickyPoint(float stickyX,
                                   float stickyY,
                                   float touchX,
                                   float touchY) {
            //鍒嗗埆璁剧疆鍥哄畾鍦嗗拰鎷栨嫿鍦嗙殑鍧愭爣
            mStickyPointF.set(stickyX, stickyY);
            mDragPointF.set(touchX, touchY);

            //閫氳繃涓や釜鍦嗙偣绠楀嚭鍦嗗績璺濓紝涔熸槸鎷栨嫿鐨勮窛绂?            mDragDistance = MathUtils.getTwoPointDistance(mDragPointF, mStickyPointF);

            if (mDragDistance <= mMaxDistance) {

                //濡傛灉鎷栨嫿璺濈灏忎簬瑙勫畾鏈€澶ц窛绂伙紝鍒欏浐瀹氱殑鍦嗗簲璇ヨ秺鏉ヨ秺灏忥紝杩欐牱鐪嬬潃鎵嶇鍚堝疄闄?                mStickRadius = (int) (mDefaultRadius - mDragDistance / 10) < 10 ? 10 : (int) (mDefaultRadius - mDragDistance / 10);
                mState = STATE_DRAG;

            } else {

                mState = STATE_INIT;

            }
        }

        /**
         * 璁剧疆鎷栨嫿鐨勫潗鏍囦綅缃?         *
         * @param touchX 鎷栨嫿鏃剁殑X鍧愭爣
         * @param touchY 鎷栨嫿鏃剁殑Y鍧愭爣
         */
        public void setDragViewLocation(float touchX, float touchY) {
            mDragPointF.set(touchX, touchY);
            //闅忔椂鏇存敼鍦嗗績璺?            mDragDistance = MathUtils.getTwoPointDistance(mDragPointF, mStickyPointF);
            if (mState == STATE_DRAG) {
                if (isInsideRange()) {
                    mStickRadius = (int) (mDefaultRadius - mDragDistance / 10) < 10 ? 10 : (int) (mDefaultRadius - mDragDistance / 10);
                } else {
                    mState = STATE_MOVE;
                    if (onDragListener != null) {
                        onDragListener.onMove();
                    }
                }
            }
            invalidate();
        }

        public void setDragUp() {

            if (mState == STATE_DRAG && isInsideRange()) {

                //鎷栨嫿鐘舵€佷笖鍦ㄨ寖鍥翠箣鍐?                startResetAnimator();

            } else if (mState == STATE_MOVE) {

                if (isInsideRange()) {
                    //鍦ㄨ寖鍥翠箣鍐?闇€瑕丷ESET
                    startResetAnimator();
                } else {
                    //鍦ㄨ寖鍥翠箣澶?娑堝け鍔ㄧ敾
                    mState = STATE_DISMISS;
                    startExplodeAnim();
                }

            }
        }


        /**
         * 鏄惁鍦ㄦ渶澶ф嫋鎷借寖鍥翠箣鍐?         *
         * @return true 鑼冨洿涔嬪唴 false 鑼冨洿涔嬪
         */
        private boolean isInsideRange() {
            return mDragDistance <= mMaxDistance;
        }

        /**
         * 鐖嗙偢鍔ㄧ敾
         */
        private void startExplodeAnim() {
            ValueAnimator animator = ValueAnimator.ofInt(0, mExplodeRes.length);
            animator.setDuration(300);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mExplodeIndex = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onDragListener != null) {
                        onDragListener.onDismiss();
                    }
                }
            });
            animator.start();
        }

        /**
         * 鎷栨嫿绾㈢偣reset鍔ㄧ敾
         */
        private void startResetAnimator() {

            if (mState == STATE_DRAG) {

                ValueAnimator animator = ValueAnimator.ofObject(
                        new PointEvaluator(),
                        new PointF(mDragPointF.x, mDragPointF.y),
                        new PointF(mStickyPointF.x, mStickyPointF.y));
                animator.setDuration(500);
                animator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        float factor = 0.4f;
                        return (float) (Math.pow(2, -10 * factor) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        PointF curPoint = (PointF) animation.getAnimatedValue();
                        mDragPointF.set(curPoint.x, curPoint.y);
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clearDragView();
                        if (onDragListener != null) {
                            onDragListener.onDrag();
                        }
                    }
                });
                animator.start();

            } else if (mState == STATE_MOVE) {

                //鍏堟嫋鎷藉埌鑼冨洿涔嬪 鍦ㄦ嫋鎷藉洖鑼冨洿涔嬪唴
                mDragPointF.set(mStickyPointF.x, mStickyPointF.y);
                invalidate();
                clearDragView();
                if (onDragListener != null) {
                    onDragListener.onRestore();
                }

            }

        }

        private void clearDragView() {
            ViewGroup viewGroup = (ViewGroup) getParent();
            viewGroup.removeView(DragView.this);
            StickDotView.this.setVisibility(VISIBLE);
        }
    }

    private static class PointEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + fraction * (endValue.x - startValue.x);
            float y = startValue.y + fraction * (endValue.y - startValue.y);
            return new PointF(x, y);
        }
    }

    /**
     * 杞崲 dp 鑷?px
     *
     * @param dp dp鍍忕礌
     * @return
     */
    protected int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }


}

