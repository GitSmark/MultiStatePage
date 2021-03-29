package com.huangxy.multistatepage.sample.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.huangxy.multistatepage.sample.R;
import com.huangxy.multistatepage.sample.fragment.Fragment1;
import com.huangxy.multistatepage.sample.fragment.Fragment2;
import com.huangxy.multistatepage.sample.fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;

public class MultiStatePage2 extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multistatepage2);

//        if(savedInstanceState == null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(R.id.fragment_container, new Fragment1()).commit(); //看这里这里（FragmentTransaction成功注入Fragment）
//        } else
        {
            mFragments.add(new Fragment1()); //注意仔细看，这两种效果是不一样的（ViewPager中无法直接注入Fragment）
            mFragments.add(new Fragment2());
            mFragments.add(new Fragment3());

            mViewPager = findViewById(R.id.viewpager);
            mViewPager.setOffscreenPageLimit(mFragments.size());
            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

                @Override
                public int getCount() {
                    return mFragments.size();
                }

                @NonNull
                @Override
                public Fragment getItem(int position) {
                    return mFragments.get(position);
                }
            });
        }
    }
}
