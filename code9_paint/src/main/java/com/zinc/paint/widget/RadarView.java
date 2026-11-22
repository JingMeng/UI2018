package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.lib_base.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/30
 * @description
 */
public class RadarView extends View {

    // 鏈€澶氭樉绀虹殑灏忕偣
    private static int DOT_COUNT = 3;

    // 闆疯揪鍥惧 鍜?楂?    private float mWidth;
    // 鍦堢殑闂撮殧
    private float[] pots = new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1f};
    // 鍦堢殑鐢荤瑪
    private Paint circlePaint;
    // 鎵弿鐨勭敾绗?    private Paint scanPaint;
    // 鍦堢殑鐢荤瑪鐨勭矖缁?    private float circlePaintWidth;
    // 鎵弿娓叉煋 shader
    private Shader scanShader;
    // 鏃嬭浆鐭╅樀
    private Matrix matrix;
    // 鎵弿绾跨▼
    private Runnable scanRunnable;
    // 鎵弿閫熷害
    private float speed;
    // 鏇存柊闂撮殧
    private static final int INTERVAL = 20;

    // 鑼冨洿
    private RectF mRect;

    private float mViewWidth;
    private float mViewHeight;

    private final List<DotInfo> mDotInfoList = new ArrayList<>();

    public RadarView(Context context) {
        this(context, null, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 鍒濆鍖?     *
     * @param context
     */
    public void init(Context context) {

        mDotInfoList.clear();
        for (int i = 0; i < DOT_COUNT; ++i) {
            mDotInfoList.add(new DotInfo());
        }

        mRect = new RectF();

        circlePaintWidth = UIUtils.dip2px(context, 0.5f);

        matrix = new Matrix();

        // 鍒濆鍖栧湀鐢荤瑪
        circlePaint = new Paint();
        // 璁剧疆瀹?        circlePaint.setStrokeWidth(circlePaintWidth);
        // 璁剧疆鎶楅敮榻?        circlePaint.setAntiAlias(true);
        // 璁剧疆棰滆壊
        circlePaint.setColor(ContextCompat.getColor(context, R.color.color_circle));
        // 璁剧疆鏍峰紡锛屽彧鐢荤嚎锛屼笉濉厖
        circlePaint.setStyle(Paint.Style.STROKE);

        // 鍒濆鍖栨壂鎻忕瑪
        scanPaint = new Paint();
        // 璁剧疆鏍峰紡锛屽嵆鐢荤嚎锛屽張濉厖
        scanPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 璁剧疆鎶楅敮榻?        scanPaint.setAntiAlias(true);

        // 鍒濆鍖栭€熷害
        speed = 5;

        post(scanRunnable);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        float tempWidth = w - getPaddingLeft() - getPaddingRight() - circlePaintWidth;
        float tempHeight = h - getPaddingTop() - getPaddingBottom() - circlePaintWidth;

        mWidth = Math.min(tempHeight, tempWidth);

        // 鍒濆鍖栨牱寮忥紱鍒ょ┖鏄洜涓猴紝7.0涔嬪悗鐨刼nMeasure浼氳蛋涓ゆ锛岄伩鍏嶄笉蹇呰鐨勫垵濮嬪寲
        if (scanShader == null) {
            scanShader = new SweepGradient(0,
                    0,
                    new int[]{Color.TRANSPARENT, ContextCompat.getColor(getContext(), R.color.color_scan)},
                    null);
            scanPaint.setShader(scanShader);
        }

        mRect.left = -mWidth / 2;
        mRect.right = mWidth / 2;
        mRect.top = -mWidth / 2;
        mRect.bottom = mWidth / 2;

        for (DotInfo dotInfo : mDotInfoList) {
            dotInfo.setRect(mRect);
        }

    }

    public void start() {
        // 鍒濆鍖栨壂鎻忕嚎绋?        scanRunnable = new Runnable() {
            @Override
            public void run() {
                matrix.postRotate(speed, 0, 0);
                postDelayed(scanRunnable, INTERVAL);
                invalidate();
            }
        };

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        // 寰幆鐢诲湀
        for (float pot : pots) {
            canvas.drawCircle(0, 0, pot * mWidth / 2, circlePaint);
        }

        canvas.drawLine(-mWidth / 2, 0, mWidth / 2, 0, circlePaint);
        canvas.drawLine(0, -mWidth / 2, 0, mWidth / 2, circlePaint);

        canvas.save();

        canvas.concat(matrix);
        canvas.drawCircle(0, 0, mWidth / 2, scanPaint);

        canvas.restore();

    }

    private static class DotInfo {

        private static final int DURATION_MIN = 300;
        private static final int DURATION_MAX = 800;

        private static final int RADIUS_MAX = UIUtils.dip2px(5);
        private static final int RADIUS_MIN = UIUtils.dip2px(5);

        private Random random;

        // 鍙敤鑼冨洿
        private RectF rectF;

        // 璇ョ偣鎵€鍦?x杞?鍧愭爣
        private float x;
        // 璇ョ偣鎵€鍦?y杞?鍧愭爣
        private float y;

        // 璇ョ偣鐨勫姩鐢绘椂闀?        private int duration;
        // 褰撳墠鐐瑰凡缁忚繍琛岀殑鏃堕暱
        private int curTime;
        // 涓婃杩愯鐨勬椂闂?        private long beforeTime;

        // 鐐瑰崐寰?        private float radius;

        public DotInfo() {
            this.random = new Random();
        }

        public void setRect(RectF rectF) {
            this.rectF = rectF;
            buildDot();
        }

        private void buildDot() {
            calculatePointCoordination();

            duration = DURATION_MIN + random.nextInt(DURATION_MAX - DURATION_MIN);
            curTime = 0;
            beforeTime = System.currentTimeMillis();
            radius = RADIUS_MIN + random.nextFloat() * (RADIUS_MAX - RADIUS_MIN);
        }

        private void calculatePointCoordination() {
            x = random.nextFloat() * rectF.width();
            x = x - rectF.left;

            float yRange = (float) Math.sqrt(Math.pow(rectF.left, 2) - Math.pow(x, 2)) * 2;

            y = random.nextFloat() * yRange - yRange / 2;
        }
    }
}

