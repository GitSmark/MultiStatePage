package com.huangxy.multistatepage;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by huangxy on 2021/01/04.
 * https://github.com/GitSmark/MultiStatePage
 */
public class MultiStatePageManager extends RelativeLayout {

    private View loadingView;
    private View contentView;
    private View emptyView;
    private View errorView;

    private boolean showLoading;

    private LayoutParams lp;
    private ViewGroup targetParent;
    private OnRetryEventListener listener;

    private final int MULTI_STATE_INJECT_VIEW = 0x4010;
    private final int MULTI_STATE_INJECT_FRAGMENT = 0x4011;
    private final int MULTI_STATE_INJECT_ACTIVITY = 0x4012;

    private MultiStatePageConfig _config = null;

    public MultiStatePageConfig getConfig() {
        return (_config != null)? _config: _GlobalConfig; //当前配置:全局配置
    }

    protected MultiStatePageManager setConfig(MultiStatePageConfig config) {
        _config = config;
        return this;
    }

    public boolean isShowLoading() {
        return showLoading || getConfig().isShowLoading();
    }

    private static MultiStatePageConfig _GlobalConfig = new MultiStatePageConfig();

    public static MultiStatePageConfig Config() {
        return _GlobalConfig;
    }

    public MultiStatePageManager(@NonNull Context context) {
        this(context, null);
    }

    public MultiStatePageManager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatePageManager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MultiStatePageManager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStatePageManager);
        int layoutRes = typedArray.getResourceId(R.styleable.MultiStatePageManager_inject_view, -1);
        if (layoutRes != -1 && layoutRes != 0) contentView = layoutInflater.inflate(layoutRes, null);
            layoutRes = typedArray.getResourceId(R.styleable.MultiStatePageManager_loading_view, -1);
        if (layoutRes != -1 && layoutRes != 0) loadingView = layoutInflater.inflate(layoutRes, null);
            layoutRes = typedArray.getResourceId(R.styleable.MultiStatePageManager_empty_view, -1);
        if (layoutRes != -1 && layoutRes != 0) emptyView = layoutInflater.inflate(layoutRes, null);
            layoutRes = typedArray.getResourceId(R.styleable.MultiStatePageManager_error_view, -1);
        if (layoutRes != -1 && layoutRes != 0) errorView = layoutInflater.inflate(layoutRes, null);
        showLoading = typedArray.getBoolean(R.styleable.MultiStatePageManager_show_loading, false);
        typedArray.recycle();
        initState(context);
    }

    private void initState(@NonNull Context context) {

        if (getId() == -1 || getId() == 0) this.setId(View.generateViewId());
        lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (loadingView == null) loadingView = layoutInflater.inflate(getConfig().getLoadingLayoutRes(), null);
        if (emptyView == null) emptyView = layoutInflater.inflate(getConfig().getEmptyLayoutRes(), null);
        if (errorView == null) errorView = layoutInflater.inflate(getConfig().getErrorLayoutRes(), null);

        addListener(emptyView); addListener(errorView);

        //if (showLoading) loading(); else success();
    }

    private void addListener(View view) {
        if (view != null) {
            View retryBtn = view.findViewById(getConfig().getRetryIdRes());
            if (retryBtn != null) {
                retryBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            //if (retryLoading) loading();
                            listener.onRetryEvent();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //支持xml直接嵌套，优先级别低于app:inject_view="@LayoutRes"
        if (getInjectView() == null && getChildCount() > 0) {
            if (getChildCount() == 1) { //只允许有一个子节点（允许一个父子节点包含多个子节点）
                contentView = getChildAt(0);
            } else {
                throw new IllegalStateException(getClass().getSimpleName() + " can host only one direct child");
            }
        }

        if (showLoading) loading(); else success();
    }

    /**
     * 通用静态注入方法
     * 在Activity onCreate方法中 MultiStatePageManager.inject(this);
     * 在Fragment onCreateView方法中 MultiStatePageManager.inject(rootView);
     */
    public static MultiStatePageManager inject(@NonNull Object target) {
        return inject(target, null);
    }

    /**
     * 通用静态注入方法
     * @param target
     * @param listener
     */
    public static MultiStatePageManager inject(@NonNull Object target, @Nullable OnRetryEventListener listener) {
        MultiStatePageManager pageManager;
        if (target instanceof View) {
            pageManager = new MultiStatePageManager((View)target, null);
        } else
        if (target instanceof Activity) {
            pageManager = new MultiStatePageManager((Activity)target, (MultiStatePageConfig)null);
        } else
        if (target instanceof Fragment) {
            pageManager = new MultiStatePageManager((Fragment)target, null);
        } else
        //if (target instanceof Context) {
            pageManager = new MultiStatePageManager((Context) target);
        //}
        pageManager.setListener(listener);
        return pageManager;
    }

    public static MultiStatePageManager inject(@NonNull Activity activity, @Nullable MultiStatePageConfig config) {
        return new MultiStatePageManager(activity, config);
    }

    @Deprecated
    public static MultiStatePageManager inject(@NonNull Fragment fragment, @Nullable MultiStatePageConfig config) {
        return new MultiStatePageManager(fragment, config);
    }

    public static MultiStatePageManager inject(@NonNull View view, @Nullable MultiStatePageConfig config) {
        return new MultiStatePageManager(view, config);
    }

    /**
     * 实现原理
     * 1. android.R.id.content 是Activity setContentView 内容的父view
     * 2. 在这个view中移除原本要添加的contentView 替换成 MultiStatePage
     */
    private MultiStatePageManager(@NonNull Activity activity, @Nullable MultiStatePageConfig config) {
        super(activity, null, 0, 0);
        this.setConfig(config)
            .initState(activity);
        targetParent = activity.findViewById(android.R.id.content);
        setTag(MULTI_STATE_INJECT_ACTIVITY);
        injectView(activity);
    }

    @Deprecated //injectFragment maybe fail to inject. -> See the method canInjectView for more details.
    private MultiStatePageManager(@NonNull Fragment fragment, @Nullable MultiStatePageConfig config) {
        super(fragment.getContext(), null, 0, 0);
        this.setConfig(config)
            .initState(fragment.getContext());
        if (fragment.getView() == null) {
            Log.w(getClass().getSimpleName(), "injectFragment must in or after onViewCreated");
            return;
        }
        targetParent = (ViewGroup) fragment.getView().getParent();
        setTag(MULTI_STATE_INJECT_FRAGMENT);
        injectView(fragment);
    }

    private MultiStatePageManager(@NonNull View view, @Nullable MultiStatePageConfig config) {
        super(view.getContext(), null, 0, 0);
        this.setConfig(config)
            .initState(view.getContext());
        targetParent = (ViewGroup) view.getParent();
        setTag(MULTI_STATE_INJECT_VIEW);
        injectView(view);
    }

    private void injectView(Object target) {

        if (target instanceof View) {
            contentView = (View) target;
        } else
        if (target instanceof Fragment) {
            contentView = ((Fragment) target).getView();
        } else {
            contentView = targetParent.getChildAt(0);
        }

        if (contentView != null && targetParent != null) {

            if (!canInjectView(targetParent)) {
                contentView = null;
                return;
            }

            int index = 0, childCount = targetParent.getChildCount();

            for (int i = 0; i < childCount; i++) {
                if (targetParent.getChildAt(i) == contentView) {
                    index = i;
                    break;
                }
            }

            targetParent.removeView(contentView);
            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
            targetParent.addView(this, index, lp);
        }

        if (isShowLoading()) loading(); else success();
    }

    /**
     * 判断targetParent能否直接注入
     * 1. ViewPager无法直接通过addView注入
     * 2. 不能同时包含injectFragment和injectView
     */
    private boolean canInjectView(ViewGroup parent) {

        if (parent == null) {
            Log.w(getClass().getSimpleName(), "targetParent can not be null");
            return false;
        }

        if (parent instanceof ViewPager) {
            Log.w(getClass().getSimpleName(), "targetParent can not be ViewPager");
            return false;
        }

        if (getTag().equals(MULTI_STATE_INJECT_FRAGMENT) && parent.findViewWithTag(MULTI_STATE_INJECT_VIEW) != null) {
            Log.w(getClass().getSimpleName(), "targetParent can not contain both injectFragment and injectView");
            return false;
        }

        return true;
    }

    public View getInjectView() {
        return contentView;
    }

    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setContentView(view);
    }

    public void setContentView(@NonNull View view) {
        contentView = view;
        //showView(view);
    }

    public void setLoadingView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setLoadingView(view);
    }

    public void setLoadingView(@NonNull View view) {
        loadingView = view;
        //showView(view);
    }

    public void setEmptyView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setEmptyView(view);
    }

    public void setEmptyView(@NonNull View view) {
        emptyView = view;
        addListener(view);
        //showView(view);
    }

    public void setErrorView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setErrorView(view);
    }

    public void setErrorView(@NonNull View view) {
        errorView = view;
        addListener(view);
        //showView(view);
    }

    public void setListener(@Nullable OnRetryEventListener listener) {
        this.listener = listener;
    }

    public boolean isLoading() {
        return loadingView != null && getChildCount() > 0 && getChildAt(0) == loadingView;
    }

    public boolean isSuccess() {
        return contentView != null && getChildCount() > 0 && getChildAt(0) == contentView;
    }

    //加载状态框
    public void showLoadingDialog() {
        doInMainThread(() -> MultiStatePageDialog.showLoadingDialog(getContext()));
    }

    public void showLoadingDialog(String prompt) {
        doInMainThread(() -> MultiStatePageDialog.showLoadingDialog(getContext(), prompt));
    }

    //成功提示框
    public void showSuccessDialog() {
        doInMainThread(() -> MultiStatePageDialog.showSuccessDialog(getContext()));
    }

    public void showSuccessDialog(String prompt) {
        doInMainThread(() -> MultiStatePageDialog.showSuccessDialog(getContext(), prompt));
    }

    //失败提示框
    public void showFailDialog() {
        doInMainThread(() -> MultiStatePageDialog.showFailDialog(getContext()));
    }

    public void showFailDialog(String prompt) {
        doInMainThread(() -> MultiStatePageDialog.showFailDialog(getContext(), prompt));
    }

    //关闭提示框
    public void hideDialog() {
        doInMainThread(MultiStatePageDialog::hide);
    }

    public void show(int code, String message) {
        bindMultiState.Convertor convertor = getConfig().getConvertor();
        if (convertor != null) {
            int state = convertor.convert(code);
            if (state == bindMultiState.EMPTY) {
                setEmptyText(message);
            } else
            if (state == bindMultiState.ERROR) {
                setErrorText(message);
            }
            show(state);
        }
    }

    @Deprecated
    public void show(int state) {
        switch (state) {
            case bindMultiState.LOADING:
                loading();
                break;
            case bindMultiState.SUCCESS:
                success();
                break;
            case bindMultiState.EMPTY:
                empty();
                break;
            case bindMultiState.ERROR:
                error();
                break;
            default:
                break;
        }
    }

    //加载中
    public void loading() {
        if (!isLoading()) {
            showView(loadingView);
        }
    }

    //成功状态
    public void success() {
        if (!isSuccess()) {
            showView(contentView);
        }
    }

    //无数据
    public void empty() {
        showView(emptyView);
    }

    public void empty(String prompt) {
        setEmptyText(prompt);
        showView(emptyView);
    }

    public void setEmptyText(String prompt) {
        if (emptyView != null) {
            TextView tv = emptyView.findViewById(getConfig().getPromptIdRes());
            if (tv != null) tv.setText(prompt);
        }
    }

    //失败状态
    public void error() {
        showView(errorView);
    }

    public void error(String prompt) {
        setErrorText(prompt);
        showView(errorView);
    }

    public void setErrorText(String prompt) {
        if (errorView != null) {
            TextView tv = errorView.findViewById(getConfig().getPromptIdRes());
            if (tv != null) tv.setText(prompt);
        }
    }

    //其它状态
    public void showView(@LayoutRes int layoutResID) {
        View child = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        doInMainThread(() -> alterView(child));
    }

    public void showView(final View child) {
        doInMainThread(() -> alterView(child));
    }

    private void alterView(View child) {
        try {
            if (child != null) {
                if (child.getParent() != null) {
                    ViewGroup parent = (ViewGroup) child.getParent();
                    if (!canInjectView(parent)) return;
                    else parent.removeView(child);
                }
                if (child.getLayoutParams() == null) {
                    child.setLayoutParams(lp);
                }
                this.removeAllViews();
                this.addView(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doInMainThread(Runnable R) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            post(() -> R.run());
        } else {
            R.run();
        }
    }

    public interface OnRetryEventListener {
        void onRetryEvent();
    }
}
