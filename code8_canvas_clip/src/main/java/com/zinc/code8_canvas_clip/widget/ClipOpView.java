package com.zinc.code8_canvas_clip.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019/4/26 涓婂崍12:22
 * desc         :
 * version      :
 */
public class ClipOpView extends BaseView {

    // 鏄疉褰㈢姸涓笉鍚屼簬B鐨勯儴鍒嗘樉绀哄嚭鏉?    public static final String DIFFERENCE = "DIFFERENCE";
    // 鏄疉鍜孊浜ら泦鐨勫舰鐘?    public static final String INTERSECT = "INTERSECT";
    // 鏄疉鍜孊鐨勫叏闆?    public static final String UNION = "UNION";
    // 鏄叏闆嗗舰鐘跺噺鍘讳氦闆嗗舰鐘朵箣鍚庣殑閮ㄥ垎
    public static final String XOR = "XOR";
    // 鏄疊褰㈢姸涓笉鍚屼簬A鐨勯儴鍒嗘樉绀哄嚭鏉ワ紝杩欐槸娌℃湁璁剧疆鏃跺€欓粯璁ょ殑
    public static final String REVERSE_DIFFERENCE = "REVERSE_DIFFERENCE";
    // 鏄彧鏄剧ずB鐨勫舰鐘?    public static final String REPLACE = "REPLACE";

    private Path mHeartPath;
    private Path mCirclePath;

    private RectF mHeartRect;

    private int mHeartAlphaColor;
    private int mCircleAlphaColor;
    private int mBackgroundColor;
    private int mTextColor;

    private int mTextSize;

    private Paint mPaint;

    private String mType;

    public ClipOpView(Context context) {
        this(context, null);
    }

    public ClipOpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipOpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setType(String type) {
        this.mType = type;
        postInvalidate();
    }

    @Override
    protected void init(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mHeartAlphaColor = Color.parseColor("#aabe97dc");
        mCircleAlphaColor = Color.parseColor("#aa226089");
        mBackgroundColor = Color.parseColor("#df0e62");
        mTextColor = Color.parseColor("#f54291");

        mTextSize = dpToPx(14);

        mHeartPath = new Path();
        createHeart(mHeartPath);

        mHeartRect = new RectF();
        mHeartPath.computeBounds(mHeartRect, true);

        mCirclePath = new Path();
        mCirclePath.addCircle(0,
                mHeartRect.bottom,
                mHeartRect.width() / 2,
                Path.Direction.CCW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvasText(canvas);

        drawSourcePic(canvas);

        drawClip(canvas);
    }

    private void canvasText(Canvas canvas) {
        canvas.save();

        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mType, getWidth() / 2, mTextSize * 2, mPaint);

        canvas.restore();
    }

    private void drawSourcePic(Canvas canvas) {
        canvas.save();

        mPaint.setColor(mHeartAlphaColor);
        canvas.translate(mHeartRect.width() / 2 + 50,
                mHeartRect.height() / 2 + 50);
        canvas.drawPath(mHeartPath, mPaint);

        mPaint.setColor(mCircleAlphaColor);
        canvas.translate(mHeartRect.width() + 50, -mHeartRect.bottom + 50);
        canvas.drawPath(mCirclePath, mPaint);

        canvas.restore();
    }

    private void drawClip(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);

        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(mHeartAlphaColor);
        canvas.drawPath(mHeartPath, mPaint);

        mPaint.setColor(mCircleAlphaColor);
        canvas.drawPath(mCirclePath, mPaint);

        canvas.clipPath(mHeartPath);

        Region.Op op;
        switch (mType) {
            case DIFFERENCE:
                op = Region.Op.DIFFERENCE;
                break;
            // 鏄疉鍜孊浜ら泦鐨勫舰鐘?            case INTERSECT:
                op = Region.Op.INTERSECT;
                break;
            // 鏄疉鍜孊鐨勫叏闆?            case UNION:
                op = Region.Op.UNION;
                break;
            // 鏄叏闆嗗舰鐘跺噺鍘讳氦闆嗗舰鐘朵箣鍚庣殑閮ㄥ垎
            case XOR:
                op = Region.Op.XOR;
                break;
            // 鏄疊褰㈢姸涓笉鍚屼簬A鐨勯儴鍒嗘樉绀哄嚭鏉ワ紝杩欐槸娌℃湁璁剧疆鏃跺€欓粯璁ょ殑
            case REVERSE_DIFFERENCE:
                op = Region.Op.REVERSE_DIFFERENCE;
                break;
            // 鏄彧鏄剧ずB鐨勫舰鐘?            case REPLACE:
                op = Region.Op.REPLACE;
                break;
            default:
                op = Region.Op.INTERSECT;
                break;
        }
        canvas.clipPath(mCirclePath, op);
        canvas.drawColor(mBackgroundColor);
    }

    /**
     * 鏋勫缓蹇冨舰
     */
    private void createHeart(Path path) {
        List<PointF> pointList = new ArrayList<>();
        pointList.add(new PointF(0, -76));
        pointList.add(new PointF(100, -206));
        pointList.add(new PointF(224, -122));
        pointList.add(new PointF(224, -24));
        pointList.add(new PointF(224, 74));
        pointList.add(new PointF(102, 180));
        pointList.add(new PointF(0, 258));
        pointList.add(new PointF(-102, 180));
        pointList.add(new PointF(-224, 74));
        pointList.add(new PointF(-224, -24));
        pointList.add(new PointF(-224, -122));
        pointList.add(new PointF(-100, -206));

        path.reset();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                path.moveTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            } else {
                path.lineTo(pointList.get(i * 3).x, pointList.get(i * 3).y);
            }

            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }

            path.cubicTo(pointList.get(i * 3 + 1).x, pointList.get(i * 3 + 1).y,
                    pointList.get(i * 3 + 2).x, pointList.get(i * 3 + 2).y,
                    pointList.get(endPointIndex).x, pointList.get(endPointIndex).y);
        }
        path.close();
    }

}

