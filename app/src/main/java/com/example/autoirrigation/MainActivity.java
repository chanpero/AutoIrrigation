package com.example.autoirrigation;


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

import com.example.autoirrigation.Fragments.HomeFragment;
import com.example.autoirrigation.Fragments.SecondFragment;
import com.example.autoirrigation.Fragments.ThirdFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabAdapter adapter;
    private NavigationView navView;
    public static final String[] tabTitle = new String[]{"首页", "Tab2", "Tab3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseTool.setStatusTransparent(this.getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewPager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(SecondFragment.newInstance());
        fragments.add(ThirdFragment.newInstance());

        adapter = new TabAdapter(getSupportFragmentManager(), fragments, tabTitle);
        //给viewPager设置适配器
        viewPager.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(viewPager);

        /**
         * 设置底部tab样式，以及添加监听事件
         */
        Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition())).setIcon(R.drawable.home_tab_selected_icon);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.home_tab_icon);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.home_tab_icon);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.home_tab_selected_icon);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.home_tab_icon);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        /**
         * 设置侧边栏home按钮
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar mToolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        }

        /**
         * 设置侧边栏MenuItem点击事件
         */
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_setting:
                        Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_feedback:
                        Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_about:
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_LONG).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


    //home按钮打开drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                for(int i = 0; i < navView.getMenu().size(); i++){
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


