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
import com.github.mikephil.charting.charts.LineChart;

import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
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

        Chart chart = new Chart((LineChart)view.findViewById(R.id.linechart), getActivity());
        chart.initChart();

        recyclerView = view.findViewById(R.id.water_hisotry_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = view.findViewById(R.id.water_history_swiperefreshlayout);

        initList();

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
                                initList();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });

        return view;
    }

    private void initList(){
        irrigationHistoryList = new LinkedList<IrrigationHistory>();
        for(int i = 0; i < 3; i++){
            String id = String.valueOf(i+"号设备");
            String time = Calendar.getInstance().getTime().toString().substring(0, 5);
            String flow = String.valueOf(5);
            IrrigationHistory irrigationHistory = new IrrigationHistory(id, time, flow);
            irrigationHistoryList.add(irrigationHistory);
        }
    }
}
