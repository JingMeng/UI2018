package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zinc.paint.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/17
 * @description
 */
public class ZoomImageView extends View {

    // 鏀惧ぇ鍊嶆暟
    private static final int FACTOR = 2;
    // 鏀惧ぇ闀滃ぇ灏?鍗婂緞
    private static final int RADIUS = 100;

    private Bitmap orgBitmap;
    private Bitmap largeBitmap;

    private ShapeDrawable shapeDrawable;
    private Matrix matrix;

    public ZoomImageView(Context context) {
        this(context, null, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        orgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.xyjy3);

        largeBitmap = Bitmap.createScaledBitmap(orgBitmap,
                orgBitmap.getWidth() * FACTOR,
                orgBitmap.getHeight() * FACTOR,
                true);

        // BitmapShader 鐨勪綔鐢?https://blog.csdn.net/nangeque/article/details/55682146
        BitmapShader shader = new BitmapShader(largeBitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        // 鍏充簬 OvalShape https://blog.csdn.net/zhangphil/article/details/52025152
        // OvalShape 鏄竴涓?鍦嗗舰鐨?shape
        // RectShape 鏄竴涓?鏂瑰舰鐨?shape
        shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(shader);
        // 鍒囧嚭鐭╁舰鍖哄煙锛岀敤鏉ョ敾鍦嗭紙鍐呭垏鍦嗭級
        // Drawable鐨剆etBounds鏂规硶鏈夊洓涓弬鏁帮紝setBounds(int left, int top, int right, int bottom),
        // 杩欎釜鍥涘弬鏁版寚鐨勬槸drawable灏嗗湪琚粯鍒跺湪canvas鐨勫摢涓煩褰㈠尯鍩熷唴銆?         shapeDrawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);

        matrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 鐢诲師鍥?        canvas.drawBitmap(orgBitmap, 0, 0, null);

        // 鐢绘斁澶у浘
        shapeDrawable.draw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // 鍋忕Щ
        matrix.setTranslate(-x * FACTOR + RADIUS, -y * FACTOR + RADIUS);
        shapeDrawable.getPaint().getShader().setLocalMatrix(matrix);
        shapeDrawable.setBounds((int) x - RADIUS,
                (int) y - RADIUS,
                (int) x + RADIUS,
                (int) y + RADIUS);

        invalidate();

        return true;
    }
}

