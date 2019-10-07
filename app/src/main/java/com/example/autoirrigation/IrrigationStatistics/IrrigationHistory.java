package com.example.autoirrigation.IrrigationStatistics;

public class IrrigationHistory {
    private String deviceId;
    private String irrigationTime;
    private String flow;

    public IrrigationHistory(String deviceId, String irrigationTime, String flow){
        this.deviceId = deviceId;
        this.irrigationTime = irrigationTime;
        this.flow = flow;
    }

    public IrrigationHistory(){
        deviceId = "null";
        irrigationTime = "1970/1/1";
        flow = "null";
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

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
