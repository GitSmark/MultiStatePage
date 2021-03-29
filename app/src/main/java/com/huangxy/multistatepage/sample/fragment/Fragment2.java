package com.huangxy.multistatepage.sample.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;

public class Fragment2 extends Fragment {

    private MultiStatePageManager pageManager1 = null;
    private MultiStatePageManager pageManager2 = null;
    private MultiStatePageManager pageManager3 = null;
    private MultiStatePageManager pageManager4 = null;
    private MultiStatePageManager pageManager5 = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_multistatepage2, container, false);
        pageManager1 = MultiStatePageManager.inject(root.findViewById(R.id.view1));
        pageManager2 = MultiStatePageManager.inject(root.findViewById(R.id.view2));
        pageManager3 = MultiStatePageManager.inject(root.findViewById(R.id.view3));
        pageManager4 = MultiStatePageManager.inject(root.findViewById(R.id.view4));
        pageManager5 = MultiStatePageManager.inject(root.findViewById(R.id.view5));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pageManager1.isLoading()) {
            handler.sendEmptyMessageDelayed(0x0001, 600);
        }
        if (pageManager2.isLoading()) {
            handler.sendEmptyMessageDelayed(0x0002, 1400);
        }
        if (pageManager3.isLoading()) {
            handler.sendEmptyMessageDelayed(0x0003, 2400);
        }
        if (pageManager4.isLoading()) {
            handler.sendEmptyMessageDelayed(0x0004, 3600);
        }
        if (pageManager5.isLoading()) {
            handler.sendEmptyMessageDelayed(0x0005, 4800);
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x0001:
                    pageManager1.success();
                    break;
                case 0x0002:
                    pageManager2.success();
                    break;
                case 0x0003:
                    pageManager3.success();
                    break;
                case 0x0004:
                    pageManager4.success();
                    break;
                case 0x0005:
                    pageManager5.success();
                    break;
                default:
                    break;
            }
        }
    };
}
