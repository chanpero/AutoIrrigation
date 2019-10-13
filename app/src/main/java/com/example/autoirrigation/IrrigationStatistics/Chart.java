package com.example.autoirrigation.IrrigationStatistics;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.example.autoirrigation.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.reverse;

public class Chart {
    private LineChart lineChart;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    private List<IrrigationHistory> allIrrigationHistoryList;
    private List<IrrigationHistory> lastWeekIrrigationHistory;

    private static String[] WEEK_NAME = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    List<OneDayFlow> oneDayFlows;

    private Context context;

    public Chart(LineChart lineChart, Context context, List<IrrigationHistory> list) {
        this.lineChart = lineChart;
        this.context = context;
        this.allIrrigationHistoryList = list;
    }

    public void initChart() {
        filterLastWeekIrrigationHistory();

        initDataObjects();

        setChartAppearance();

        showLineChart(oneDayFlows, "一周灌溉统计", Color.LTGRAY);
        //设置背景渐变色
        Drawable drawable = context.getResources().getDrawable(R.drawable.fade_blue);
        setChartFillDrawable(drawable);

        setMarkerView();
    }

    /**
     * 设置chart外观
     */
    public void setChartAppearance(){
        Description description = new Description();
//        description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        lineChart.setBackgroundColor(Color.WHITE);


        //XY轴的设置
        xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return oneDayFlows.get((int)value - 1).getDate();
            }
        });
        xAxis.setTextSize(11f);
        xAxis.setDrawGridLines(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(1f);

        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);

        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        rightYaxis.setEnabled(false);
        leftYAxis.setEnabled(false);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }

    /**
     * 从所有灌溉历史中过滤出上一周的灌溉历史
     */
    public void filterLastWeekIrrigationHistory(){
        lastWeekIrrigationHistory = new LinkedList<>();

        Calendar oneWeekAgo = Calendar.getInstance();
        oneWeekAgo.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE) - 6);
        for(IrrigationHistory irrigationHistory : allIrrigationHistoryList){
            Calendar temp = Calendar.getInstance();
            temp.setTime(irrigationHistory.getBeginDate());

            if(temp.after(oneWeekAgo)){
                lastWeekIrrigationHistory.add(irrigationHistory);
            }
        }
    }

    /**
     * 初始化要显示的数据对象
     */
    public void initDataObjects(){
        Calendar today = Calendar.getInstance();
        int week = today.get(Calendar.DAY_OF_WEEK) - 1;

        oneDayFlows = new LinkedList<>();
        for(int i = 0; i < 7; i++) {
            oneDayFlows.add(new OneDayFlow(WEEK_NAME[(week - i + 7) % 7], 0f));
        }
//        reverse(oneDayFlows);

        for(IrrigationHistory irrigationHistory : lastWeekIrrigationHistory){
            Calendar temp = Calendar.getInstance();
            temp.setTime(irrigationHistory.getBeginDate());

            OneDayFlow oneDayFlow = oneDayFlows.get(temp.get(Calendar.DAY_OF_WEEK) + week);
            oneDayFlow.setFlow(oneDayFlow.getFlow() + irrigationHistory.getFlow());
        }

        reverse(oneDayFlows);
        oneDayFlows.get(6).setDate("今日");
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    public void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(final List<OneDayFlow> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            OneDayFlow data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i + 1, (float)data.getFlow());
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);

        //不显示点
        lineDataSet.setDrawCircles(false);

        initLineDataSet(lineDataSet, color, LineDataSet.Mode.CUBIC_BEZIER);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 设置线条填充背景颜色
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(context, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }
}

class OneDayFlow{
    private String date;
    private double flow;

    OneDayFlow(String date, double flow){
        this.date = date;
        this.flow = flow;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }
}
