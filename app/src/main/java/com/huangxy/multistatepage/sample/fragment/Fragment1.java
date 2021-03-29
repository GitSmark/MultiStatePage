package com.huangxy.multistatepage.sample.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;
import com.huangxy.multistatepage.sample.activity.MultiStatePage4;

public class Fragment1 extends Fragment {

    private MultiStatePageManager pageManager = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_multistatepage1, container, false);

        root.findViewById(R.id.edit_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MultiStatePage4.class));
            }
        });

        //pageManager = MultiStatePageManager.inject(root); //正确姿势
        //return pageManager;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageManager = MultiStatePageManager.inject(this); //注意：ViewPager中无法直接注入Fragment，推荐统一使用inject(view);
        pageManager.setListener(new MultiStatePageManager.OnRetryEventListener() {
            @Override
            public void onRetryEvent() {
                pageManager.success();
            }
        });
        pageManager.error();
    }
}
