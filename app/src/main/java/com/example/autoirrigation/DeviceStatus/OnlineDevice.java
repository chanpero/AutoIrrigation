package com.example.autoirrigation.DeviceStatus;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlineDevice extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private List<DeviceStatus> onlineDeviceStatusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_device);

        swipeRefreshLayout = findViewById(R.id.onlineDevice_SwipeRefreshLayout);
        recyclerView = findViewById(R.id.onlineDevice_RecyclerView);
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    initView();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initView() throws ParseException {
        onlineDeviceStatusList = new ArrayList<>();

        List<String> returnList = new DBUtil().queryDeviceStatus("njfucs123456");
        for (String str : returnList) {
            if (str.equals("0"))         //没有任务情况
                break;
            String[] s = str.split("\\|");
//            <string>1|36 |1 |2019/8/30 9:50:05|2019/8/30 9:59:40</string>
            int deviceId = Integer.valueOf(s[1].trim());
            String subTime = s[3].trim();
            String updateTime = s[4].trim();
            DeviceStatus deviceStatus = new DeviceStatus(deviceId, subTime, updateTime);

            if(deviceStatus.isStatus())
                onlineDeviceStatusList.add(deviceStatus);
        }

        recyclerView.removeAllViewsInLayout();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        deviceStatusAdapter adapter = new deviceStatusAdapter(onlineDeviceStatusList);
        adapter.setContext(this);
//        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        recyclerView.setAdapter(adapter);
    }
}
