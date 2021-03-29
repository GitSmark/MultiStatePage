package com.huangxy.multistatepage;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

/**
 * Created by huangxy on 2021/01/04.
 * https://github.com/GitSmark/MultiStatePage
 */
public class MultiStatePageConfig {

    @LayoutRes
    private int loading_LayoutRes = R.layout.layout_loading;
    @LayoutRes
    private int empty_LayoutRes = R.layout.layout_empty;
    @LayoutRes
    private int error_LayoutRes = R.layout.layout_error;
    @IdRes
    private int prompt_IdRes = R.id.tv_prompt; //文本提示
    @IdRes
    private int retry_IdRes = R.id.btn_retry; //重试按钮

    //初始状态是否显示加载状态
    private boolean showLoading = false;

    public MultiStatePageConfig loadingView(@LayoutRes int layoutResID) {
        loading_LayoutRes = layoutResID;
        return this;
    }

    public int getLoadingLayoutRes() {
        return loading_LayoutRes;
    }

    public MultiStatePageConfig emptyView(@LayoutRes int layoutResID) {
        empty_LayoutRes = layoutResID;
        return this;
    }

    public int getEmptyLayoutRes() {
        return empty_LayoutRes;
    }

    public MultiStatePageConfig errorView(@LayoutRes int layoutResID) {
        error_LayoutRes = layoutResID;
        return this;
    }

    public int getErrorLayoutRes() {
        return error_LayoutRes;
    }

    public MultiStatePageConfig promptText(@IdRes int IdRes) {
        prompt_IdRes = IdRes;
        return this;
    }

    public int getPromptIdRes() {
        return prompt_IdRes;
    }

    public MultiStatePageConfig retryBtn(@IdRes int IdRes) {
        retry_IdRes = IdRes;
        return this;
    }

    public int getRetryIdRes() {
        return retry_IdRes;
    }

    public MultiStatePageConfig showLoading(boolean isShowLoading) {
        showLoading = isShowLoading;
        return this;
    }

    public boolean isShowLoading() {
        return showLoading;
    }
}
