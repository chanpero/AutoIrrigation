package com.example.autoirrigation.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.example.autoirrigation.R;
import com.example.autoirrigation.Task.AddTaskActivity;
import com.example.autoirrigation.Task.TaskAdapter;
import com.example.autoirrigation.Task.TimingTask;
import com.example.autoirrigation.Tools.DBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class TimingTaskFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton taskFab;

    private List<TimingTask> mTaskList;

    public static TimingTaskFragment newInstance() {
        return new TimingTaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recyclerview);
        taskFab = view.findViewById(R.id.task_fab);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUI();
            }
        });

        recyclerView.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));

        taskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTaskActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
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
                        updateView();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void updateView() {
        mTaskList = new ArrayList<>();
        List<String> returnList = new DBUtil().queryTask("njfucs123456");
        for (String str : returnList) {
            if(str.equals("0"))         //没有任务情况
                break;
            String[] s = str.split("/");
//            <string>0001/open/2019/6/22/7/35/once/True</string>
            String deviceCode = s[0];
            String operationMode = s[1];
            String time = s[2] + "/" + s[3] + "/" + s[4] + "/" + s[5] + "/" + s[6];
            String circulationMode = s[7];
            boolean isOn = s[8].equals("True");

            mTaskList.add(new TimingTask(deviceCode, operationMode, time, circulationMode, isOn));
        }

        recyclerView.removeAllViewsInLayout();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        TaskAdapter adapter = new TaskAdapter(mTaskList);
        recyclerView.setAdapter(adapter);
    }
}
