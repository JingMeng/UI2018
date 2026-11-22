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
 * @date 鍒涘缓鏃堕棿锛?019/1/4
 * @description 娴嬭瘯 {@link PathMeasure#getLength()}
 */
public class GetLengthView extends BaseView {

    Path mPath;

    PathMeasure mClosePathMeasure;
    PathMeasure mNoClosePathMeasure;
    PathMeasure mNoClosePathMeasure2;

    Paint mPaint;

    public GetLengthView(Context context) {
        super(context);
    }

    public GetLengthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GetLengthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean mIsInit;

    @Override
    protected void init(Context context) {

        if (!mIsInit) {
            mIsInit = true;

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(ContextCompat.getColor(context, R.color.color_blue));
            mPaint.setStrokeWidth(5f);
            mPaint.setStyle(Paint.Style.STROKE);

            mPath = new Path();

            mPath.lineTo(0, 100);
            mPath.lineTo(100, 100);
            mPath.lineTo(100, -100);
            mPath.lineTo(200, -100);
            mPath.lineTo(200, 0);

            mClosePathMeasure = new PathMeasure(mPath, true);
            float closeLength = mClosePathMeasure.getLength();

            mNoClosePathMeasure = new PathMeasure(mPath, false);
            float noCloseLength = mNoClosePathMeasure.getLength();

            // 褰撹矾寰凱ath鍙樺姩鍚庯紝PathMeasure闇€瑕侀噸鏂板叧鑱旓紝鍚﹀垯淇濆瓨鐨勮繕鏄箣鍓嶇殑path鏁版嵁銆?            // 鎵€浠ユ墽琛屼簡 mPath.close()鍚庯紝姝ゅ鐨?noCloseLength2 渚夸负800锛岃€?noCloseLength 杩樻槸 600
            mPath.close();
            mNoClosePathMeasure2 = new PathMeasure(mPath, false);
            float noCloseLength2 = mNoClosePathMeasure2.getLength();

            Log.i(TAG, "PathMeasure Info( forceClose=true )");
            Log.i(TAG, "Length:" + closeLength);
            Log.i(TAG, "isClose: " + mClosePathMeasure.isClosed());
            Log.i(TAG, "=======================================");

            Log.i(TAG, "PathMeasure Info( forceClose=false )");
            Log.i(TAG, "Length:" + noCloseLength);
            Log.i(TAG, "isClose: " + mNoClosePathMeasure.isClosed());
            Log.i(TAG, "=======================================");

            Log.i(TAG, "PathMeasure Info( forceClose=false ) Path is Close");
            Log.i(TAG, "Length:" + noCloseLength2);
            Log.i(TAG, "isClose: " + mNoClosePathMeasure2.isClosed());
            Log.i(TAG, "=======================================");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        // 骞崇Щ鑷崇敾甯冧腑闂?        canvas.translate(mWidth / 2, mHeight / 2);
        // 鐢绘姌绾胯矾寰?        canvas.drawPath(mPath, mPaint);
    }
}

