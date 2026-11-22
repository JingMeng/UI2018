package com.zinc.xfermode.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zinc.xfermode.R;
import com.zinc.xfermode.widget.ScratchCardImageView;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/2/14
 * @description 鍒埉鍗? */
public class ScratchCardActivity extends AppCompatActivity {

    ScratchCardImageView iv_scratch_card;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        iv_scratch_card = findViewById(R.id.iv_scratch_card);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iv_scratch_card.recycle();
    }

}

