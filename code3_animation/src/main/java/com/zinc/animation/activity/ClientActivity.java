package com.zinc.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.zinc.animation.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/17
 * @description
 */
public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_client);
    }

    public void onRadar(View view) {
        startActivity(new Intent(this, RadarActivity.class));
    }

    public void onDial(View view) {
        startActivity(new Intent(this, DialActivity.class));
    }

    public void onInterpolator(View view) {
        startActivity(new Intent(this, TimeInterpolatorActivity.class));
    }

    public void onShoppingCart(View view) {
        startActivity(new Intent(this, ShoppingActivity.class));
    }

    public void onResource(View view) {
        startActivity(new Intent(this, SimpleAnimationActivity.class));
    }

}

