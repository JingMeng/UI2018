package com.zinc.drawable_gravity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/29
 * @description
 */
public class RevealDrawable extends Drawable {

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private Drawable unselectedDrawable;
    private Drawable selectedDrawable;
    //鏂瑰悜
    private int orientation;

    // 姣旂巼
    private float ratio;

    private int leftWidth;
    private int rightWidth;

    private int topHeight;
    private int bottomHeight;

    private Rect rect;

    public RevealDrawable(Drawable unselectedDrawable,
                          Drawable selectedDrawable,
                          int orientation) {
        this.unselectedDrawable = unselectedDrawable;
        this.selectedDrawable = selectedDrawable;
        this.orientation = orientation;
        this.rect = new Rect();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        Log.i("RevealDrawable", "draw: ");
        // 鍦╫nLevelChange鏃讹紝bounds涓繕娌℃湁鍊?        Rect bounds = getBounds();

        if (orientation == HORIZONTAL) {      // 妯悜

            leftWidth = (int) (ratio * bounds.width());
            rightWidth = bounds.width() - leftWidth;
            topHeight = bounds.height();
            bottomHeight = bounds.height();

        } else if (orientation == VERTICAL) {  // 绾靛悜

            leftWidth = bounds.width();
            rightWidth = bounds.width();
            topHeight = (int) (ratio * bounds.height());
            bottomHeight = bounds.height() - topHeight;

        }

        Log.i("onLevelChange", "ratio:" + ratio + "; " +
                "leftWidth:" + leftWidth + "; " +
                "topHeight:" + topHeight + "; " +
                "rightHeight:" + rightWidth + "; " +
                "bottomHeight:" + bottomHeight);

        Gravity.apply(Gravity.LEFT,
                leftWidth,
                topHeight,
                bounds,
                rect);

        canvas.save();
        canvas.clipRect(rect);
        unselectedDrawable.draw(canvas);
        canvas.restore();


        Gravity.apply(Gravity.RIGHT,
                rightWidth,
                bottomHeight,
                bounds,
                rect
        );

        canvas.save();
        canvas.clipRect(rect);
        selectedDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        unselectedDrawable.setBounds(bounds);
        selectedDrawable.setBounds(bounds);
        Log.i("onBoundsChange", "width:" + bounds.width() +
                "; height:" + bounds.height());
    }

    @Override
    public int getIntrinsicHeight() {
        return Math.max(selectedDrawable.getIntrinsicHeight(),
                unselectedDrawable.getIntrinsicHeight());
    }

    @Override
    public int getIntrinsicWidth() {
        return Math.max(selectedDrawable.getIntrinsicWidth(),
                unselectedDrawable.getIntrinsicWidth());
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    /**
     * 褰撹皟鐢ㄤ簡 {@link Drawable#setLevel(int)} 鏃讹紝浼氬洖璋冭繖涓柟娉?     *
     * @param level 鍙栧€艰寖鍥翠负 0-10000
     * @return
     */
    @Override
    protected boolean onLevelChange(int level) {

        // 璁＄畻姣旂巼
        ratio = level / 10000f;

        // 鍒锋柊鑷繁锛屼細璋冪敤draw鏂规硶
        invalidateSelf();

        return true;
    }

    /**
     * 鑾峰彇閫忔槑搴?     *
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}

