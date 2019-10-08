package com.example.autoirrigation.IrrigationStatistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.autoirrigation.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class LineChartMarkView extends MarkerView {
    private static String[] WEEK = {"一", "二", "三", "四", "五", "六", "日"};

    private TextView tvDate;
    private TextView tvValue;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_markview);
        this.xAxisValueFormatter = xAxisValueFormatter;

        tvDate = findViewById(R.id.tv_date);
        tvValue = findViewById(R.id.tv_value);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
        tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
//        tvDate.setText("周" + WEEK[(int)e.getX() - 1]);
        tvValue.setText("流量: " + e.getY() + "L");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
