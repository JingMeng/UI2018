package com.zinc.paint.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.paint.R;
import com.zinc.paint.widget.GradientHeartView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/19
 * @description
 */
public class GradientHeartActivity extends AppCompatActivity {

    GradientHeartView gradientHeartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient_heart);

        gradientHeartView = findViewById(R.id.gradient_view);

        findViewById(R.id.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradientHeartView.setType(0);
            }
        });

        findViewById(R.id.radial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradientHeartView.setType(1);
            }
        });

        findViewById(R.id.sweep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradientHeartView.setType(2);
            }
        });

    }
}

