package com.zinc.flowlayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DATA_INTERVAL = 2;

    private static final List<String> INFO = new ArrayList<>();

    static {
        INFO.add("Hello World!");
        INFO.add("鐚涚寷鐨勫皬鐩嗗弸");
        INFO.add("鎺?);
        INFO.add("閲戦噾閲?);
        INFO.add("PHP鏄渶濂界殑璇█");
        INFO.add("澶ndroid");
        INFO.add("IOS");
        INFO.add("JAVA");
        INFO.add("Python");
        INFO.add("杩欐槸涓€涓緢闀垮緢闀垮緢闀垮緢闀垮緢闀垮緢闀垮緢闀垮緢闀胯秴绾ф棤鏁岄暱鐨勫彞瀛?);
    }

    private TagFlowLayout tagFlowLayout;
    private TextView tvDataCount;
    private SeekBar sbDataCount;
    private Switch swShowComplexLayout;

    private Random mRandom = new Random();

    private int mDataCount = 10;

    private int mTypeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout_client);

        tagFlowLayout = findViewById(R.id.tag_flow_layout);
        tvDataCount = findViewById(R.id.tv_data_count);
        sbDataCount = findViewById(R.id.sb_data_count);
        swShowComplexLayout = findViewById(R.id.sw_show_complex_layout);

        tvDataCount.setText(String.format(getString(R.string.data_count), mDataCount));

        swShowComplexLayout.setChecked(false);
        swShowComplexLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTypeCount = isChecked ? 2 : 1;
            }
        });

        sbDataCount.setProgress(mDataCount / DATA_INTERVAL);
        sbDataCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDataCount = progress * DATA_INTERVAL;
                tvDataCount.setText(String.format(getString(R.string.data_count), mDataCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.tv_play).setOnClickListener(this);

        createData();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_play) {
            createData();
        }
    }

    /**
     * 鍒涘缓view
     */
    private void createData() {

        // 鍏堟竻闄や箣鍓嶇殑view
        tagFlowLayout.removeAllViews();

        // 寰幆娣诲姞view
        for (int i = 0; i < mDataCount; ++i) {
            int type = mRandom.nextInt(mTypeCount);
            if (type == 0) {
                createSingleView();
            } else {
                createComplexView();
            }
        }


        // 鍒锋柊
        tagFlowLayout.invalidate();

    }

    private void createSingleView() {
        int index = mRandom.nextInt(INFO.size());
        final String itemContent = INFO.get(index);

        TextView item = (TextView) getLayoutInflater()
                .inflate(R.layout.item_single_tag, tagFlowLayout, false);
        item.setText(itemContent);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClientActivity.this, "text:" + itemContent,
                        Toast.LENGTH_SHORT).show();
            }
        });

        tagFlowLayout.addView(item);
    }

    private void createComplexView() {
        int index = mRandom.nextInt(INFO.size());
        final String itemContent = INFO.get(index);

        ViewGroup group = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.item_complex_tag, tagFlowLayout, false);

        TextView tvInfo = group.findViewById(R.id.tv_info);
        tvInfo.setText(itemContent.length() > 0 ?
                itemContent.substring(0, 1) :
                "");

        TextView button = group.findViewById(R.id.tv_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClientActivity.this, "text:" + itemContent,
                        Toast.LENGTH_SHORT).show();
            }
        });

        tagFlowLayout.addView(group);
    }

}

