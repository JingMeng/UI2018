package com.zinc.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/11/5
 * @description
 */
public class PlaneLoadingView extends BaseView {

    private static final float DELAY = 0.005f;

    // PathMeasure 娴嬮噺杩囩▼涓殑鍧愭爣
    private float mPos[];
    // PathMeasure 娴嬮噺杩囩▼涓殑姝ｅ垏
    private float mTan[];

    // 鍦堢殑鐢荤瑪
    private Paint mCirclePaint;

    // 绠ご鍥剧墖
    private Bitmap mArrowBitmap;

    // 鍦嗚矾寰?    private Path mCirclePath;

    // 璺緞娴嬮噺
    private PathMeasure mPathMeasure;

    // 褰撳墠绉诲姩鍊?    private float mCurrentValue = 0;

    private Matrix mMatrix;

    private ValueAnimator valueAnimator;

    public PlaneLoadingView(Context context) {
        super(context);
    }

    public PlaneLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaneLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(Context context) {
        // 鍒濆鍖?鐢荤瑪 [鎶楅敮榻裤€佷笉濉厖銆佺孩鑹层€佺嚎鏉?px]
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(2);

        // 鑾峰彇鍥剧墖
        mArrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, null);

        // 鍒濆鍖?鍦嗚矾寰?[鍦嗗績(0,0)銆佸崐寰?00px銆侀『鏃堕拡鐢籡
        mCirclePath = new Path();
        mCirclePath.addCircle(0, 0, 200, Path.Direction.CW);

        // 鍒濆鍖?瑁呰浇 鍧愭爣 鍜?姝ｄ綑寮?鐨勬暟缁?        mPos = new float[2];
        mTan = new float[2];

        // 鍒濆鍖?PathMeasure 骞朵笖鍏宠仈 鍦嗚矾寰?        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mCirclePath, false);

        // 鍒濆鍖栫煩闃?        mMatrix = new Matrix();

        // 鍒濆鍖?浼板€煎櫒 [鍖洪棿0-1銆佹椂闀?绉掋€佺嚎鎬у闀裤€佹棤闄愭寰幆]
        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(5000);
        // 鍖€閫熷闀?        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 绗竴绉嶅仛娉曪細閫氳繃鑷繁鎺у埗锛屾槸绠ご鍦ㄥ師鏉ョ殑浣嶇疆缁х画杩愯
                mCurrentValue += DELAY;
                if (mCurrentValue >= 1) {
                    mCurrentValue -= 1;
                }

                // 绗簩绉嶅仛娉曪細鐩存帴鑾峰彇鍙互閫氳繃浼板€煎櫒锛屾敼鍙樺叾鍙樺姩瑙勫緥
//                mCurrentValue = (float) animation.getAnimatedValue();

                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 鐢荤綉鏍煎拰鍧愭爣杞?        drawCoordinate(canvas);

        // 绉昏嚦canvas涓棿
        canvas.translate(mWidth / 2, mHeight / 2);

        // 鐢诲渾璺緞
        canvas.drawPath(mCirclePath, mCirclePaint);

        // 娴嬮噺 pos(鍧愭爣) 鍜?tan(姝ｅ垏)
        mPathMeasure.getPosTan(mPathMeasure.getLength() * mCurrentValue, mPos, mTan);

        // 璁＄畻瑙掑害
        float degree = (float) (Math.atan2(mTan[1], mTan[0]) * 180 / Math.PI);

        Log.i("PlaneLoadingView_1", "------------pos[0] = " + mPos[0] + "; pos[1] = " + mPos[1]);
        Log.i("PlaneLoadingView_2", "------------tan[0](cos) = " + mTan[0] + "; tan[1](sin) = " + mTan[1]);
        Log.i("PlaneLoadingView_3", "path length = " + mPathMeasure.getLength() * mCurrentValue);
        Log.i("PlaneLoadingView_4", "degree = " + degree);

        // 閲嶇疆鐭╅樀
        mMatrix.reset();
        // 璁剧疆鏃嬭浆瑙掑害
        mMatrix.postRotate(degree, mArrowBitmap.getWidth() / 2, mArrowBitmap.getHeight() / 2);
        // 璁剧疆鍋忕Щ閲?        mMatrix.postTranslate(mPos[0] - mArrowBitmap.getWidth() / 2,
                mPos[1] - mArrowBitmap.getHeight() / 2);

        // 鐢诲師鐐?        canvas.drawCircle(0, 0, 3, mCirclePaint);

        // 鐢荤澶达紝浣跨敤鐭╅樀鏃嬭浆
        canvas.drawBitmap(mArrowBitmap, mMatrix, mCirclePaint);

        // 鐢诲湪 绠ご 鍥炬爣鐨勪腑蹇冪偣
        canvas.drawCircle(mPos[0], mPos[1], 3, mCirclePaint);

    }

    public void startLoading() {
        valueAnimator.start();
    }

    public void stopLoading() {
        valueAnimator.cancel();
    }

}

