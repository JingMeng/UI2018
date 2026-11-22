package com.zinc.paint.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zinc.paint.widget.RadarView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/30
 * @description
 */
public class RadarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new RadarView(this));
    }
}

