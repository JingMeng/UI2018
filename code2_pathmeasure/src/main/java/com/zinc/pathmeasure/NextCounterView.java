package com.zinc.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/12/6
 * @description
 */
public class NextCounterView extends BaseView {

    // 鐢ㄤ簬楠岃瘉 nextContour 鏂规硶鐨勮矾寰?    Path mNextContourPath;
    PathMeasure mNextContourPathMeasure;

    boolean isInit = false;

    Paint mPaint;

    public NextCounterView(Context context) {
        super(context);
    }

    public NextCounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NextCounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_blue));
        mPaint.setStyle(Paint.Style.STROKE);

        mNextContourPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            mNextContourPath.moveTo(-100, -100);
            mNextContourPath.lineTo(-100, 100);
            mNextContourPath.lineTo(100, 100);
            mNextContourPath.lineTo(100, -100);
            mNextContourPath.lineTo(-100, -100);

            mNextContourPath.moveTo(-50, -50);
            mNextContourPath.lineTo(-50, 50);
            mNextContourPath.lineTo(50, 50);
            mNextContourPath.lineTo(50, -50);

            mNextContourPath.moveTo(50, -50);
            mNextContourPath.lineTo(-50, -50);

            mNextContourPathMeasure = new PathMeasure(mNextContourPath, false);

            Log.i(TAG, "NextContour");
            int i = 0;
            while (mNextContourPathMeasure.nextContour()) {
                ++i;
                Log.i(TAG, "绗? + i + "涓疆寤撶殑 Length:" + mNextContourPathMeasure.getLength());
            }

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        // 骞崇Щ鑷崇敾甯冧腑闂?        canvas.translate(mWidth / 2, mHeight / 2);

        // 鐢?娴嬭瘯nextContour 鐨勮矾寰?        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_purple));
        mPaint.setStrokeWidth(2.5f);
        canvas.drawPath(mNextContourPath, mPaint);
    }

}

