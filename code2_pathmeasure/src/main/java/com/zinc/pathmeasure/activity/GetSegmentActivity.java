package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.GetSegmentView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/4
 * @description
 */
public class GetSegmentActivity extends AppCompatActivity {

    private GetSegmentView mGetSegmentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_segment);
        mGetSegmentView = findViewById(R.id.get_segment_view);
    }

    public void onSetTrue(View view) {
        mGetSegmentView.setStartWithMoveTo(true);
    }

    public void onSetFalse(View view) {
        mGetSegmentView.setStartWithMoveTo(false);
    }
}

