package com.example.autoirrigation.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autoirrigation.IrrigationStatistics.Chart;
import com.example.autoirrigation.IrrigationStatistics.IrrigationHistory;
import com.example.autoirrigation.IrrigationStatistics.IrrigationHistoryAdapter;
import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;
import com.example.autoirrigation.Tools.Utils;
import com.github.mikephil.charting.charts.LineChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class IrrigationStatistic extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<IrrigationHistory> irrigationHistoryList;
    private SwipeRefreshLayout swipeRefreshLayout;


    public static IrrigationStatistic newInstance() {
        return new IrrigationStatistic();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        recyclerView = view.findViewById(R.id.water_hisotry_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = view.findViewById(R.id.water_history_swiperefreshlayout);

        try {
            initList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mAdapter = new IrrigationHistoryAdapter(irrigationHistoryList);
        recyclerView.setAdapter(mAdapter);

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
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    initList();
                                    mAdapter = new IrrigationHistoryAdapter(irrigationHistoryList);
                                    recyclerView.setAdapter(mAdapter);
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



        Chart chart = new Chart((LineChart)view.findViewById(R.id.linechart), getActivity(), irrigationHistoryList);
        chart.initChart();

        return view;
    }

    private void initList() throws ParseException {
        irrigationHistoryList = new LinkedList<IrrigationHistory>();

        List<String> list = new DBUtil().queryIrrigationHistory("njfucs123456");
        //<string>01 05 00 13 FF 00 7D FF|2019/8/31 17:03:08</string>
        boolean lastControlCodeIsOpen = false;
        Date beginDate = null;
        Date endDate = null;
        for(String s : list){
            String[] stringArray = s.split("\\|");

            if(stringArray[0].equals("01 05 00 13 00 00 3C 0F") && !lastControlCodeIsOpen)
                continue;
            if(stringArray[0].equals("01 05 00 13 FF 00 7D FF") && lastControlCodeIsOpen)
                continue;

            if(stringArray[0].equals("01 05 00 13 FF 00 7D FF") && !lastControlCodeIsOpen){
                lastControlCodeIsOpen = true;
                beginDate = Utils.parseDateString(stringArray[1], "yy/MM/dd HH:mm:ss");
            }

            if(stringArray[0].equals("01 05 00 13 00 00 3C 0F") && lastControlCodeIsOpen){
                lastControlCodeIsOpen = false;
                endDate = Utils.parseDateString(stringArray[1], "yy/MM/dd HH:mm:ss");
            }

            if(!lastControlCodeIsOpen && beginDate != null && endDate != null){
                irrigationHistoryList.add(new IrrigationHistory(beginDate, endDate));
            }
        }
    }
}
