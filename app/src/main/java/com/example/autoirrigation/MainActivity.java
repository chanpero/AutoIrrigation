package com.example.autoirrigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.autoirrigation.DrawerActivities.About.AboutActivity;
import com.example.autoirrigation.DrawerActivities.Feedback.FeedbackActivity;
import com.example.autoirrigation.DrawerActivities.Setting.SettingActivity;
import com.example.autoirrigation.Fragments.HomeFragment;
import com.example.autoirrigation.Fragments.IrrigationStatistic;
import com.example.autoirrigation.Fragments.TimingTaskFragment;
import com.example.autoirrigation.Tools.TabAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabAdapter adapter;
    private NavigationView navView;
    public static final String[] tabTitle = new String[]{"首页", "定时任务", "灌溉统计"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        for(int i = 1; i < 100; i++){
//            new DBUtil().insertTask("000" + i, "open", "2019/8/28/4/12", "everyWeek", "njfucs123456");
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        List<Fragment> fragments = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance();
        homeFragment.setActivity(this);
        fragments.add(homeFragment);
        fragments.add(TimingTaskFragment.newInstance());
        fragments.add(IrrigationStatistic.newInstance());

        adapter = new TabAdapter(getSupportFragmentManager(), fragments, tabTitle);
        //给viewPager设置适配器
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(viewPager);

        /**
         * 设置底部tab样式，以及添加监听事件
         */
        Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition())).setIcon(R.drawable.home_tab_selected_icon);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.task_tab_icon);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.irrigation_history_icon);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setTitle(R.string.app_name);
                        tab.setIcon(R.drawable.home_tab_selected_icon);
                        break;
                    case 1:
                        setTitle("定时任务");
                        tab.setIcon(R.drawable.task_tab_selected_icon);
                        break;
                    case 2:
                        setTitle("灌溉统计");
                        tab.setIcon(R.drawable.irrigation_history_selected_icon);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.home_tab_icon);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.task_tab_icon);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.irrigation_history_icon);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        /**
         * 设置侧边栏home按钮
         */
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        }

        /**
         * 设置侧边栏MenuItem点击事件
         */
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_setting:
                        Intent intent = getIntent();
                        String pwd = intent.getStringExtra("upwd");
                        String id = intent.getStringExtra("uid");
                        Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("uid", id);
                        bundle.putString("upwd",pwd);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        break;
                    case R.id.nav_feedback:
                        startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                }
                return true;
            }
        });
    }


    //home按钮打开drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                for (int i = 0; i < navView.getMenu().size(); i++) {
                    navView.getMenu().getItem(i).setChecked(false);
                }
                findViewById(R.id.nav_view).setAlpha(0.8f);
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}


