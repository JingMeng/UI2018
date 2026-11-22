package com.zinc.pathmeasure.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.pathmeasure.PlaneLoadingView;
import com.zinc.pathmeasure.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/11/5
 * @description
 */
public class PlaneLoadingActivity extends AppCompatActivity {

    PlaneLoadingView mPlaneLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plane_loading);

        mPlaneLoadingView = findViewById(R.id.plane_loading_view);
    }

    public void start(View view) {
        mPlaneLoadingView.startLoading();
    }

    public void stop(View view) {
        mPlaneLoadingView.stopLoading();
    }

}

