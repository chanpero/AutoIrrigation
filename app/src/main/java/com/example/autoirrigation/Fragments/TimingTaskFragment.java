package com.example.autoirrigation.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

import com.example.autoirrigation.R;
import com.example.autoirrigation.TimingTask.AddTaskActivity;
import com.example.autoirrigation.TimingTask.AlarmService;
import com.example.autoirrigation.TimingTask.TaskAdapter;
import com.example.autoirrigation.TimingTask.TimingTask;
import com.example.autoirrigation.Tools.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static android.content.Context.ALARM_SERVICE;

public class TimingTaskFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton taskFab;

    private List<PendingIntent> pendingIntents;
    private List<AlarmManager> alarmManagers;

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

        recyclerView.setItemAnimator(new OvershootInLeftAnimator(2f));

        taskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTaskActivity.class));
            }
        });

        pendingIntents = new ArrayList<>();
        alarmManagers = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public void updateView() throws ParseException {
        mTaskList = new ArrayList<>();
        List<String> returnList = new DBUtil().queryTask("njfucs123456");
        for (String str : returnList) {
            if (str.equals("0"))         //没有任务情况
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
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));

        updateAlarm();
    }

    private void updateAlarm() throws ParseException {
        for(AlarmManager am : alarmManagers)
            am.cancel(pendingIntents.get(alarmManagers.indexOf(am)));

        for(TimingTask task : mTaskList){
            if(task.isOn()){
                AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(ALARM_SERVICE);
                Intent newIntent = new Intent(getActivity(), AlarmService.class);
                //set open valve or close valve
                newIntent.setAction(task.getOperationMode());
                long firstTime = getFirstTime(task);
                long interval = getInterval(task);
                if(firstTime == -1)
                    continue;
                PendingIntent pendingIntent=PendingIntent.getService(getContext(),0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                if(task.getCirculationMode().equals("once")){
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, firstTime, pendingIntent);
                }
                else{
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, interval, pendingIntent);
                }

                alarmManagers.add(alarmManager);
                pendingIntents.add(pendingIntent);
            }

        }
    }

    private long getFirstTime(TimingTask task) throws ParseException {
        int[] dayOfMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String dateString = task.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", new Locale("ZN"));
        Date date = sdf.parse(dateString);
        long time = date.getTime();
        while(time < new Date().getTime()){
            if(task.getCirculationMode().equals("everyDay"))
                time += AlarmManager.INTERVAL_DAY;
            else if(task.getCirculationMode().equals("everyWeek"))
                time += AlarmManager.INTERVAL_DAY * 7;
            else if(task.getCirculationMode().equals("everyMonth")){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if(calendar.get(2) == 1 && isLeapYaer(calendar.get(1))){
                    time += AlarmManager.INTERVAL_DAY * 29;
                }
                else{
                    time += AlarmManager.INTERVAL_DAY * dayOfMonth[calendar.get(2)];
                }
            }
            else {
                time = -1;
                break;
            }
        }
        return time;
    }

    private long getInterval(TimingTask task) throws ParseException {
        int[] dayOfMonth = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String circMode = task.getCirculationMode();
        String dateString = task.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", new Locale("ZN"));
        Date date = sdf.parse(dateString);

        if(circMode.equals("everyDay"))
            return AlarmManager.INTERVAL_DAY;
        else if(circMode.equals("everyWeek"))
            return AlarmManager.INTERVAL_DAY * 7;
        else if(circMode.equals("everyMonth")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if(calendar.get(2) == Calendar.FEBRUARY && isLeapYaer(calendar.get(1))){
                return AlarmManager.INTERVAL_DAY * 29;
            }
            else{
                return AlarmManager.INTERVAL_DAY * dayOfMonth[calendar.get(2)];
            }
        }
        else
            return -1;
    }

    private boolean isLeapYaer(int year){
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
