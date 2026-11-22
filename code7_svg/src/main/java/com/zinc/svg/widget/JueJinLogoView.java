package com.zinc.svg.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
 * author       : zinc
 * time         : 2019/4/5 涓嬪崍6:01
 * desc         :
 * version      :
 */
public class JueJinLogoView extends View {

    private InnerHandler mHandle;

    private float mSvgWidth;
    private float mSvgHeight;

    private float mSvgTranX;
    private float mSvgTranY;

    private Paint mPaint;

    private final List<PathData> mPathDataList = new ArrayList<>();

    private int mSvgRes;

    private PathMeasure mPathMeasure;

    /**
     * 鐢诲竷鐨勭煩闃?     */
    private final Matrix mCanvasMatrix = new Matrix();

    /**
     * svg 鐨?rect
     */
    private final RectF mSvgRect = new RectF();

    private final Path mAnimPath = new Path();

    private ValueAnimator mAnim = null;
    private float mAnimValue = 0;

    private float mLineWidth;

    public JueJinLogoView(Context context) {
        this(context, null, 0);
    }

    public JueJinLogoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JueJinLogoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        // 鍏抽棴纭欢鍔犻€燂紝鍚﹀垯浼氭ā绯?        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mSvgRes = R.raw.juejin;

        mHandle = new InnerHandler(context, this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPathMeasure = new PathMeasure();

        mLineWidth = UIUtils.dip2px(context, 0.5f);

        ParserSvgThread thread = new ParserSvgThread(context, mSvgRes);
        thread.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvasMatrix.reset();
        mAnimPath.reset();
        mAnimPath.lineTo(0, 0);

        float mScale = calculateScale(mSvgRect.width(), mSvgRect.height(), getWidth(), getHeight());

        // 绉昏嚦涓績
        mCanvasMatrix.preTranslate(getWidth() / 2, getHeight() / 2);
        mCanvasMatrix.preTranslate(-mSvgRect.width() / 2, -mSvgRect.height() / 2);

        mCanvasMatrix.preScale(
                mScale,
                mScale,
                mSvgRect.width() / 2,
                mSvgRect.height() / 2);

        canvas.setMatrix(mCanvasMatrix);

        int index = (int) mAnimValue;
        float process = mAnimValue - index;

        for (int i = 0; i < index; ++i) {
            PathData pathData = mPathDataList.get(i);
            mPaint.setColor(pathData.color);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(pathData.path, mPaint);
        }

        if (mAnimValue >= mPathDataList.size()) {
            return;
        }


        Log.i("onDraw", "mPathDataList: " + mPathDataList.size() +
                "; mAnimValue: " + mAnimValue +
                "; index: " + index +
                "; process: " + process);

        PathData pathData = mPathDataList.get(index);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(pathData.color);
        mPaint.setStrokeWidth(mLineWidth / mScale);

        mPathMeasure.setPath(pathData.path, false);
        mPathMeasure.getSegment(0,
                mPathMeasure.getLength() * process,
                mAnimPath,
                true);
        canvas.drawPath(mAnimPath, mPaint);


    }

    public void start() {
        if (mAnim != null) {
            mAnim.cancel();
        }

        if (mPathDataList.size() <= 0) {
            return;
        }

        mAnim = ValueAnimator.ofFloat(0, mPathDataList.size());
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mAnim.setDuration(10000);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.start();

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
    private class ParserSvgThread extends Thread {

        private static final String WIDTH = "android:width";
        private static final String HEIGHT = "android:height";
        private static final String VIEWPORT_WIDTH = "android:viewportWidth";
        private static final String VIEWPORT_HEIGHT = "android:viewportHeight";

        private static final String GROUP = "group";
        private static final String TRAN_X = "android:translateX";
        private static final String TRAN_Y = "android:translateY";

        private static final String PATH = "path";
        private static final String PATH_DATA = "android:pathData";
        private static final String FILL_COLOR = "android:fillColor";

        /**
         * 鍦板浘璧勬簮id
         */
        private final int mSvgResourceId;
        private final WeakReference<Context> mContext;

        ParserSvgThread(Context context, int svgResourceId) {
            this.mContext = new WeakReference<>(context);
            this.mSvgResourceId = svgResourceId;
        }

        @Override
        public void run() {
            // 鎵撳紑 svg 鐨勮緭鍏ユ祦
            InputStream inputStream = null;
            try {
                inputStream = getContext().getResources().openRawResource(mSvgResourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            // 鍦板浘杈撳叆娴佹墦寮€澶辫触
            if (inputStream == null) {
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
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
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 鑾峰彇鏍圭粨鐐?            Element root = document.getDocumentElement();

            // 瑁呰浇鎴戜滑瑙ｆ瀽鐨勬暟鎹?            List<PathData> pathDataList = new ArrayList<>();

            float viewportWidth = 0;
            float viewportHeight = 0;

            try {
                mSvgWidth = getRealSize(root.getAttribute(WIDTH));
                mSvgHeight = getRealSize(root.getAttribute(HEIGHT));
                viewportWidth = getRealSize(root.getAttribute(VIEWPORT_WIDTH));
                viewportHeight = getRealSize(root.getAttribute(VIEWPORT_HEIGHT));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (viewportWidth == 0 || viewportHeight == 0) {
                mHandle.sendEmptyMessage(InnerHandler.ERROR);
                return;
            }

            // 鑾峰彇 <group> 鑺傜偣
            NodeList groupNodeList = root.getElementsByTagName(GROUP);

            Element group = (Element) groupNodeList.item(0);
            String tranX = group.getAttribute(TRAN_X);
            if (TextUtils.isEmpty(tranX)) {
                mSvgTranX = 0;
            } else {
                float rate = mSvgWidth / viewportWidth;
                try {
                    float tx = Float.parseFloat(tranX);
                    mSvgTranX = rate * tx;
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }
            }

            String tranY = group.getAttribute(TRAN_Y);
            if (TextUtils.isEmpty(tranY)) {
                mSvgTranY = 0;
            } else {
                float rate = mSvgHeight / viewportHeight;
                try {
                    float ty = Float.parseFloat(tranY);
                    mSvgTranY = rate * ty;
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }
            }

            NodeList pathNodeList = group.getElementsByTagName(PATH);

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
                String pathData = pathNode.getAttribute(PATH_DATA);
                // path 鐨?棰滆壊
                String colorData = pathNode.getAttribute(FILL_COLOR);

                // 瑙ｆ瀽 path
                Path path = null;
                try {
                    path = PathParser.createPathFromPathData(pathData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // path 瑙ｆ瀽鍑洪敊锛岄€€鍑?                if (path == null) {
                    mHandle.sendEmptyMessage(InnerHandler.ERROR);
                    return;
                }

                int color = Color.parseColor(colorData);

                path.computeBounds(rect, true);

                left = left == -1 ? rect.left : Math.min(left, rect.left);
                right = right == -1 ? rect.right : Math.max(right, rect.right);
                top = top == -1 ? rect.top : Math.min(top, rect.top);
                bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);

                PathData item = new PathData();
                item.path = path;
                item.color = color;

                pathDataList.add(item);
            }

            mSvgRect.left = left;
            mSvgRect.top = top;
            mSvgRect.right = right;
            mSvgRect.bottom = bottom;

            mPathDataList.clear();
            mPathDataList.addAll(pathDataList);

            mHandle.sendEmptyMessage(InnerHandler.SUCCESS);
        }

        private float getRealSize(String sizeString) {

            if (sizeString.endsWith("px")) {
                return Float.parseFloat(
                        sizeString.substring(0, sizeString.length() - 2));
            } else if (sizeString.endsWith("dp")) {
                float dpSize = Float.parseFloat(sizeString.substring(0, sizeString.length() - 2));
                return UIUtils.dip2px(mContext.get(), dpSize);
            }

            try {
                return Float.parseFloat(sizeString);
            } catch (Exception e) {
                throw e;
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

    private static class PathData {
        Path path;
        int color;
    }

}

