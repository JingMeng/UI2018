package com.zinc.xfermode.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zinc.xfermode.widget.GoogleXFerModeView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/22
 * @description
 */
public class GoogleXFerModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GoogleXFerModeView(this));
    }
}

