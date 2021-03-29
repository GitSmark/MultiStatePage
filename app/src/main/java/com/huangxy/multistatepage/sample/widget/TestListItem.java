package com.huangxy.multistatepage.sample.widget;

import android.view.View;
import android.widget.TextView;

import com.huangxy.mcadapter.McAdapterItem;
import com.huangxy.multistatepage.sample.R;

public class TestListItem extends McAdapterItem<String> {

    private TextView tv;

    public TestListItem(){}

    public TestListItem(Object obj){
        super(obj);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.listitem_test;
    }

    @Override
    public void onBindViews(View root) {
        super.onBindViews(root);
        tv = root.findViewById(R.id.tv_sub);
    }

    @Override
    public void onUpdateViews(String model) {
        tv.setText(model);
    }

    @Override
    public void onItemAction() {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener!=null) {
                    mClickListener.onClick(v, getBindPosition());
                }
            }
        });
    }
}