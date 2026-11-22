package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.BoatWaveView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/4
 * @description 涔橀鐮存氮鐨勫皬鑸? */
public class BoatLoadingActivity extends AppCompatActivity {

    BoatWaveView mBoatWaveView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_wave_view);
        mBoatWaveView = findViewById(R.id.boat_wave_view);
    }

    public void start(View view) {
        mBoatWaveView.startAnim();
    }

    public void stop(View view) {
        mBoatWaveView.stopAnim();
    }

}

