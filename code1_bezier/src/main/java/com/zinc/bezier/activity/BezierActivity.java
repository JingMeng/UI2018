package com.zinc.bezier.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zinc.bezier.dialog.BezierDialog;
import com.zinc.bezier.widget.BezierView;
import com.zinc.bezier.R;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/30
 * @description
 */
public class BezierActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivSetting;
    private ImageView ivPlay;
    private BezierView bezierView;

    private BezierDialog dialog;

    // 鏄惁鏄剧ず闄嶉樁绾?    private boolean isShowReduceOrderLine = true;
    // 鏄惁寰幆鎾斁
    private boolean isLoopPlay = false;
    // 闃舵暟锛堥粯璁や簲闃讹級
    private int order = 5;
    // 閫熺巼锛堥粯璁?0涓偣鐨勮烦杩囨挱鏀撅級
    private int rate = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);

        ivSetting = findViewById(R.id.iv_setting);
        ivPlay = findViewById(R.id.iv_play);
        bezierView = findViewById(R.id.bezier_view);
        bezierView.setIsShowReduceOrderLine(isShowReduceOrderLine);
        bezierView.setIsLoop(isLoopPlay);
        bezierView.setOrder(order);
        bezierView.setRate(rate);

        dialog = BezierDialog.getInstance();

        ivSetting.setOnClickListener(this);
        ivPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.iv_setting) {
            if (bezierView.getState() == BezierView.RUNNING) {
                Toast.makeText(this, "鍔ㄧ敾鎾斁涓紝璇风◢绛?..", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.setShowReduceOrderLine(isShowReduceOrderLine);
            dialog.setLoopPlay(isLoopPlay);
            dialog.setRate(rate);
            dialog.setOrder(order);
            dialog.show(getSupportFragmentManager(), "BezierActivity");
        } else if (i == R.id.iv_play) {

            if (bezierView.getState() == BezierView.RUNNING) {    // 杩愯涓?                ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons_play));
                bezierView.pause();
            } else if (bezierView.getState() == BezierView.STOP) { // 宸叉殏鍋?                ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons_pause));
                bezierView.pause();
            } else {
                ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons_pause));
                bezierView.start();
            }

        }
    }

    public void setShowReduceOrderLine(boolean showReduceOrderLine) {
        isShowReduceOrderLine = showReduceOrderLine;
        bezierView.setIsShowReduceOrderLine(isShowReduceOrderLine);
    }

    public void setLoopPlay(boolean loopPlay) {
        isLoopPlay = loopPlay;
        bezierView.setIsLoop(isLoopPlay);
    }

    public void setOrder(int order) {
        this.order = order;
        bezierView.setOrder(order);
        bezierView.invalidate();
    }

    public void setRate(int rate) {
        this.rate = rate;
        bezierView.setRate(rate);
    }

    public void resetPlayBtn() {
        ivPlay.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icons_play));
    }

}

