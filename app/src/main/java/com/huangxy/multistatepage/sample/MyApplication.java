package com.huangxy.multistatepage.sample;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.bindMultiState;

@SuppressLint("StaticFieldLeak")
public class MyApplication extends Application implements bindMultiState.Convertor {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //设置全局配置（非必须）
        MultiStatePageManager.Config()
                .addConverterFactory(this)
                .loadingView(R.layout.layout_loading)
                .emptyView(R.layout.layout_empty)
                .errorView(R.layout.layout_error)
                .showLoading(true); //设置初始状态是否显示加载状态（默认false）
    }

    @Override
    public int convert(int code) {
        if (code == 200) {
            return bindMultiState.SUCCESS;
        } else
        if (code == 211) {
            return bindMultiState.EMPTY;
        } else
        if (code >= 400) {
            return bindMultiState.ERROR;
        }
        return -1;
    }
}
