package com.zinc.pathmeasure.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/11/5
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathmeasure_client);
    }

    public void onAbc(View view) {
        Intent intent = new Intent(this, CommonActivity.class);
        intent.putExtra(CommonActivity.TYPE, CommonActivity.ABC);
        startActivity(intent);
    }

    public void onGetSegment(View view) {
        Intent intent = new Intent(this, GetSegmentActivity.class);
        startActivity(intent);
    }

    public void onArrow(View view) {
        Intent intent = new Intent(this, PlaneLoadingActivity.class);
        startActivity(intent);
    }

    public void onLoading(View view) {
        Intent intent = new Intent(this, CommonActivity.class);
        intent.putExtra(CommonActivity.TYPE, CommonActivity.LOADING);
        startActivity(intent);
    }

    public void onBoat(View view) {
        Intent intent = new Intent(this, BoatLoadingActivity.class);
        startActivity(intent);
    }

    public void onGetLength(View view) {
        Intent intent = new Intent(this, CommonActivity.class);
        intent.putExtra(CommonActivity.TYPE, CommonActivity.GET_LENGTH);
        startActivity(intent);
    }
}

