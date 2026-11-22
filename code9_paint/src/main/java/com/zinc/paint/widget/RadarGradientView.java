package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.lib_base.UIUtils;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/16
 * @description 闆疯揪鍥? */
public class RadarGradientView extends View {

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
    private static final int INTERVAL = 50;

    public RadarGradientView(Context context) {
        this(context, null, 0);
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 鍒濆鍖?     *
     * @param context
     */
    public void init(Context context) {

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

        // 鍒濆鍖栨壂鎻忕嚎绋?        scanRunnable = new Runnable() {
            @Override
            public void run() {
                matrix.postRotate(speed, mWidth / 2, mWidth / 2);
                invalidate();
                postDelayed(scanRunnable, INTERVAL);
            }
        };

        post(scanRunnable);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float tempWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - circlePaintWidth;
        float tempHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - circlePaintWidth;

        mWidth = Math.min(tempHeight, tempWidth);

        // 鍒濆鍖栨牱寮忥紱鍒ょ┖鏄洜涓猴紝7.0涔嬪悗鐨刼nMeasure浼氳蛋涓ゆ锛岄伩鍏嶄笉蹇呰鐨勫垵濮嬪寲
        if (scanShader == null) {
            scanShader = new SweepGradient(mWidth / 2, mWidth / 2,
                    new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
            scanPaint.setShader(scanShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        // 寰幆鐢诲湀
        for (float pot : pots) {
            canvas.drawCircle(mWidth / 2, mWidth / 2, pot * mWidth / 2, circlePaint);
        }

        canvas.save();

        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mWidth / 2,
                pots[pots.length - 1] * mWidth / 2, scanPaint);
//        canvas.drawRect(new RectF(0, 0, mWidth, mWidth), scanPaint);

        canvas.restore();

    }

}

