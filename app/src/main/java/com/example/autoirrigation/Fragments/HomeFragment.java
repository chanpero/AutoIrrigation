package com.example.autoirrigation.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoirrigation.DeviceStatus.DeviceStatus;
import com.example.autoirrigation.DeviceStatus.deviceStatusAdapter;
import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class HomeFragment extends Fragment {
    private List<DeviceStatus> mDeviceStatusList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView onlineCountTV;
    private TextView offlineCountTV;
    private Handler handler;
    private Runnable runnable;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.deviceStatusSwipeRefreshLayout);
        recyclerView = view.findViewById(R.id.deviceStatusRecyclerView);
        onlineCountTV = view.findViewById(R.id.online_count);
        offlineCountTV = view.findViewById(R.id.offline_count);

        try {
            updateView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUI();
            }
        });

        //设置定时检查，每分钟检查一次设备状态
        handler =new Handler();
        runnable =new Runnable() {
            @Override
            public void run() {
                //要做的事情.updateview()
//                Toast.makeText(getActivity(), "更新设备状态", Toast.LENGTH_SHORT).show();
                try {
                    updateView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 60000);
            }
        };

        handler.postDelayed(runnable, 2000);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void refreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void updateView() throws ParseException {
        mDeviceStatusList = new ArrayList<>();

        List<String> returnList = new DBUtil().queryDeviceStatus("njfucs123456");
        for (String str : returnList) {
            if (str.equals("0"))         //没有任务情况
                break;
            String[] s = str.split("\\|");
//            <string>1|36 |1 |2019/8/30 9:50:05|2019/8/30 9:59:40</string>
            int deviceId = Integer.valueOf(s[1].trim());
            String subTime = s[3].trim();
            String updateTime = s[4].trim();

            mDeviceStatusList.add(new DeviceStatus(deviceId, subTime, updateTime));
        }

        recyclerView.removeAllViewsInLayout();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        deviceStatusAdapter adapter = new deviceStatusAdapter(mDeviceStatusList);
//        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        recyclerView.setAdapter(adapter);

        int onlineCount = 0;
        int offlineCount = 0;
        for (DeviceStatus deviceStatus : mDeviceStatusList) {
            if (deviceStatus.isStatus())
                onlineCount++;
            else
                offlineCount++;
        }
        onlineCountTV.setText(onlineCount + "");
        offlineCountTV.setText(offlineCount + "");
    }
}



