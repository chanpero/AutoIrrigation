package com.example.autoirrigation.TimingTask;

import java.io.Serializable;

public class TimingTask implements Serializable {
    private String deviceCode;
    private String operationMode;
    private String time;
    private String circulationMode;

    private boolean isAlarmed;

    private boolean isOn;

    public TimingTask(String deviceCode, String operationMode, String time, String circulationMode, boolean isOn) {
        this.deviceCode = deviceCode;
        this.operationMode = operationMode;
        this.time = time;
        this.circulationMode = circulationMode;
        this.isOn = isOn;

        this.isAlarmed = false;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }

    public String getCirculationMode() {
        return circulationMode;
    }

    public void setCirculationMode(String circulationMode) {
        this.circulationMode = circulationMode;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public boolean isAlarmed() {
        return isAlarmed;
    }

    public void setAlarmed(boolean alarmed) {
        isAlarmed = alarmed;
    }
}
