package com.huangxy.multistatepage.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxy.multistatepage.MultiStatePageConfig;
import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;

public class MultiStatePage1 extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;

    private MultiStatePageManager pageManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multistatepage1);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        editText = findViewById(R.id.edit_text);

        pageManager = MultiStatePageManager.inject(this); //Activity注入
        //pageManager = MultiStatePageManager.inject(this, MultiStatePageConfig);
        //pageManager = MultiStatePageManager.inject(this, OnRetryEventListener);
        pageManager.setListener(new MultiStatePageManager.OnRetryEventListener() {
            @Override
            public void onRetryEvent() {
                pageManager.success();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageManager.loading();
        new Handler().postDelayed(() -> {
            pageManager.error("点击重新加载按钮试试看...");
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        final String prompt = editText.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btn1:
                if (!TextUtils.isEmpty(prompt)) {
                    pageManager.showLoadingDialog(prompt); //loadingDialog需要手动调用hideDialog();
                } else {
                    pageManager.showLoadingDialog(); //默认返回键可关闭Dialog
                }
                break;
            case R.id.btn2:
                if (!TextUtils.isEmpty(prompt)) {
                    pageManager.showSuccessDialog(prompt);
                } else {
                    pageManager.showSuccessDialog();
                }
                break;
            case R.id.btn3:
                if (!TextUtils.isEmpty(prompt)) {
                    pageManager.showFailDialog(prompt);
                } else {
                    pageManager.showFailDialog();
                }
                break;
            default:
                break;
        }
    }
}