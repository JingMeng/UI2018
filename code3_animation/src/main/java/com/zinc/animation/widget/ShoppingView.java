package com.zinc.animation.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.zinc.animation.utils.BezierUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/26
 * @description
 */
public class ShoppingView extends android.support.v7.widget.AppCompatImageView {

    // 缂╂斁鍔ㄧ敾
    private ObjectAnimator mScaleXAnimator;
    private ObjectAnimator mScaleYAnimator;
    // 鏃嬭浆鍔ㄧ敾
    private ObjectAnimator mRotateAnimator;
    // 绉诲姩鍔ㄧ敾
    private ObjectAnimator mTranslateAnimator;

    // 鍔ㄧ敾闆嗗悎
    private AnimatorSet mAnimatorSet;

    private AnimatorListener mListener;

    // 寮€濮嬪潗鏍囷紝鐩稿浜庡叏灞忓箷鍖呮嫭鐘舵€佹爮
    private final PointF mStartPos = new PointF(0, 0);
    // 缁撴潫鍧愭爣锛岀浉瀵逛簬鍏ㄥ睆骞曞寘鎷姸鎬佹爮
    private final PointF mEndPos = new PointF(0, 0);

    public ShoppingView(Context context) {
        this(context, null, 0);
    }

    public ShoppingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(AnimatorListener listener) {
        this.mListener = listener;
    }

    private void init(Context context) {

        mAnimatorSet = new AnimatorSet();

        // X銆乊 鍧囩缉灏?鍊?        mScaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0.8f);
        mScaleYAnimator.setDuration(200);
        mScaleYAnimator.setInterpolator(new AccelerateInterpolator());

        mScaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0.8f);
        mScaleXAnimator.setDuration(200);
        mScaleXAnimator.setInterpolator(new AccelerateInterpolator());

        // 椤烘椂閽堟棆杞?35 搴?        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 35);
        mRotateAnimator.setDuration(100);
        mRotateAnimator.setInterpolator(new LinearInterpolator());

    }

    /**
     * 璁剧疆鍧愭爣
     *
     * @param position
     */
    public void setPosition(PointF position) {
        if (position == null) {
            return;
        }

        Log.i("shop", "setPosition: " + position.toString());

        setTranslationX(position.x);
        setTranslationY(position.y);
    }

    public void start(PointF startPoint, PointF endPoint) {

        setPosition(startPoint);

        mTranslateAnimator = ObjectAnimator.ofObject(this,
                "position",
                new BezierEvaluator(startPoint, endPoint),
                startPoint,
                endPoint);

        mTranslateAnimator.setDuration(450);
        mTranslateAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimatorSet.play(mScaleYAnimator)
                .with(mScaleXAnimator)
                .before(mRotateAnimator);
        mAnimatorSet.play(mRotateAnimator)
                .before(mTranslateAnimator);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.animEnd();
                }

                // 灏嗚嚜宸辩Щ闄?                ViewGroup parent = (ViewGroup) ShoppingView.this.getParent();
                if (parent != null && parent instanceof ViewGroup) {
                    parent.removeView(ShoppingView.this);
                }

            }
        });

        mAnimatorSet.start();

    }

    /**
     * 鍔ㄧ敾鍥炶皟
     */
    public interface AnimatorListener {
        /**
         * 鍔ㄧ敾缁撴潫
         */
        void animEnd();
    }

    private static class BezierEvaluator implements TypeEvaluator<PointF> {

        private final List<PointF> pointList;

        public BezierEvaluator(PointF startPoint, PointF endPoint) {
            this.pointList = new ArrayList<>();

            PointF controlPointF = new PointF(endPoint.x, startPoint.y);

            pointList.add(startPoint);
            pointList.add(controlPointF);
            pointList.add(endPoint);

        }

        @Override
        public PointF evaluate(float fraction, PointF startPoint, PointF endPoint) {
            return new PointF(BezierUtils.calculatePointCoordinate(BezierUtils.X_TYPE, fraction, 2, 0, pointList),
                    BezierUtils.calculatePointCoordinate(BezierUtils.Y_TYPE, fraction, 2, 0, pointList));
        }
    }

}
