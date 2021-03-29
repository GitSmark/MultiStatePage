package com.huangxy.multistatepage.sample.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.huangxy.mcadapter.McEntity;
import com.huangxy.mcadapter.McRcvAdapter;
import com.huangxy.multistatepage.MultiStatePageManager;
import com.huangxy.multistatepage.sample.R;
import com.huangxy.multistatepage.sample.widget.TestListItem;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<McEntity> datalist = new ArrayList<>();
    private MultiStatePageManager pageManager = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_multistatepage3, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new McRcvAdapter(datalist));

        //pageManager = MultiStatePageManager.inject(recyclerView); //直接注入recyclerView的必须在setColorSchemeColors()前调用，否则会显示异常

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189)); //因为setSchemeColors会调用ensureTarget()指定targetView
        swipeRefreshLayout.setOnRefreshListener(this);

        pageManager = MultiStatePageManager.inject(root);
        return pageManager;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i=0; i<20; i++) {
            datalist.add(new McEntity("MultiStatePage3 Fragment", TestListItem.class));
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pageManager.isLoading()) {
            new Handler().postDelayed(() -> pageManager.success(), 1000);
        }
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
