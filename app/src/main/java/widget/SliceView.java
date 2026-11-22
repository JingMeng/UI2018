//package widget;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.Point;
//import android.graphics.PointF;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.zinc.ui2018.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Jiang zinc
// * @date 鍒涘缓鏃堕棿锛?018/11/22
// * @description 鍒囨崲鎸夐挳锛屼簰宓?// */
//public class SliceView extends View {
//
//    private final static String TAG = SliceView.class.getSimpleName();
//    private final static int LEFT = 1;
//    private final static int RIGHT = 2;
//    private final static int NONE = 0;
//
//    // 鏂滈潰楂樺害
//    private final static double ANGLE = 2.67;
//    // 绾垮
//    private float mLineWidth;
//    // 瀛椾綋澶у皬
//    private float mTextSize;
//    // 闂撮殧
//    private int mSpace;
//    // 鏄惁宸茬粡鍒濆鍖栦簡
//    private boolean mIsInit = false;
//
//    // 鍙粯鍒堕儴鍒嗙殑 楂樸€佸
//    private float mHeight;
//    private float mWidth;
//
//    // 绐佸嚭閮ㄥ垎鐨勯暱搴?//    private float mSharpWidth;
//
//    private float mShortLine;
//    private float mLongLine;
//
//    // 鍙宠竟鐨勬甯搁鑹?//    private int mRightColor;
//    // 宸﹁竟鐨勬甯搁鑹?//    private int mLeftColor;
//    // 鏈閫変腑棰滆壊
//    private int mUnSelectColor;
//
//    private Paint mRightPaint;
//    private Paint mLeftPaint;
//    private Paint mRightTextPaint;
//    private Paint mLeftTextPaint;
//
//    private Path mRightPath;
//    private Path mLeftPath;
//
//    // 鍙宠竟鏂囧瓧
//    private String mRightText;
//    // 宸﹁竟鏂囧瓧
//    private String mLeftText;
//
//    private List<PointF> mLeftPoint = new ArrayList<>();
//    private List<PointF> mRightPoint = new ArrayList<>();
//
//    // 榛樿閫夋嫨宸﹁竟
//    private boolean isSelectLeft = true;
//
//    // 瑙︾鍒扮殑鏄摢涓€杈癸紝鐢ㄤ簬鎶捣鏃跺垽璇绘槸鍚︿负閫変腑
//    private int mTouchIndex = 0;
//
//    private ClickListener mListener;
//
//    public SliceView(Context context) {
//        this(context, null, 0);
//    }
//
//    public SliceView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SliceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    private void init(Context context) {
//
//        mSpace = dip2px(context, 15);
//        mLineWidth = dip2px(context, 1);
//        mTextSize = dip2px(context, 14);
//
//        mLeftColor = ContextCompat.getColor(context, R.color.color_slice_right);
//        mRightColor = ContextCompat.getColor(context, R.color.color_slice_left);
//        mUnSelectColor = ContextCompat.getColor(context, R.color.color_slice_unselect);
//
//        mLeftPaint = new Paint();
//        mLeftPaint.setAntiAlias(true);
//        mLeftPaint.setColor(mLeftColor);
//        mLeftPaint.setStrokeWidth(mLineWidth);
//
//        mRightPaint = new Paint();
//        mRightPaint.setAntiAlias(true);
//        mRightPaint.setColor(mRightColor);
//        mRightPaint.setStrokeWidth(mLineWidth);
//
//        mLeftTextPaint = new Paint();
//        mLeftTextPaint.setAntiAlias(true);
//        mLeftTextPaint.setTextAlign(Paint.Align.CENTER);
//        mLeftTextPaint.setTextSize(mTextSize);
//
//        mRightTextPaint = new Paint();
//        mRightTextPaint.setAntiAlias(true);
//        mRightTextPaint.setTextAlign(Paint.Align.CENTER);
//        mRightTextPaint.setTextSize(mTextSize);
//
//        mLeftPath = new Path();
//        mRightPath = new Path();
//
//        mLeftText = "澧炲姞淇濊瘉閲?;
//        mRightText = "鍑忓皯淇濊瘉閲?;
//
//    }
//
//    public void setListener(ClickListener listener) {
//        this.mListener = listener;
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if (!mIsInit) {
//
//            mIsInit = true;
//            int height = getMeasuredHeight();
//            int width = getMeasuredWidth();
//
//            mHeight = height - getPaddingTop() - getPaddingBottom();
//            mWidth = width - getPaddingLeft() - getPaddingRight();
//
//            mSharpWidth = (float) (mHeight / ANGLE);
//
//            mShortLine = (mWidth - mSharpWidth - mSpace) / 2;
//            mLongLine = mShortLine + mSharpWidth;
//
//            buildBtnPath();
//        }
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        if (isSelectLeft) {
//            mLeftPaint.setStyle(Paint.Style.FILL);
//            mRightPaint.setStyle(Paint.Style.STROKE);
//
//            mLeftPaint.setColor(mLeftColor);
//            mRightPaint.setColor(mUnSelectColor);
//
//            mLeftTextPaint.setColor(Color.WHITE);
//            mRightTextPaint.setColor(mUnSelectColor);
//
//        } else {
//            mLeftPaint.setStyle(Paint.Style.STROKE);
//            mRightPaint.setStyle(Paint.Style.FILL);
//
//            mLeftPaint.setColor(mUnSelectColor);
//            mRightPaint.setColor(mRightColor);
//
//            mLeftTextPaint.setColor(mUnSelectColor);
//            mRightTextPaint.setColor(Color.WHITE);
//        }
//
//        canvas.drawPath(mLeftPath, mLeftPaint);
//        canvas.drawPath(mRightPath, mRightPaint);
//        canvas.drawText(mLeftText,
//                mShortLine / 2 + mSharpWidth / 2,
//                mHeight / 2 + mTextSize / 4,
//                mLeftTextPaint);
//        canvas.drawText(mRightText,
//                mWidth - mShortLine / 2 - mSharpWidth / 2,
//                mHeight / 2 + mTextSize / 4,
//                mRightTextPaint);
//
//    }
//
//    /**
//     * 鏋勫缓鎸夐挳璺緞
//     */
//    private void buildBtnPath() {
//
//        mLeftPoint.add(new PointF(mLineWidth / 2f, mLineWidth / 2));
//        mLeftPoint.add(new PointF(mShortLine, mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLongLine, mHeight - mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLineWidth / 2, mHeight - mLineWidth / 2));
//        mLeftPoint.add(new PointF(mLineWidth / 2f, mLineWidth / 2));
//        buildPathFromList(mLeftPoint, mLeftPath);
//
//        mRightPoint.add(new PointF(mShortLine + mSpace, mLineWidth / 2));
//        mRightPoint.add(new PointF(mWidth - mLineWidth / 2, mLineWidth / 2));
//        mRightPoint.add(new PointF(mWidth - mLineWidth / 2, mHeight - mLineWidth / 2));
//        mRightPoint.add(new PointF(mLongLine + mSpace + mLineWidth / 2, mHeight - mLineWidth / 2));
//        mRightPoint.add(new PointF(mShortLine + mSpace, mLineWidth / 2));
//        buildPathFromList(mRightPoint, mRightPath);
//
//    }
//
//    private void buildPathFromList(List<PointF> pointList, Path path) {
//        for (int i = 0; i < pointList.size(); ++i) {
//            if (i == 0) {
//                path.moveTo(pointList.get(i).x, pointList.get(i).y);
//            } else {
//                path.lineTo(pointList.get(i).x, pointList.get(i).y);
//            }
//        }
//    }
//
//    private int dip2px(Context context, float dipValue) {
//        float density = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * density + 0.5f);
//    }
//
//    private PointF mTouchPoint = new PointF();
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
////        Log.i(TAG, "onTouchEvent: [x:" + event.getX() + "; y:" + event.getY() + "]");
////        Log.i(TAG, "onTouchEvent: " + event.getAction());
//
//        mTouchPoint.x = event.getX();
//        mTouchPoint.y = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
////                Log.i(TAG, "onTouchEvent: ACTION_DOWN");
//                if (contains(mLeftPoint, mTouchPoint)) {
//                    mTouchIndex = LEFT;
//                } else if (contains(mRightPoint, mTouchPoint)) {
//                    mTouchIndex = RIGHT;
//                } else {
//                    mTouchIndex = NONE;
//                }
//
//                break;
//            case MotionEvent.ACTION_MOVE:
////                Log.i(TAG, "onTouchEvent: ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                int upIndex = NONE;
//                if (contains(mLeftPoint, mTouchPoint)) {
//                    upIndex = LEFT;
//                } else if (contains(mRightPoint, mTouchPoint)) {
//                    upIndex = RIGHT;
//                }
//
//                // 鎶捣鐐逛笉鍐嶈寖鍥村唴
//                if(upIndex == NONE){
//                    break;
//                }
//
//                // 鎸変笅鍜屾姮璧疯鍦ㄥ悓涓€涓帶浠讹紝鎵嶅彲浠?//                if (upIndex != mTouchIndex) {
//                    break;
//                }
//
//                boolean result = upIndex == LEFT;
//
//                // 濡傛灉鍜屼箣鍓嶄竴鏍风殑璇濓紝涓嶅洖璋?//                if(result == isSelectLeft){
//                    break;
//                }
//
//                isSelectLeft = result;
//
//                if (mListener != null) {
//                    mListener.onClick(upIndex);
//                }
//
////                Log.i(TAG, "onTouchEvent: ACTION_UP");
//                invalidate();
//                mTouchIndex = NONE;
//                break;
//        }
//
//        return true;
//    }
//
//    /**
//     * Return true if the given point is contained inside the boundary.
//     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
//     *
//     * @param touchPoint The point to check
//     * @return true if the point is inside the boundary, false otherwise
//     */
//    public boolean contains(List<PointF> pointList, PointF touchPoint) {
//        int i;
//        int j;
//        boolean result = false;
//        for (i = 0, j = pointList.size() - 1; i < pointList.size(); j = i++) {
//            if ((pointList.get(i).y > touchPoint.y) != (pointList.get(j).y > touchPoint.y) &&
//                    (touchPoint.x < (pointList.get(j).x - pointList.get(i).x) * (touchPoint.y - pointList.get(i).y) / (pointList.get(j).y - pointList.get(i).y) + pointList.get(i).x)) {
//                result = !result;
//            }
//        }
//        return result;
//    }
//
//    interface ClickListener {
//        void onClick(int index);
//    }
//
//}

