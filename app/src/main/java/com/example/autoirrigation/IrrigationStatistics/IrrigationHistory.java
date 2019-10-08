package com.example.autoirrigation.IrrigationStatistics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IrrigationHistory {
    private static double FLOW_PER_SECOND = 0.01;

    private String deviceId;
    private String irrigationTime;
    private double flow;

    private Date beginDate;
    private Date endDate;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public IrrigationHistory(Date beginDate, Date endDate){
        this.deviceId = "36";
        this.beginDate = beginDate;
        this.endDate = endDate;

        this.irrigationTime = new SimpleDateFormat("MM-dd HH:mm").format(beginDate) + "\n至\n" + new SimpleDateFormat("MM-dd HH:mm").format(endDate);

        DecimalFormat df = new DecimalFormat("#.00");
        double time = (double)(endDate.getTime() - beginDate.getTime()) / 1000;
        this.flow = Double.parseDouble(df.format(time * FLOW_PER_SECOND));
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIrrigationTime() {
        return irrigationTime;
    }

    public void setIrrigationTime(String irrigationTime) {
        this.irrigationTime = irrigationTime;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }
}
