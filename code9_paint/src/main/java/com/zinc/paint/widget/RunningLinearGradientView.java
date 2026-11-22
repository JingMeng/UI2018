package com.zinc.paint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/21
 * @description 娴佸姩鐨勬笎鍙樻晥鏋? */
public class RunningLinearGradientView extends android.support.v7.widget.AppCompatTextView {

    // 涓€娆″亸绉荤殑閲?    private float DELAY = 20;

    private TextPaint paint;
    private Matrix matrix;

    private LinearGradient linearGradient;

    // 宸茬粡鐨勫亸绉婚噺
    private float translate;

    public RunningLinearGradientView(Context context) {
        this(context, null, 0);
    }

    public RunningLinearGradientView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RunningLinearGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int curRow = 1;
    private int row;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        paint = getPaint();
        String text = getText().toString();

        // 鏂囧瓧瀹藉害
        float textWidth = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        // 鏂囧瓧鐨勬€婚珮搴?        float textHeight = fontMetrics.bottom - fontMetrics.top;
        // 鏂囧瓧鐨勫崟涓珮搴︼紙鍗宠楂樺害锛?        float textRowHeight = getTextSize();

        row = (int) (textHeight / textRowHeight);

        int gradientSize = (int) (textWidth / text.length() * 3);

        linearGradient = new LinearGradient(
                -gradientSize,
                textRowHeight * curRow,
                0,
                textRowHeight * curRow,
                new int[]{0x22ffffff, 0xffffffff, 0x22ffffff},
                null,
                Shader.TileMode.CLAMP);

        paint.setShader(linearGradient);

    }

    private void init(Context context) {
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        translate += DELAY;
        float textWidth = getPaint().measureText(getText().toString());
        if (translate > textWidth + 1 || translate < 1) {
            DELAY = 0;
            curRow++;
            if (curRow > row) {
                curRow = 0;
            }
        }

        matrix.setTranslate(translate, 0);
        linearGradient.setLocalMatrix(matrix);
        postInvalidateDelayed(50);

    }
}

