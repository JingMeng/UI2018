package com.zinc.bezier.activity;

import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.zinc.bezier.widget.DIYBezierView;
import com.zinc.bezier.R;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?019/1/9
 * @description 甯﹀姩鐢昏繃绋嬬殑璐濆灏? */
public class DIYBezierActivity extends AppCompatActivity {

    private DIYBezierView diyBezierView;
    private RadioGroup rgStatus;
    private Switch showLineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_bezier);

        diyBezierView = findViewById(R.id.circle_bezier_view);
        rgStatus = findViewById(R.id.rg_status);
        showLineSwitch = findViewById(R.id.show_line_switch);

        diyBezierView.setIsShowHelpLine(true);
        showLineSwitch.setChecked(true);
        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.status_free) {                // 鑷敱鍙樻崲
                    diyBezierView.setStatus(DIYBezierView.Status.FREE);
                } else if (checkedId == R.id.status_mirror_diff) {  // 闀滃儚寮傚悜
                    diyBezierView.setStatus(DIYBezierView.Status.MIRROR_DIFF);
                } else if (checkedId == R.id.status_three) {        // 涓夌偣鎷藉姩
                    diyBezierView.setStatus(DIYBezierView.Status.THREE);
                } else if (checkedId == R.id.status_mirror_same) {  // 闀滃儚鍚屽悜
                    diyBezierView.setStatus(DIYBezierView.Status.MIRROR_SAME);
                }
            }
        });

        showLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                diyBezierView.setIsShowHelpLine(isChecked);
            }
        });

    }

    public void onReset(View view) {
        diyBezierView.reset();
    }

    public void onLog(View view) {
        List<PointF> controlPointList = diyBezierView.getControlPointList();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (int i = 0; i < controlPointList.size(); ++i) {
            stringBuilder.append("绗?)
                    .append(i)
                    .append("涓偣鍧愭爣(鍗曚綅dp)锛歔")
                    .append(px2dip(this, controlPointList.get(i).x))
                    .append(", ")
                    .append(px2dip(this, controlPointList.get(i).y))
                    .append("]")
                    .append("\n");
        }

        Log.i("DIY Bezier", "鎺у埗鐐规棩蹇? " + stringBuilder.toString());

    }

    public int px2dip(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }
}

