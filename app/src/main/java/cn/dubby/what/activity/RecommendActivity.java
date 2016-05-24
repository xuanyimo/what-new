package cn.dubby.what.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.dubby.what.R;
import cn.dubby.what.component.NewsFragmentAdapter;
import cn.dubby.what.component.fragment.Recommend4Coding;
import cn.dubby.what.component.fragment.Recommend4Cooking;
import cn.dubby.what.component.fragment.Recommend4Game;
import cn.dubby.what.component.fragment.Recommend4Relaxing;

public class RecommendActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    //tab切换相关的
    private List<Fragment> fragmentList;
    private List<String> titleList;

    private NewsFragmentAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        loadData();
    }

    private void initView() {
        setContentView(R.layout.activity_recommend);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        fragmentList = new ArrayList<>();
        fragmentList.add(new Recommend4Coding());
        fragmentList.add(new Recommend4Relaxing());
        fragmentList.add(new Recommend4Cooking());
        fragmentList.add(new Recommend4Game());


        titleList = new ArrayList<>();
        titleList.add("编程");
        titleList.add("休闲");
        titleList.add("厨房");
        titleList.add("游戏");


        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(3)));

        pagerAdapter = new NewsFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadData() {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.zoomin,R.animator.zoomout);
    }


}
