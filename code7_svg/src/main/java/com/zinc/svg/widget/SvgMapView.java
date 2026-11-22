package com.zinc.svg.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.zinc.svg.PathParser;
import com.zinc.svg.R;
import com.zinc.lib_base.UIUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * author       : Jiang zinc
 * time         : 2019/3/28 涓婂崍11:23
 * email        : 56002982@qq.com
 * desc         : svg 鐨勫湴鍥?View
 * version      : 1.0.0
 */
public class SvgMapView extends View {

    /**
     * 榛樿棰滆壊
     */
    private static final int[] DEFAULT_COLOR = new int[]{
            R.color.t1_color1,
            R.color.t1_color2,
            R.color.t1_color3,
            R.color.t1_color4
    };

    private static final int ANIM_DURATION = 500;

    private static final int DEFAULT_SEL_COLOR = R.color.t1_sel_color;
    private static final int DEFAULT_OUTLINE_COLOR = R.color.t1_outline;
    private static final int DEFAULT_MAP_RESOURCE = R.raw.china;

    private InnerHandler mHandle;

    /**
     * 濉厖鍦板浘鐨勯鑹?     */
    private int[] mMapColor = DEFAULT_COLOR;

    /**
     * 閫変腑鐨勫尯鍩熼鑹?     */
    private int mSelColor;
    private int mOutlineColor;

    private Paint mPaint;
    private Paint mTextPaint;

    /**
     * 缂╂斁鍊嶆暟
     */
    private float mScale;

    /**
     * 鏄惁鍦ㄦ挱鏀?     */
    private boolean isPlaying;

    /**
     * 鐢ㄤ簬瀛樺偍宸茬粡瑙ｆ瀽瀹岀殑鏁版嵁
     */
    private final List<ItemData> mMapItemDataList = new ArrayList<>();

    /**
     * 鐢诲竷鐨勭煩闃?     */
    private final Matrix mCanvasMatrix = new Matrix();
    /**
     * 瑙︾鐐圭殑鐭╅樀
     */
    private final Matrix mTouchChangeMatrix = new Matrix();

    /**
     * 鐢ㄤ簬鍒ゆ柇鑾峰彇瑙︾鍖哄煙鐨剅ect
     */
    private final RectF mTouchRectF = new RectF();

    /**
     * 鎻忚堪鐨勬枃瀛?     */
    private final RectF mDesRect = new RectF();

    /**
     * 鐢ㄤ簬璁板綍瑙︾鍘荤殑鑼冨洿
     */
    private final Region mTouchRegion = new Region();

    /**
     * 瑙︾鐐癸紝绗竴涓负x杞达紝绗簩涓负y杞?     */
    private final float[] mTouchPoints = new float[]{0, 0};

    /**
     * 閫変腑鐨勫尯鍩?     */
    private ItemData mSelItem;

    /**
     * svg 鐨?rect
     */
    private final RectF mSvgRect = new RectF();

    /**
     * 褰撳墠鏄剧ず鐨勪富 rect
     */
    private final RectF mCurRect = new RectF();

    /**
     * 涓婃閫変腑鐨?rect
     */
    private final RectF mLastRectF = new RectF();

    /**
     * 鍔ㄧ敾涓殑缂╂斁姣斾緥
     */
    private float mAnimScale;

    private ValueAnimator mValueAnim;

    /**
     * 瑙ｆ瀽绾跨▼
     */
    private ParserMapThread mParserThread;

    /**
     * 鍦板浘璧勬簮
     */
    private int mMapResource;

    /**
     * 鎻忚堪妗嗙殑澶栬竟璺?     */
    private int mDesMargin;

    /**
     * 鎻忚堪鏂囧瓧鐨勫ぇ灏?     */
    private int mDesTextSize;

    /**
     * 鎻忚堪鏂囧瓧鐨勯鑹?     */
    private int mDesTextColor;

    /**
     * 鎻忚堪鏂囧瓧鑳屾櫙棰滆壊
     */
    private int mDesBgColor;

    /**
     * 鎻忚堪妗嗙殑鍦嗚鍗婂緞
     */
    private int mDesRoundRadius;

    /**
     * 鏂囧瓧閭ｈ竟璺?     */
    private int mDesPadding;

    public SvgMapView(Context context) {
        this(context, null, 0);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        // 鍏抽棴纭欢鍔犻€燂紝鍚﹀垯浼氭ā绯?        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        mSelColor = ContextCompat.getColor(context, DEFAULT_SEL_COLOR);
        mOutlineColor = ContextCompat.getColor(context, DEFAULT_OUTLINE_COLOR);
        mMapResource = DEFAULT_MAP_RESOURCE;

        mDesMargin = UIUtils.dip2px(context, 5);
        mDesPadding = UIUtils.dip2px(context, 5f);
        mDesRoundRadius = UIUtils.dip2px(context, 3.5f);

        mDesTextSize = UIUtils.dip2px(context, 12f);
        mDesTextColor = ContextCompat.getColor(context, R.color.des_color);
        mDesBgColor = Color.WHITE;
        mTextPaint.setTextSize(mDesTextSize);
        mTextPaint.setColor(mDesTextColor);

        // 鍒濆鍖栧姩鐢?        mValueAnim = ValueAnimator.ofFloat(0, 1);
        mValueAnim.setInterpolator(new LinearInterpolator());
        mValueAnim.setDuration(ANIM_DURATION);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }
        });

        initData();

    }

    private void initData() {
        mSvgRect.setEmpty();
        mCurRect.setEmpty();
        mLastRectF.setEmpty();
        mScale = 1.0f;
        mAnimScale = 1.0f;
        isPlaying = false;

        mMapItemDataList.clear();

        mCanvasMatrix.reset();
        mTouchChangeMatrix.reset();

        mTouchRectF.setEmpty();

        mSelItem = null;

        mParserThread = new ParserMapThread(mMapResource);
        mParserThread.start();
    }

    /**
     * 璁剧疆鍦板浘璧勬簮
     *
     * @param mapResource 鍦板浘璧勬簮
     */
    public void setMapResource(int mapResource) {
        if (mParserThread != null && mParserThread.isAlive()) {
            mParserThread.mIsCancel = true;
        }
        mMapResource = mapResource;
        initData();
    }

    /**
     * 璁剧疆鍦板浘棰滆壊
     *
     * @param mapColor 鍦板浘棰滆壊鏁扮粍
     */
    public void setMapColor(int[] mapColor) {
        this.mMapColor = mapColor;
    }

    /**
     * 璁剧疆閫変腑棰滆壊
     *
     * @param selColor 閫変腑鐨勯鑹茶祫婧?     */
    public void setSelColor(int selColor) {
        this.mSelColor = ContextCompat.getColor(getContext(), selColor);
    }

    /**
     * 璁剧疆鍕惧嫆棰滆壊
     *
     * @param outlineColor 鍕惧嫆棰滆壊
     */
    public void setOutlineColor(int outlineColor) {
        this.mOutlineColor = ContextCompat.getColor(getContext(), outlineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        // 閲嶇疆鐭╅樀
        mCanvasMatrix.reset();
        mTouchChangeMatrix.reset();

        handleLastState();
        handleCurState();

        // 灏嗙煩闃垫柦鍔犱簬鐢诲竷
        canvas.setMatrix(mCanvasMatrix);

        // 鐢诲湴鍥?        for (ItemData itemData : mMapItemDataList) {
            drawItem(canvas, itemData);
        }

        canvas.restore();

        drawDesTest(canvas);

    }

    /**
     * 鎻忚堪鏂囧瓧
     */
    private void drawDesTest(Canvas canvas) {

        if (mSelItem == null) {
            return;
        }
        canvas.save();

        float textLength = mTextPaint.measureText(mSelItem.title);

        mDesRect.left = 0;
        mDesRect.top = 0;
        mDesRect.right = textLength + mDesPadding * 4;
        mDesRect.bottom = mDesTextSize + mDesPadding * 2;

        canvas.translate(mDesMargin, mDesMargin);

        mPaint.setColor(mDesBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(mDesRect, mDesRoundRadius, mDesRoundRadius, mPaint);

        canvas.translate(0, mDesTextSize / 2);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mDesTextSize);
        mPaint.setColor(mDesTextColor);
        canvas.drawText(mSelItem.title,
                mDesRect.width() / 2,
                mDesRect.height() / 2,
                mPaint);

        canvas.restore();
    }

    /**
     * 澶勭悊褰撳墠鐘舵€?     */
    private void handleCurState() {
        if (mCurRect.equals(mLastRectF)) {
            return;
        }

        float curCenterX = mCurRect.left + mCurRect.width() / 2;
        float curCenterY = mCurRect.top + mCurRect.height() / 2;

        float lastCenterX = mLastRectF.left + mLastRectF.width() / 2;
        float lastCenterY = mLastRectF.top + mLastRectF.height() / 2;

        float dx = curCenterX - lastCenterX;
        float dy = curCenterY - lastCenterY;

        // 杩涜缂╂斁
        if (!mCurRect.isEmpty()) {

            mScale = calculateScale(mCurRect.width(), mCurRect.height(),
                    getWidth(), getHeight()) / mScale;

            if (mScale > 1) {
                mScale = (mScale - 1) * mAnimScale + 1;
            } else if (mScale < 1) {
                mScale = 1 - (1 - mScale) * mAnimScale;
            }
        }

        // 闇€瑕?澶氬亸绉诲尯鍩?涓?鏁村湴鍥?鐨勫鍖哄煙
        mCanvasMatrix.preTranslate(-dx * mAnimScale, -dy * mAnimScale);
        mCanvasMatrix.preScale(
                mScale,
                mScale,
                curCenterX,
                curCenterY);


        mTouchChangeMatrix.postTranslate(dx, dy);
        mTouchChangeMatrix.postScale(1 / mScale,
                1 / mScale,
                curCenterX,
                curCenterY);
    }

    /**
     * 澶勭悊涓婁竴娆＄殑鐘舵€?     */
    private void handleLastState() {
        // 绉昏嚦鐢诲竷涓績
        mCanvasMatrix.preTranslate(getWidth() / 2, getHeight() / 2);

        // 绉诲杈?        float lastLeftMargin = mLastRectF.left - mSvgRect.left;
        float lastTopMargin = mLastRectF.top - mSvgRect.top;
        mCanvasMatrix.preTranslate(-lastLeftMargin, -lastTopMargin);

        // 绉昏嚦涓績
        mCanvasMatrix.preTranslate(-mLastRectF.width() / 2, -mLastRectF.height() / 2);

        // 杩涜缂╂斁
        if (!mLastRectF.isEmpty()) {
            mScale = calculateScale(
                    mLastRectF.width(),
                    mLastRectF.height(),
                    getWidth(),
                    getHeight());
        }
        mCanvasMatrix.preScale(
                mScale,
                mScale,
                lastLeftMargin + mLastRectF.width() / 2,
                lastTopMargin + mLastRectF.height() / 2);

        mTouchChangeMatrix.postTranslate(-getWidth() / 2, -getHeight() / 2);
        mTouchChangeMatrix.postTranslate(lastLeftMargin, lastTopMargin);
        mTouchChangeMatrix.postTranslate(mLastRectF.width() / 2, mLastRectF.height() / 2);
        mTouchChangeMatrix.postScale(1 / mScale,
                1 / mScale,
                lastLeftMargin + mLastRectF.width() / 2,
                lastTopMargin + mLastRectF.height() / 2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 姝ｅ湪鎾斁鍒欎笉澶勭悊
        if (isPlaying) {
            return true;
        }

        // 澶勭悊鎷︽埅浜嬩欢
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            mTouchPoints[0] = event.getX();
            mTouchPoints[1] = event.getY();

            // 灏嗙偣鍧愭爣杩涜杞崲
            mTouchChangeMatrix.mapPoints(mTouchPoints);

            // 閲嶇疆閫夐」
            mSelItem = null;

            // 缃叆鍦板浘閫変腑鐘舵€?            for (ItemData item : mMapItemDataList) {
                if (isTouch(item, mTouchPoints[0], mTouchPoints[1])) {
                    mSelItem = item;
                    break;
                }
            }

            mLastRectF.set(mCurRect);

            // 濡傛灉褰撳墠宸茬粡鏄剧ず璇ュ尯鍩熷氨涓嶅啀鎵ц
            if (mTouchRectF.equals(mCurRect)) {
                mSelItem = null;
                startAnim(mSvgRect);
                return true;
            }

            //  閫変腑鐨勪负绌?            if (mSelItem == null) {
                startAnim(mSvgRect);
                return true;
            }

            startAnim(mTouchRectF);
        }

        return true;
    }

    /**
     * 寮€濮嬪姩鐢?     *
     * @param rectF 灏嗚鍒拌揪鐨?rect
     */
    private void startAnim(RectF rectF) {
        mCurRect.set(rectF);
        mValueAnim.start();
        isPlaying = true;
    }

    /**
     * 鏄惁鍦ㄨЕ纰扮殑鑼冨洿鍐?     *
     * @param item 鍦板浘鐨勬瘡涓暟鎹」
     * @param x    瑙︾鐐圭殑x杞?     * @param y    瑙︾鐐圭殑y杞?     * @return true锛氬湪鑼冨洿鍐咃紱false锛氬湪鑼冨洿澶?     */
    private boolean isTouch(ItemData item, float x, float y) {

        item.path.computeBounds(mTouchRectF, true);

        mTouchRegion.setPath(
                item.path,
                new Region((int) mTouchRectF.left,
                        (int) mTouchRectF.top,
                        (int) mTouchRectF.right,
                        (int) mTouchRectF.bottom)
        );

        return mTouchRegion.contains((int) x, (int) y);
    }

    /**
     * 鐢绘瘡涓尯鍩?     */
    private void drawItem(Canvas canvas, ItemData itemData) {

        if (itemData == mSelItem) {
            mPaint.setColor(mSelColor);
        } else {
            mPaint.setColor(itemData.color);
        }

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(itemData.path, mPaint);

        mPaint.setColor(mOutlineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(itemData.path, mPaint);

    }

    /**
     * 鐢ㄤ簬璁＄畻缂╂斁鐨勫ぇ灏?     */
    private float calculateScale(float fromWidth,
                                 float fromHeight,
                                 float toWidth,
                                 float toHeight) {
        float widthScale = toWidth / fromWidth;
        float heightScale = toHeight / fromHeight;

        return Math.min(widthScale, heightScale);
    }

    /**
     * 瑙ｆ瀽鍦板浘鐨勭嚎绋?     */
    private class ParserMapThread extends Thread {

        private static final String PATH = "path";
        private static final String DATA = "d";
        private static final String TITLE = "title";

        /**
         * 鍦板浘璧勬簮id
         */
        private final int mMapResourceId;

        /**
         * 鏄惁瑕佸彇娑?         */
        private boolean mIsCancel;

        ParserMapThread(int mapResourceId) {
            this.mMapResourceId = mapResourceId;
            this.mIsCancel = false;
        }

        @Override
        public void run() {
            // 鎵撳紑鍦板浘鐨勮緭鍏ユ祦
            InputStream inputStream = null;
            try {
                inputStream = getContext().getResources().openRawResource(mMapResourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            // 鍦板浘杈撳叆娴佹墦寮€澶辫触
            if (inputStream == null) {
                if (!mIsCancel) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                }
                return;
            }

            // 浠?XML鏂囨。 鐢熸垚 DOM瀵硅薄鏍?            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = null;
            try {
                document = factory.newDocumentBuilder().parse(inputStream);
            } catch (SAXException |
                    IOException |
                    ParserConfigurationException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 鍑洪敊鍒欏叧闂祦锛屽彂閫佹彁绀轰俊鎭?            if (document == null) {
                if (!mIsCancel) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                }
                return;
            }

            // 鑾峰彇鏍圭粨鐐?            Element root = document.getDocumentElement();

            // 瑁呰浇鎴戜滑瑙ｆ瀽鐨勬暟鎹?            List<ItemData> mapDataList = new ArrayList<>();

            // 鑾峰彇 <path/> 鑺傜偣
            NodeList pathNodeList = root.getElementsByTagName(PATH);

            int colorSize = mMapColor.length;

            // 鐢ㄤ簬璁板綍鏁翠釜 svg 鐨勫疄闄呭ぇ灏?            float left = -1;
            float top = -1;
            float right = -1;
            float bottom = -1;

            // 璁＄畻鍑?path 鐨?rect
            RectF rect = new RectF();

            // 閬嶅巻鎵€鏈夌殑 Path 鑺傜偣
            for (int i = 0; i < pathNodeList.getLength(); ++i) {
                Element pathNode = (Element) pathNodeList.item(i);
                // path 鐨?svg 璺緞
                String pathData = pathNode.getAttribute(DATA);
                // path 鐨?title
                String title = pathNode.getAttribute(TITLE);

                // 瑙ｆ瀽 path
                Path path = null;
                try {
                    path = PathParser.createPathFromPathData(pathData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // path 瑙ｆ瀽鍑洪敊锛岄€€鍑?                if (path == null) {
                    if (!mIsCancel) {
                        mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    }
                    return;
                }

                path.computeBounds(rect, true);

                left = left == -1 ? rect.left : Math.min(left, rect.left);
                right = right == -1 ? rect.right : Math.max(right, rect.right);
                top = top == -1 ? rect.top : Math.min(top, rect.top);
                bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                ItemData itemData = new ItemData(path,
                        ContextCompat.getColor(getContext(), mMapColor[i % colorSize]),
                        title);

                mapDataList.add(itemData);
            }

            mSvgRect.left = left;
            mSvgRect.top = top;
            mSvgRect.right = right;
            mSvgRect.bottom = bottom;

            // 瑙ｆ瀽瀹岋紝璧嬪€肩粰褰撳墠鏄剧ず鐨勪富 Rect
            mCurRect.set(mSvgRect);
            mLastRectF.set(mSvgRect);

            mMapItemDataList.clear();
            mMapItemDataList.addAll(mapDataList);
            if (!mIsCancel) {
                mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
            }
        }

    }

    /**
     * 鍐呴儴handler
     */
    private static class InnerHandler extends Handler {

        /**
         * 瑙ｆ瀽鎴愬姛
         */
        static final int SUCCESS = 1;
        /**
         * 瑙ｆ瀽澶辫触
         */
        static final int ERROR = 2;

        static final String ERROR_MSG = "鍦板浘璧勬簮鏈夎锛岃浇鍏ュけ璐?;

        private WeakReference<Context> mContext;
        private WeakReference<View> mView;

        InnerHandler(Context context, View view) {
            super(Looper.getMainLooper());
            mContext = new WeakReference<>(context);
            mView = new WeakReference<>(view);
        }

        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == SUCCESS) {

                if (mView.get() != null) {
                    mView.get().postInvalidate();
                }

            } else if (msg.what == ERROR) {

                if (mContext.get() != null) {
                    Toast.makeText(mContext.get(), ERROR_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /**
     * 鍦板浘鏁版嵁椤?     */
    private static class ItemData {

        // 璺緞鏁版嵁
        Path path;
        // 棰滆壊
        int color;
        // 鍚嶇О
        String title;

        ItemData(Path path, int color, String title) {
            this.path = path;
            this.color = color;
            this.title = title;
        }

    }

}

