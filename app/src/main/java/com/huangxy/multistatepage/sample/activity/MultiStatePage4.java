package com.huangxy.multistatepage.sample.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;

public class MultiStatePage4 extends AppCompatActivity {

    private MultiStatePageManager pageManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multistatepage4);
        pageManager = findViewById(R.id.pagemanager);

//        TextView tv = new TextView(MultiStatePage4.this);
//        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80.f);
//        tv.setGravity(Gravity.CENTER);
//        tv.setText("Hello World!");
//        pageManager.setContentView(tv); //setContentView必须在先注入后使用才有效果，或者配合xml使用
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            pageManager.show(200, "请求成功");
            //pageManager.show(211, "暂无数据");
            //pageManager.show(500, "请求失败");
        }, 800);
    }
}
