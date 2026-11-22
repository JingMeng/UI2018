package com.zinc.animation.activity;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/14
 * @description 灞炴€у姩鐢绘簮鐮佸垎鏋? */
public class SimpleAnimationActivity extends Activity {

    private static final String TAG = "SimpleAnimationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZincView view = new ZincView(this);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "zinc", 0f, 2f, 5f);
        // 鏃堕暱
        objectAnimator.setDuration(5000);
        // 鎻掑€煎櫒
        objectAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        // 浼板€煎櫒
        objectAnimator.setEvaluator(new FloatEvaluator());
        // 鏇存柊鍥炶皟
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue().getClass());
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue());
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
            }
        });
        // 鐢熷懡鍛ㄦ湡鍥炶皟
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "onAnimationCancel: ");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "onAnimationRepeat: ");
            }
        });
        // 寮€鍚?        objectAnimator.start();

    }

    public static class ZincView extends View {

        public ZincView(Context context) {
            super(context);
        }

        public void setZinc(float value) {
            Log.i(TAG, "setZinc: " + value);
        }

    }

}

