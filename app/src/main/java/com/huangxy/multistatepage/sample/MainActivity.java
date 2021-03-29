package com.huangxy.multistatepage.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huangxy.multistatepage.sample.activity.MultiStatePage1;
import com.huangxy.multistatepage.sample.activity.MultiStatePage2;
import com.huangxy.multistatepage.sample.activity.MultiStatePage3;
import com.huangxy.multistatepage.sample.activity.MultiStatePage4;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                gotoActivity(MultiStatePage1.class);
                break;
            case R.id.btn2:
                gotoActivity(MultiStatePage2.class);
                break;
            case R.id.btn3:
                gotoActivity(MultiStatePage3.class);
                break;
            case R.id.btn4:
                gotoActivity(MultiStatePage4.class);
                break;
            default:
                break;
        }
    }

    private void gotoActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }
}
