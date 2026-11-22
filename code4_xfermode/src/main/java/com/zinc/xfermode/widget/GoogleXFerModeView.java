package com.zinc.xfermode.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/22
 * @description FerMode 鐨?鏍峰紡浣跨敤
 * <p>
 * 娉ㄦ剰鐨勬槸锛岃鍜実oogle鐨勭ず渚嬩竴妯′竴鏍烽渶瑕佸湪manifest涓鍔? * android:hardwareAccelerated="false"
 * 鍏抽棴纭欢鍔犻€燂紝鍏跺疄灏辨槸閬垮紑 openGL 鐨勭粯鍒? * 鍚﹀垯Clear銆丏arken銆丩ighten浼氬拰绀轰緥涓殑涓嶅お涓€鏍? k
 */
public class GoogleXFerModeView extends View {

    private String TAG = "XFerModeView";

    static Bitmap makeDst(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xffFFCC44);

        canvas.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), paint);

        return bitmap;
    }

    static Bitmap makeSrc(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff66AAFF);
        canvas.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, paint);

        return bitmap;

    }

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),

            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),

            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),

            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "Xor",
            "SrcOver", "DstOver", "SrcIn", "DstIn",
            "SrcOut", "DstOut", "SrcATop", "DstATop",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    public GoogleXFerModeView(Context context) {
        this(context, null, 0);
    }

    public GoogleXFerModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleXFerModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    // 杈规鐢荤瑪
    private Paint borderPaint;
    // 瀛椾綋鐢荤瑪
    private Paint textPaint;

    private boolean isInit = false;
    // 姣忎釜妗嗙殑瀹藉害
    private float itemWidth;
    // 妯悜杈圭晫
    private float horizontalOffset = 10;
    // 绾靛悜杈圭晫
    private float verticalOffset;

    // 杈规鐨勫
    private int itemBorderWidth = 2;

    // 瀛椾綋澶у皬
    private int textSize = 25;

    private Bitmap dstBitmap;
    private Bitmap rectBitmap;

    private Paint bitmapPaint;

    // 鑳屾櫙
    private Shader itemBackground;

    private void init(Context context) {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStrokeWidth(itemBorderWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        // 瀛椾綋灞呬腑[鍗崇敾鐨勭偣涓哄瓧浣撲腑闂碷
        textPaint.setTextAlign(Paint.Align.CENTER);

        // 鍥剧殑缁樺埗绗?        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);

        Bitmap bm = Bitmap.createBitmap(new int[]{0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF},
                2, 2, Bitmap.Config.RGB_565);
        itemBackground = new BitmapShader(bm,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        itemBackground.setLocalMatrix(m);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isInit) {
            isInit = true;

            int width = getMeasuredWidth();
            // 璁＄畻姣忎釜瀹界殑澶у皬
            itemWidth = (width - 4 * horizontalOffset) / 4;

            verticalOffset = itemWidth * 0.1f;

            int borderWidth = itemBorderWidth * 2;
            dstBitmap = makeDst((int) itemWidth - borderWidth, (int) itemWidth - borderWidth);
            rectBitmap = makeSrc((int) itemWidth - borderWidth, (int) itemWidth - borderWidth);

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < 4; ++row) {
            for (int col = 0; col < 4; ++col) {

                float translateX = horizontalOffset / 2 + (horizontalOffset + itemWidth) * col;
                float translateY = (verticalOffset + itemWidth + textSize) * row;

                float translateYItem = textSize + verticalOffset;

                // 缁樺埗杈规
                borderPaint.setShader(null);
                borderPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(translateX,
                        translateY + translateYItem,
                        translateX + itemWidth,
                        translateY + itemWidth + translateYItem,
                        borderPaint);

                // 缁樺埗鑳屾櫙
                borderPaint.setShader(itemBackground);
                borderPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(translateX,
                        translateY + translateYItem,
                        translateX + itemWidth,
                        translateY + itemWidth + translateYItem,
                        borderPaint);

                // 瀛樺偍鍥惧眰
                int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                        Canvas.ALL_SAVE_FLAG);

                canvas.translate(translateX, translateY);

                // 缁樺埗瀛椾綋
                canvas.drawText(sLabels[row * 4 + col],
                        itemWidth / 2,
                        (textSize + verticalOffset) / 2 + 10,
                        textPaint);

                canvas.translate(0, translateYItem);

                bitmapPaint.setXfermode(null);
                // 鐢诲渾
                canvas.drawBitmap(dstBitmap, itemBorderWidth, itemBorderWidth, bitmapPaint);
                bitmapPaint.setXfermode(sModes[row * 4 + col]);
                // 鐢荤煩褰?                canvas.drawBitmap(rectBitmap, itemBorderWidth, itemBorderWidth, bitmapPaint);

                canvas.restoreToCount(layer);
            }
        }

    }

}

