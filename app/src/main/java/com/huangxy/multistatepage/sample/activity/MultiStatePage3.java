package com.huangxy.multistatepage.sample.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;

public class MultiStatePage3 extends AppCompatActivity implements View.OnClickListener {

    private MultiStatePageManager pageManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multistatepage3);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);

        ImageView iv = findViewById(R.id.any_view);
        pageManager = MultiStatePageManager.inject(iv); //anyView注入
        pageManager.setListener(new MultiStatePageManager.OnRetryEventListener() {
            @Override
            public void onRetryEvent() {
                TextView tv = new TextView(MultiStatePage3.this);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80.f);
                tv.setText("MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager\n" +
                           "MultiStatePageManager");
                pageManager.showView(tv);
            }
        });
        pageManager.success();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                pageManager.loading();
                break;
            case R.id.btn2:
                pageManager.success();
                break;
            case R.id.btn3:
                pageManager.empty();
                break;
            case R.id.btn4:
                pageManager.error();
                break;
            default:
                break;
        }
    }
}