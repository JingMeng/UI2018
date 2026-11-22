package com.zinc.animation.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/15
 * @description 闆疯揪鍥? */
public class RadarChartView extends View {

    private static final String TAG = "RadarChartView";

    // 榛樿6缁村害
    private static final int DEFAULT_DIMEN_COUNT = 6;
    // 360搴?    private static final double CIRCLE_ANGLE = 360d;
    // 缃戞牸绾跨殑榛樿棰滆壊
    private static final String DEFAULT_LINE_COLOR = "#7a7a7a";
    // 榛樿涓績鐐?    private static final PointF DEFAULT_CENTER_POINT = new PointF(0, 0);
    // 闆疯揪鍥?鑳屾櫙娓愬彉 鍒嗗壊鐨勫眰绾?    private static final int RADAR_BG_LEVEL = 10;
    // 闆疯揪鍥?鑳屾櫙娓愬彉 鏄剧ず鐨勫眰绾?    private static final int RADAR_BG_SHOW_LEVEL = 6;
    // 鑳屾櫙 闃舵 閫忔槑
    private static final int BG_ALPHA_LEVEL = 10;
    // 涓€涓含搴︾殑鍔ㄧ敾鎸佺画鏃堕棿
    private static final int DURATION = 200;

    // 鍒濆鍖栫姸鎬?    private static final int INIT = 0x001;
    // 杩愯鐘舵€?    private static final int RUNNING = 0x002;

    // 杈规瀹藉害
    private float RADAR_BORDER_LINE_WIDTH = dpToPx(0.5f);
    // 缁村害鍒嗗壊绾垮搴?    private float RADAR_DIMEN_LINE_WIDTH = dpToPx(0.5f);
    // 鏁版嵁绾垮搴?    private float DATA_LINE_WIDTH = dpToPx(1.5f);
    // 灏忕偣鐨勫崐寰?    private float DOT_RADIUS = dpToPx(1.5f);
    // 瀛椾綋澶у皬
    private float TEXT_SIZE = spToPx(10f);

    // 绾害鏁?    private int mDimenCount;
    // 姣忎釜绾害鐨勭嚎闀?    private float mLength;
    // 姣忎釜绾害瑙掑害
    private double mAngle;

    // 闆疯揪鍥?椤剁偣鍧愭爣闆?    private List<PointF> mVertexList;

    // 闆疯揪鍥?绾跨殑鐢荤瑪
    private Paint mLinePaint;
    // 闆疯揪鍥?鑳屾櫙鐢荤瑪
    private Paint mRadarBgPaint;
    // 缁樺埗鏁版嵁鐨勭敾绗?    private Paint mDataPaint;
    // 缁樺埗鏂囧瓧鐨勭敾绗?    private Paint mTextPaint;

    // 闆疯揪鍥?杈规璺緞
    private Path mRadarLinePath;
    // 闆疯揪鍥?缁村害鍒嗗壊绾?    private Path mDimensionPath;
    // 闆疯揪鍥?鑳屾櫙璺緞
    private Path mRadarBgPath;
    // 闆疯揪鍥?姝ｅ湪杩愬姩鐨勮矾寰?    private Path mRunningPath;

    // 闆疯揪鍥剧殑涓績鐐?    private PointF mRadarCenterPoint;

    // 闆疯揪鍥炬暟鎹?    private List<Data> mDataList;
    // 闆疯揪鍥惧熀纭€鏁版嵁
    private List<Data> mBaseDataList;

    // 鏂囧瓧鎻忚堪
    private List<String> mTextDataList;

    // 鎻掑€煎櫒
    private ValueAnimator mAnimator;
    // 褰撳墠鐨勬彃鍊煎櫒鍊?    private float mAnimCurValue;
    // 鎬诲叡闇€瑕佺殑寰幆鏁?    private int mTotalLoopCount;
    // 褰撳墠闇€瑕佺殑寰幆鏁?    private int mCurLoopCount;

    // 褰撳墠鍔ㄧ敾鐘舵€?    private int mCurState;

    protected float mWidth;
    protected float mHeight;

    public RadarChartView(Context context) {
        this(context, null, 0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mLength = Math.min(w, h) / 4;

        calculateRadarChartVertex();
        initRadarLine();
    }

    protected void init(Context context) {

        mCurState = INIT;

        mVertexList = new ArrayList<>();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));
        // 杩炵嚎鍦嗚
        mLinePaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mRadarBgPaint = new Paint();
        mRadarBgPaint.setAntiAlias(true);
        mRadarBgPaint.setStyle(Paint.Style.FILL);
        mRadarBgPaint.setPathEffect(new CornerPathEffect(dpToPx(2.5f)));

        mDataPaint = new Paint();
        mDataPaint.setAntiAlias(true);
        mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
        mDataPaint.setPathEffect(new CornerPathEffect(dpToPx(1f)));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TEXT_SIZE);
        mTextPaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));

        mRadarLinePath = new Path();

        mDimensionPath = new Path();

        mRadarBgPath = new Path();

        mRunningPath = new Path();

        mDataList = new ArrayList<>();
        mBaseDataList = new ArrayList<>();
        mTextDataList = new ArrayList<>();

        mRadarCenterPoint = DEFAULT_CENTER_POINT;

        mDimenCount = DEFAULT_DIMEN_COUNT;
        mAngle = CIRCLE_ANGLE / mDimenCount;

    }

    /**
     * 璁剧疆鏂囧瓧鎻忚堪鏁版嵁
     *
     * @param textDataList
     */
    public void setTextDataList(List<String> textDataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mTextDataList.clear();
        mTextDataList.addAll(textDataList);
    }

    /**
     * 璁剧疆缁村害
     *
     * @param dimenCount
     */
    public void setDimenCount(int dimenCount) {

        if (dimenCount < 3) {
            Log.w(TAG, "Dimension is must be bigger than two.");
            dimenCount = 3;
        }

        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mBaseDataList.clear();
        mDataList.clear();

        // 璁剧疆缁村害锛屽苟閲嶆柊璁＄畻姣忎釜缁村害瑙掑害
        mDimenCount = dimenCount;
        mAngle = CIRCLE_ANGLE / mDimenCount;

        calculateRadarChartVertex();
        initRadarLine();
        invalidate();
    }

    /**
     * 璁剧疆鏁版嵁
     *
     * @param dataList
     */
    public void setDataList(List<Data> dataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }

    /**
     * 璁剧疆鍩烘湰鏁版嵁
     *
     * @param baseDataList
     */
    public void setBaseDataList(List<Data> baseDataList) {
        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        this.mBaseDataList.clear();
        this.mBaseDataList.addAll(baseDataList);
    }

    /**
     * 妫€娴嬫暟鎹腑鐨?{@link Data#data} 闀垮害鏄惁鍜?{@link #mDimenCount} 鐩稿悓
     *
     * @param dataList 鏁版嵁
     */
    private void checkData(List<Data> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getData().size() != mDimenCount) {
                throw new RuntimeException("The Data size is not equal to dimension count.");
            }
        }
    }

    /**
     * 寮€濮嬬粯鍒?     */
    public void start() {

        if (mCurState != INIT) {
            Log.w(TAG, "Cancel or stop the animation first.");
            return;
        }

        mCurState = RUNNING;

        // 妫€娴嬪熀绾挎暟鎹?        checkData(mBaseDataList);
        // 妫€娴嬫暟鎹?        checkData(mDataList);
        // 妫€娴嬫枃瀛楁弿杩版暟鎹?        if (mTextDataList.size() != 0 && mTextDataList.size() != mDimenCount) {
            throw new RuntimeException("Text data length should be zero or equal with dimension count.");
        }

        // 灏嗗綋鍓嶅惊鐜暟缃负 0
        mCurLoopCount = 0;

        // 璁＄畻鏁版嵁
        calculateDataVertex(true);
        calculateDataVertex(false);

        /**
         * 绗竴涓淮搴︿笉闇€瑕佸睍寮€锛屾墍浠ョ淮搴︽暟闇€瑕?1
         * 杩欓噷涓嶄娇鐢?setRepeatMode 璁剧疆澶氭寰幆,
         * 鏄洜涓?{@link AnimatorListenerAdapter#onAnimationRepeat(Animator)} 鍜?         * {@link android.animation.ValueAnimator.AnimatorUpdateListener#onAnimationUpdate(ValueAnimator)}
         * 鏃犳硶纭繚鍏堕『搴忥紝鏈夋椂浼氬嚭鐜颁贡鍊肩幇璞★紝杩欑鐜拌薄鐩墠鏈夋鐜囧嚭鐜板湪 mate10锛?.1.0锛夋墜鏈轰笂锛屾墍浠ヤ娇鐢ㄨ繖绉嶆柟娉曡繘琛岃閬?         **/
        mTotalLoopCount = (mDimenCount - 1) * mDataList.size();
        mAnimator = ValueAnimator.ofFloat(0f, mTotalLoopCount);
        mAnimator.setDuration(DURATION * mTotalLoopCount);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();

                // 鏁存暟閮ㄥ垎鍗充负褰撳墠鐨勫姩鐢绘暟鎹笅鏍?                mCurLoopCount = (int) value;

                // 灏忔暟閮ㄥ垎鏋佷负褰撳墠缁村害姝ｅ湪灞曞紑鐨勮繘搴︾櫨鍒嗘瘮
                mAnimCurValue = value - mCurLoopCount;

                invalidate();
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 鍔ㄧ敾缁撴潫锛屽皢鐘舵€佺疆涓哄垵濮嬬姸鎬侊紝骞跺啀鍒锋柊涓€娆★紝璁╂渶鍚庣殑鏁版嵁鍏ㄩ儴鏄剧ず
                mCurState = INIT;
                invalidate();
            }
        });

        // 寮€鍚姩鐢?        mAnimator.start();
    }

    /**
     * 鍋滄鍔ㄧ敾
     */
    public void stop() {
        if (mAnimator == null) {
            return;
        }
        mAnimator.cancel();
    }

    /**
     * 娓呯┖鏁版嵁
     */
    public void reset() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mBaseDataList.clear();
        mDataList.clear();
        invalidate();
    }

    /**
     * 璁＄畻鏁版嵁鐨勯《鐐瑰潗鏍?     *
     * @param isBase 鏄惁涓?鍩虹鏁版嵁
     */
    private void calculateDataVertex(boolean isBase) {

        List<Data> calDataList = isBase ? mBaseDataList : mDataList;

        for (int i = 0; i < calDataList.size(); ++i) {

            Data data = calDataList.get(i);

            // 鑾峰彇 姣斾緥鏁版嵁
            List<Float> pointDataList = data.getData();

            // 璁剧疆璺緞
            Path curPath = new Path();
            data.setPath(curPath);

            curPath.reset();
            for (int j = 0; j < pointDataList.size(); ++j) {

                // 褰撳墠缁村害鐨勬暟鎹瘮渚?                float ratio = pointDataList.get(j);
                // 褰撳墠缁村害鐨勯《鐐瑰潗鏍?                PointF curDimenPoint = mVertexList.get(j);

                if (j == 0) {
                    curPath.moveTo(curDimenPoint.x * ratio,
                            curDimenPoint.y * ratio);
                } else {
                    curPath.lineTo(curDimenPoint.x * ratio,
                            curDimenPoint.y * ratio);
                }

            }
            curPath.close();

        }

    }

    /**
     * 璁＄畻闆疯揪鍥剧殑椤剁偣锛岃繖閲屽彧鏄绠楋紝娌℃湁杩涜璺緞鎷煎噾
     * {@link #initRadarLine()}杩涜鎷煎噾璺緞
     */
    private void calculateRadarChartVertex() {

        // 娓呴櫎涔嬪墠椤剁偣
        mVertexList.clear();

        // 寰幆閬嶅巻璁＄畻椤剁偣鍧愭爣
        for (int i = 0; i < mDimenCount; ++i) {

            PointF point = new PointF();

            // 褰撳墠瑙掑害
            double curAngle = i * mAngle;
            // 杞姬搴﹀埗
            double radian = Math.toRadians(curAngle);

            // 璁＄畻鍏?x銆亂 鐨勫潗鏍?            // y杞撮渶瑕佽繘琛屽彇鍙嶏紝鍥犱负canvas鐨勫潗鏍囪酱鍜屾垜浠暟瀛︿腑鐨勫潗鏍囪酱鐨剏杞存濂芥槸涓婁笅鐩稿弽鐨?            point.x = (float) (mLength * Math.sin(radian));
            point.y = (float) -(mLength * Math.cos(radian));

            mVertexList.add(point);
        }

    }

    /**
     * 鍒濆鍖?闆疯揪鍥?澶栬竟妗嗗拰缁村害鍒嗗壊绾?     */
    protected void initRadarLine() {
        // 鍏堟竻绌?        mRadarLinePath.reset();
        mDimensionPath.reset();

        // 鐢?澶栬竟妗?        for (int i = 0; i < mVertexList.size(); ++i) {
            if (i == 0) {
                mRadarLinePath.moveTo(mVertexList.get(i).x, mVertexList.get(i).y);
            } else {
                mRadarLinePath.lineTo(mVertexList.get(i).x, mVertexList.get(i).y);
            }
        }
        mRadarLinePath.close();

        // 缁村害鍒嗗壊绾?        for (int i = 0; i < mVertexList.size(); ++i) {
            mDimensionPath.moveTo(mVertexList.get(i).x, mVertexList.get(i).y);
            mDimensionPath.lineTo(mRadarCenterPoint.x, mRadarCenterPoint.y);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 骞崇Щ鐢诲竷鑷充腑蹇?        canvas.translate(mWidth / 2, mHeight / 2);

        // 鐢婚浄杈?妗嗗拰缁村害绾?        drawRadarLine(canvas);
        // 鐢婚浄杈?鑳屾櫙
        drawRadarBackground(canvas);
        // 鐢?椤剁偣鐨勫皬鐐?        drawDot(canvas);
        // 鐢绘枃瀛?        drawText(canvas);

        // 鐢诲熀绾挎暟鎹?        drawData(canvas, true);

        if (mCurState == INIT) {
            drawData(canvas, false);
        } else {
            drawRunningData(canvas);
        }

    }

    /**
     * 鐢绘枃瀛?     *
     * @param canvas 鐢诲竷
     */
    protected void drawText(Canvas canvas) {

        if (mTextDataList == null || mTextDataList.size() != mDimenCount) {
            Log.w(TAG, "The length of description text list is not equal with dimension.");
            return;
        }

        for (int i = 0; i < mDimenCount; ++i) {

            PointF vertexPoint = mVertexList.get(i);

            // 鎵€鍦ㄨ薄闄?            int dimension = checkThePointDimension(vertexPoint);

            Paint.Align align;
            float y = vertexPoint.y * 1.15f;
            switch (dimension) {
                case 1:
                    align = Paint.Align.CENTER;
                    break;
                case 2:
                    align = Paint.Align.LEFT;
                    break;
                case 3:
                    align = Paint.Align.LEFT;
                    y -= ((mTextPaint.descent() + mTextPaint.ascent()) / 2);
                    break;
                case 4:
                    align = Paint.Align.LEFT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 5:
                    align = Paint.Align.CENTER;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 6:
                    align = Paint.Align.RIGHT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent());
                    break;
                case 7:
                    align = Paint.Align.RIGHT;
                    y -= (mTextPaint.descent() + mTextPaint.ascent() / 2);
                    break;
                case 8:
                    align = Paint.Align.RIGHT;
                    break;
                default:
                    align = Paint.Align.CENTER;
                    break;
            }
            mTextPaint.setTextSize(TEXT_SIZE);

            mTextPaint.setTextAlign(align);

            canvas.drawText(mTextDataList.get(i),
                    vertexPoint.x * 1.15f,
                    y,
                    mTextPaint);

        }

    }

    /**
     * 鐢?椤剁偣鐨勫皬鐐癸紝鏈夋弿杩版枃瀛楁椂锛屾墠缁樺埗灏忕偣
     *
     * @param canvas 鐢诲竷
     */
    protected void drawDot(Canvas canvas) {
        if (mTextDataList == null || mTextDataList.size() != mDimenCount) {
            Log.w(TAG, "The length of description text list is not equal with dimension.");
            return;
        }

        for (PointF point : mVertexList) {

            mLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(point.x * 1.08f, point.y * 1.08f, DOT_RADIUS, mLinePaint);

        }
    }

    /**
     * 缁樺埗杩愬姩涓殑鏁版嵁
     *
     * @param canvas 鐢诲竷
     */
    protected void drawRunningData(Canvas canvas) {

        // 鏁版嵁涓虹┖ 鍒欎笉缁樺埗
        if (mDataList.size() <= 0) {
            return;
        }

        // 褰撳墠鏁版嵁鐨勪笅鏍囷紙-1鍥犱负绗竴涓淮搴︿笉鐢ㄥ姩鐢伙級
        int curIndex = mCurLoopCount / (mDimenCount - 1);
        // 褰撳墠鏁版嵁鐨勭淮搴︼紙-1鍥犱负绗竴涓淮搴︿笉鐢ㄥ姩鐢伙級
        int curDimen = (mCurLoopCount % (mDimenCount - 1)) + 1;

        for (int i = 0; i <= curIndex; ++i) {

            Path path;

            // 褰撳墠瀵规瘮鐨勬暟鎹?            Data curData = mDataList.get(i);

            // 褰撳墠闇€瑕佽繘琛岃繍鍔ㄥ睍寮€鐨勫姣?            if (i == curIndex) {
                // 閲嶅埗杩愬姩涓殑璺緞
                mRunningPath.reset();

                // 绗竴缁村害 鐨?椤剁偣鏄浐瀹氱殑
                mRunningPath.moveTo(curData.getData().get(0) * mVertexList.get(0).x,
                        curData.getData().get(0) * mVertexList.get(0).y);

                // 缁樺埗 2-curDimen 缁村害
                for (int j = 1; j <= curDimen; ++j) {

                    // 褰撳墠缁村害鐨勫姣旀暟鎹?鎵€鍗犺缁村害鐨勬瘮渚?                    Float curDimenRatio = curData.getData().get(j);

                    // 褰撳墠缁村害鐨勯《鐐瑰潗鏍?                    PointF curDimenVertexPoint = mVertexList.get(j);

                    float x = curDimenVertexPoint.x * curDimenRatio;
                    float y = curDimenVertexPoint.y * curDimenRatio;

                    if (j == curDimen) {
                        // 缁樺埗姝ｅ湪绉诲姩鐨勭偣
                        mRunningPath.lineTo(x * mAnimCurValue, y * mAnimCurValue);
                    } else {
                        // 缁樺埗宸茬粡鍥哄畾鐨勭偣
                        mRunningPath.lineTo(x, y);
                    }
                }

                // 涓嶆槸鏈€鍚庣殑鐐瑰垯杩橀渶杩炴帴鍘熺偣
                if (curDimen != mDimenCount - 1) {
                    mRunningPath.lineTo(mRadarCenterPoint.x, mRadarCenterPoint.y);
                }

                mRunningPath.close();

                path = mRunningPath;

            } else {
                path = mDataList.get(i).getPath();
            }

            // 鐢昏疆寤?            mDataPaint.setStyle(Paint.Style.STROKE);
            mDataPaint.setColor(curData.getColor());
            mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
            canvas.drawPath(path, mDataPaint);

            // 鐢昏儗鏅?            mDataPaint.setStyle(Paint.Style.FILL);
            mDataPaint.setColor(getAlphaColor(curData.getColor(), 127));
            canvas.drawPath(path, mDataPaint);


        }

    }

    /**
     * 鐢昏儗鏅?     */
    protected void drawRadarBackground(Canvas canvas) {

        if (RADAR_BG_SHOW_LEVEL > RADAR_BG_LEVEL) {
            throw new RuntimeException("RADAR_BG_SHOW_LEVEL can not bigger than RADAR_BG_LEVEL.");
        }

        for (int i = 0; i < RADAR_BG_SHOW_LEVEL; ++i) {

            mRadarBgPath.reset();
            for (int j = 0; j < mDimenCount; ++j) {

                PointF curVertexPoint = mVertexList.get(j);

                float x = curVertexPoint.x * (RADAR_BG_LEVEL - i) / RADAR_BG_LEVEL;
                float y = curVertexPoint.y * (RADAR_BG_LEVEL - i) / RADAR_BG_LEVEL;

                if (j == 0) {
                    mRadarBgPath.moveTo(x, y);
                } else {
                    mRadarBgPath.lineTo(x, y);
                }

            }
            mRadarBgPath.close();

            mRadarBgPaint.setColor(getAlphaColor(Color.parseColor(DEFAULT_LINE_COLOR),
                    i * BG_ALPHA_LEVEL));

            canvas.drawPath(mRadarBgPath, mRadarBgPaint);

        }

    }

    /**
     * 缁樺埗闆疯揪鍥剧殑缃戞牸绾?     */
    protected void drawRadarLine(Canvas canvas) {

        // 缁樺埗闆疯揪鍥捐竟妗?        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(RADAR_BORDER_LINE_WIDTH);
        canvas.drawPath(mRadarLinePath, mLinePaint);

        // 缁樺埗闆疯揪鍥剧淮搴﹀垎鍓茬嚎
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(RADAR_DIMEN_LINE_WIDTH);
        canvas.drawPath(mDimensionPath, mLinePaint);

    }

    /**
     * 缁樺埗鍩虹嚎鏁版嵁
     *
     * @param canvas 鐢诲竷
     * @param isBase 鏄惁涓哄熀绾?     */
    protected void drawData(Canvas canvas, boolean isBase) {

        List<Data> dataList = isBase ? mBaseDataList : mDataList;

        for (int i = 0; i < dataList.size(); ++i) {
            Data data = dataList.get(i);

            int color = data.getColor();

            mDataPaint.setColor(color);
            mDataPaint.setStyle(Paint.Style.STROKE);
            if (isBase) {
                mDataPaint.setStrokeWidth(RADAR_BORDER_LINE_WIDTH);
            } else {
                mDataPaint.setStrokeWidth(DATA_LINE_WIDTH);
            }
            canvas.drawPath(data.getPath(), mDataPaint);

            if (!isBase) {
                mDataPaint.setStyle(Paint.Style.FILL);
                mDataPaint.setColor(getAlphaColor(color, 127));
                canvas.drawPath(data.getPath(), mDataPaint);
            }

        }

    }

    /**
     * 缁欓鑹插姞閫忔槑搴?     *
     * @param color 棰滆壊
     * @param alpha 閫忔槑
     * @return 鍔犱簡閫忔槑搴︾殑棰滆壊
     */
    protected int getAlphaColor(int color, int alpha) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        color = Color.HSVToColor(alpha, hsv);
        return color;
    }

    /**
     * 鑾峰彇鐐规墍鍦ㄧ殑璞￠檺
     *
     * <pre>
     *              鈹?     *              鈹?     *              鈹?     *      8       1       2
     *              鈹?     *              鈹?     * 鈹佲攣鈹佲攣鈹?鈹佲攣鈹佲攣鈹佲攣鈹佲晪鈹佲攣鈹佲攣鈹佲攣鈹?鈹佲攣鈹佲攣鈹佲攣鈻?x
     *              鈹?     *              鈹?     *      6       5       4
     *              鈹?     *              鈹?     *              鈹?     *              鈻?     *              y
     * </pre>
     *
     * @return
     */
    protected int checkThePointDimension(PointF pointF) {

        if (pointF == null) {
            return -1;
        }

        int x = (int) pointF.x;
        int y = (int) pointF.y;

        if (x == 0 && y < 0) {
            return 1;
        } else if (x > 0 && y < 0) {
            return 2;
        } else if (x > 0 && y == 0) {
            return 3;
        } else if (x > 0 && y > 0) {
            return 4;
        } else if (x == 0 && y > 0) {
            return 5;
        } else if (x < 0 && y > 0) {
            return 6;
        } else if (x < 0 && y == 0) {
            return 7;
        } else if (x < 0 && y < 0) {
            return 8;
        }
        return -1;
    }

    /**
     * 杞崲 sp 鑷?px
     *
     * @param spValue sp鍊?     * @return px鍊?     */
    protected int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 杞崲 dp 鑷?px
     *
     * @param dpValue dp鍊?     * @return px鍊?     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }

    /**
     * @author Jiang zinc
     * @date 鍒涘缓鏃堕棿锛?019/1/16
     * @description 闆疯揪鍥炬暟鎹?     */
    public static class Data {

        /**
         * 闆疯揪鍥剧殑鏁版嵁锛屾暟鎹寖鍥?[0-1]
         * 浣庝簬0锛屽鐞嗕负0
         * 澶т簬1锛屽鐞嗕负1
         * 鏁版嵁闀垮害锛岃鍜岄浄杈惧浘缁村害鐩稿悓
         */
        private List<Float> data;
        /**
         * 鏁版嵁鑹插€?         */
        private int color;

        private Path path;

        public Data(List<Float> data, int color) {
            this.data = data;
            this.color = color;
        }

        public List<Float> getData() {
            return data;
        }

        public int getColor() {
            return color;
        }

        private Path getPath() {
            return path;
        }

        private void setPath(Path path) {
            this.path = path;
        }
    }

}

