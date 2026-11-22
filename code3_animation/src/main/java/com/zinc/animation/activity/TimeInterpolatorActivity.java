package com.zinc.animation.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;

import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.animation.R;
import com.zinc.animation.adapter.TimeInterpolatorAdapter;
import com.zinc.animation.bean.TimeInterpolatorBean;
import com.zinc.animation.interpolator. SpringInterpolator;
import com.zinc.animation.widget.TimeInterpolatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/24
 * @description
 */
public class TimeInterpolatorActivity extends AppCompatActivity implements TimeInterpolatorAdapter.ClickListener {
    // 鍙?000甯?    private static final int FRAME = 1000;

    private TimeInterpolatorView mTimeInterpolatorView;

    private final List<PointF> dataList = new ArrayList<>();
    private final List<TimeInterpolatorBean> interpolatorList = new ArrayList<>();

    private TimeInterpolator mInterpolator;

    private TextView tvRun;
    private View animView;
    private RecyclerView recycleView;
    private TextView tvDuration;
    private TextView tvStateInfo;

    private TimeInterpolatorAdapter mAdapter;

    private ObjectAnimator mAnimator;
    private ValueAnimator mXAnimator;

    private AnimatorSet animatorSet;

    private boolean isRunning;

    private PointF curPoint;

    private View getStartView() {
        return (View) findViewById(R.id.start_view);
    }

    private View getAnimView() {
        return (View) findViewById(R.id.anim_view);
    }

    private View getEndView() {
        return (View) findViewById(R.id.end_view);
    }

    private EditText getEtDuration() {
        return (EditText) findViewById(R.id.et_duration);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_interpolator);

        mTimeInterpolatorView = findViewById(R.id.time_interpolator_view);
        tvRun = findViewById(R.id.tv_run);
        animView = findViewById(R.id.anim_view);
        recycleView = findViewById(R.id.recycle_view);
        tvStateInfo = findViewById(R.id.tv_state_info);

        isRunning = false;

        curPoint = new PointF(0, 0);

        buildInterpolatorList();
        createData();

        final float start = dpToPx(this, 35);
        final float end = getScreenHeight(this) - dpToPx(this, 35 + 50) - getStatusHeight(this);

        mAnimator = ObjectAnimator.ofFloat(animView, "y", start, end);
        mXAnimator = ValueAnimator.ofFloat(0f, 1f);
        animatorSet = new AnimatorSet();
        animatorSet.play(mAnimator).with(mXAnimator);

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                updateState(false);
            }
        });

        mXAnimator.setInterpolator(new LinearInterpolator());
        mXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = animation.getAnimatedFraction();
                float y = mInterpolator.getInterpolation(x);

                curPoint.x = x;
                curPoint.y = y;

                Log.i("zincTest", "onAnimationUpdate: [" + x + "," + y + "銆?);

                mTimeInterpolatorView.setCurPoint(curPoint);
            }
        });

        mTimeInterpolatorView.setCurPoint(curPoint);
        mTimeInterpolatorView.setLineData(dataList);

        tvRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    Toast.makeText(TimeInterpolatorActivity.this, "鍔ㄧ敾姝ｅ湪杩涜涓紝璇风◢绛?, Toast.LENGTH_SHORT).show();
                    return;
                }

                updateState(true);
                String durationString = getEtDuration().getText().toString();
                long duration = TextUtils.isEmpty(durationString) ? 2000L : Long.parseLong(durationString);

                animatorSet.setDuration(duration);
                mAnimator.setInterpolator(mInterpolator);
                animatorSet.start();
            }
        });

        mAdapter = new TimeInterpolatorAdapter(this, interpolatorList);
        mAdapter.setListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);

    }

    private void createData() {
        dataList.clear();
        for (float x = 0; x <= 1; x += 1.0f / FRAME) {
            float y = mInterpolator.getInterpolation(x);

            PointF pointF = new PointF(x, y);
            dataList.add(pointF);
        }
    }

    /**
     * 鍒濆鍖栨彃鍊煎櫒锛岄渶瑕佺殑鍙互鍦ㄨ繖閲屾坊鍔犺嚜宸辩殑鎻掑€煎櫒
     */
    private void buildInterpolatorList() {
        interpolatorList.clear();
        interpolatorList.add(new TimeInterpolatorBean(true, "SpringInterpolator", new SpringInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AccelerateDecelerateInterpolator", new AccelerateDecelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AccelerateInterpolator", new AccelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AnticipateInterpolator", new AnticipateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "AnticipateOvershootInterpolator", new AnticipateOvershootInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "BounceInterpolator", new BounceInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "CycleInterpolator(1)", new CycleInterpolator(1)));
        interpolatorList.add(new TimeInterpolatorBean(false, "DecelerateInterpolator", new DecelerateInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "LinearInterpolator", new LinearInterpolator()));
        interpolatorList.add(new TimeInterpolatorBean(false, "OvershootInterpolator", new OvershootInterpolator()));

        mInterpolator = interpolatorList.get(0).getTimeInterpolator();

    }

    public static float getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static float dpToPx(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return dipValue * density + 0.5f;
    }

    public static float getStatusHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onTimeInterpolatorClick(int position) {
        for (TimeInterpolatorBean bean : interpolatorList) {
            bean.setSelect(false);
        }

        interpolatorList.get(position).setSelect(true);

        mInterpolator = interpolatorList.get(position).getTimeInterpolator();

        mAdapter.notifyDataSetChanged();

        createData();
        mTimeInterpolatorView.setLineData(dataList);

        curPoint.x = 0;
        curPoint.y = 0;
        mTimeInterpolatorView.setCurPoint(curPoint);

    }

    private void updateState(boolean isRunning) {
        this.isRunning = isRunning;
        tvStateInfo.setText(isRunning ? "running" : "ready");
        tvStateInfo.setTextColor(isRunning ?
                Color.parseColor("#32CD32") :
                Color.parseColor("#1E90FF"));
    }

}

