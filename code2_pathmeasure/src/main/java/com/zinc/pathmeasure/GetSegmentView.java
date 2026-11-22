package com.zinc.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/2
 * @description 娴嬭瘯 {@link android.graphics.PathMeasure#getSegment(float, float, Path, boolean)} api
 */
public class GetSegmentView extends BaseView {

    // 娴嬭瘯 getSegment鏂规硶 鐨勫睘鎬?    private Path mPath;   // 杩涜鎴彇鐨勮矾寰?    private Path mDst;      // 瀛樻斁鎴彇鐨勮矾寰?    private PathMeasure mGetSegmentPathMeasure;

    private Paint mPaint;

    private boolean mStartWithMoveTo = false;

    public GetSegmentView(Context context) {
        super(context);
        init(context);
    }

    public GetSegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GetSegmentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        mPath = new Path();
        mDst = new Path();

        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.color_purple));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2.5f);
        mPaint.setStyle(Paint.Style.STROKE);

        mGetSegmentPathMeasure = new PathMeasure();
        // 椤烘椂閽堢敾 鍗婂緞涓?00px鐨勫渾
        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        mGetSegmentPathMeasure.setPath(mPath, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        // 骞崇Щ鑷崇敾甯冧腑闂?        canvas.translate(mWidth / 2, mHeight / 2);

        // 閲嶇疆dst锛屾竻绌轰箣鍓嶈矾寰?        mDst.reset();

        mDst.moveTo(0, 0);
        mDst.lineTo(100, 100);

        mGetSegmentPathMeasure.getSegment(mGetSegmentPathMeasure.getLength() * 0.25f,
                mGetSegmentPathMeasure.getLength() * 0.5f,
                mDst,
                mStartWithMoveTo);

        canvas.drawPath(mDst, mPaint);
    }

    public void setStartWithMoveTo(boolean value) {
        mStartWithMoveTo = value;
        invalidate();
    }

}

