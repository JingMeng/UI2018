package com.zinc.bezier.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.bezier.R;
import com.zinc.bezier.widget.HeartView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/10
 * @description
 */
public class HeartActivity extends AppCompatActivity {

    private HeartView heartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_heart);

        heartView = findViewById(R.id.heart_view);

    }

    public void onStart(View view) {
        heartView.start();
    }
}

