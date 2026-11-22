package com.zinc.drawable_gravity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Jiang zinc
 * @date 鍒涘缓鏃堕棿锛?018/10/29
 * @description
 */
public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_client);

        Drawable unselectedDrawable = ContextCompat.getDrawable(this, R.drawable.avft);
        Drawable selectedDrawable = ContextCompat.getDrawable(this, R.drawable.avft_active);

        ImageView imageview = findViewById(R.id.iv);

        RevealDrawable revealDrawable = new RevealDrawable(unselectedDrawable,
                selectedDrawable, RevealDrawable.HORIZONTAL);

        imageview.setImageDrawable(revealDrawable);
        imageview.setImageLevel(5000);

        iv = (ImageView) findViewById(R.id.iv);
        findViewById(R.id.btn_change).setOnClickListener(this);

    }

    private EditText getLevel() {
        return (EditText) findViewById(R.id.level);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_change) {
            String levelString = getLevel().getText().toString();
            try {
                int level = Integer.parseInt(levelString);
                if (level < 0 || level > 10000) {
                    Toast.makeText(this, "鑼冨洿0-10000", Toast.LENGTH_SHORT).show();
                    return;
                }

                iv.setImageLevel(level);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

