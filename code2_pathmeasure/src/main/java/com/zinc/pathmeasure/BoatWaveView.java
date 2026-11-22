package com.zinc.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/12/7
 * @description
 */
public class BoatWaveView extends BaseView {

    public BoatWaveView(Context context) {
        super(context);
    }

    public BoatWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoatWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 灏忚埞娴姳鐨勫搴?    private static final int BOAT_LENGTH = 200;
    // 娴姳鐨勫搴?    private int WAVE_LENGTH;

    // 灏忚埞娴姳鐨勯珮搴?    private static final int BOAT_WAVE_HEIGHT = 20;
    // 娉㈡氮楂樺害
    private static final int WAVE_HEIGHT = 35;

    // 娴姳姣忔鐨勫亸绉婚噺
    private final static int WAVE_OFFSET = 5;

    private boolean isInit = false;

    private int mWidth;
    private int mHeight;

    public Paint mWavePaint;
    // 娴锋氮鐨勮矾寰?    public Path mWavePath;
    // 灏忚埞鐨勬氮璺緞
    public Path mBoatWavePath;
    // 灏忚埞鐨勮矾寰?    public Path mBoatPath;

    private PathMeasure mBoatPathMeasure;

    // 灏忚埞鐨勬氮鑹插€?    private int mBoatBlue;
    // 娴姳鐨勮壊鍊?    private int mWaveBlue;

    // 灏忚埞褰撳墠鎵€澶勭殑鍊?    private float mCurValue = 0f;

    // 娴姳褰撳墠鐨勫亸绉婚噺
    private int mCurWaveOffset = 0;
    // 灏忚埞鐨勬氮鑺卞亸绉婚噺
    private int mBoatWaveOffset = 0;

    // 鐢ㄤ簬鍙樻崲灏忚埞鐨?    private Matrix mMatrix;

    // 灏忚埞鐨勫浘鐗?    private Bitmap mBoatBitmap;

    private ValueAnimator mAnimator;

    @Override
    protected void init(Context context) {

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);

        mBoatPath = new Path();
        mWavePath = new Path();
        mBoatWavePath = new Path();

        mBoatPathMeasure = new PathMeasure();

        mBoatBlue = ContextCompat.getColor(context, R.color.color_boat_blue);
        mWaveBlue = ContextCompat.getColor(context, R.color.color_wave_blue);

        mMatrix = new Matrix();

        // 鍔犺浇鍥剧墖
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        mBoatBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boat, options);

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(4000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurValue = (float) animation.getAnimatedValue();
                mCurWaveOffset = (mCurWaveOffset + WAVE_OFFSET) % mWidth;
                mBoatWaveOffset = (mBoatWaveOffset + WAVE_OFFSET/2) % mWidth;

                postInvalidate();
            }
        });

    }

    /**
     * @param path       璺緞
     * @param length     娴姳鐨勫搴?     * @param height     娴姳鐨勯珮搴?     * @param isClose    鏄惁瑕侀棴鍚?     * @param lengthTime 娴姳闀跨殑鍊嶆暟
     */
    private void initPath(Path path, int length, int height, boolean isClose, float lengthTime) {
        // 鍒濆鍖?灏忚埞鐨勮矾寰?        path.moveTo(-length, mHeight / 2);
        for (int i = -length; i < mWidth * lengthTime + length; i += length) {
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

        if (isClose) {
            path.rLineTo(0, mHeight / 2);
            path.rLineTo(-(mWidth * 2 + 2 * length), 0);
            path.close();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();

            WAVE_LENGTH = mWidth / 3;

            // 鍒濆鍖?灏忚埞鐨勬氮璺緞
            initPath(mBoatWavePath, WAVE_LENGTH, BOAT_WAVE_HEIGHT, true, 2);

            // 鍒濆鍖?娴殑璺緞
            initPath(mWavePath, WAVE_LENGTH, WAVE_HEIGHT, true, 2);

            // 鍒濆鍖?灏忚埞鐨勮矾寰?            initPath(mBoatPath, WAVE_LENGTH, BOAT_WAVE_HEIGHT, false, 1);

            // 璁?PathMeasure 涓?Path 鍏宠仈
            mBoatPathMeasure.setPath(mBoatPath, false);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        float length = mBoatPathMeasure.getLength();
        mBoatPathMeasure.getMatrix(length * mCurValue,
                mMatrix,
                PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        mMatrix.preTranslate(-mBoatBitmap.getWidth() / 2, -mBoatBitmap.getHeight() * 5 / 6);

        canvas.drawBitmap(mBoatBitmap, mMatrix, null);

        // 鐢昏埞鐨勬氮鑺?        canvas.save();
        canvas.translate(-mBoatWaveOffset, 0);
        mWavePaint.setColor(mBoatBlue);
        canvas.drawPath(mBoatWavePath, mWavePaint);
        canvas.restore();

        // 鐢绘氮鑺?        canvas.save();
        canvas.translate(-mCurWaveOffset, 0);
        mWavePaint.setColor(mWaveBlue);
        canvas.drawPath(mWavePath, mWavePaint);
        canvas.restore();

    }

    public void startAnim() {
        mAnimator.start();
    }

    public void stopAnim() {
        mAnimator.cancel();
    }

}

