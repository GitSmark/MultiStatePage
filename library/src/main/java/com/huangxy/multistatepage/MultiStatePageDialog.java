package com.huangxy.multistatepage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by huangxy on 2021/01/04.
 * https://github.com/GitSmark/MultiStatePage
 */
public class MultiStatePageDialog {

    public static final long delayMillis = 1200;
    protected static AlertDialog alertDialog = null;

    public static void showFailDialog(@NonNull Context context) {
        showFailDialog(context, null);
    }

    public static void showSuccessDialog(@NonNull Context context) {
        showSuccessDialog(context, null);
    }

    public static void showLoadingDialog(@NonNull Context context) {
        showLoadingDialog(context, null);
    }

    public static void show(@NonNull Context context, @LayoutRes int resource) {
        show(context, resource, null, -1);
    }

    public static void showFailDialog(@NonNull Context context, long delayMillis) {
        showFailDialog(context, null, delayMillis);
    }

    public static void showSuccessDialog(@NonNull Context context, long delayMillis) {
        showSuccessDialog(context, null, delayMillis);
    }

    public static void showLoadingDialog(@NonNull Context context, long delayMillis) {
        showLoadingDialog(context, null, delayMillis);
    }

    public static void show(@NonNull Context context, @LayoutRes int resource, long delayMillis) {
        show(context, resource, null, delayMillis);
    }

    public static void showFailDialog(@NonNull Context context, String text) {
        showFailDialog(context, text, delayMillis); //默认关闭
    }

    public static void showSuccessDialog(@NonNull Context context, String text) {
        showSuccessDialog(context, text, delayMillis); //默认关闭
    }

    public static void showLoadingDialog(@NonNull Context context, String text) {
        showLoadingDialog(context, text, -1);
    }

    public static void show(@NonNull Context context, @LayoutRes int resource, String text) {
        show(context, resource, text, -1);
    }

    public static void showFailDialog(@NonNull Context context, String text, long delayMillis) {
        show(context, R.layout.dialog_fail, text, delayMillis);
    }

    public static void showSuccessDialog(@NonNull Context context, String text, long delayMillis) {
        show(context, R.layout.dialog_success, text, delayMillis);
    }

    public static void showLoadingDialog(@NonNull Context context, String text, long delayMillis) {
        show(context, R.layout.dialog_loading, text, delayMillis);
    }

    //显示dialog
    public static void show(@NonNull Context context, @LayoutRes int resource, String text, long delayMillis) {

        //if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog);
            View rootView = LayoutInflater.from(context).inflate(resource, null);
            TextView tv = rootView.findViewById(R.id.dialog_text);
            if (tv != null && text != null) tv.setText(text);
            rootView.setClickable(false); //禁止选中
            rootView.setFocusable(false);
            builder.setView(rootView);
            alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(false);
            Window window = alertDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(null);
                window.setDimAmount(0);
            }
        //}
        alertDialog.show();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            if (delayMillis > 0) {
                handler.sendEmptyMessageDelayed(0x0001, delayMillis); //延时关闭
            }
        }
    }

    //隐藏dialog
    public static void hide() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private static Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x0001:
                    hide();
                    break;
                default:
                    break;
            }
        }
    };
}
