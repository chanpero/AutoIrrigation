package com.example.autoirrigation.DeviceStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeviceStatus {
    private int deviceId;
    private String linkTime;
    private boolean status;

    public DeviceStatus(int deviceId, String subTime, String updateTime) throws ParseException {
        this.deviceId = deviceId;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:MM:ss", new Locale("ZN"));
        Date subDate = sdf.parse(subTime);
        Date updateDate = sdf.parse(updateTime);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(subDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(updateDate);

        if(calendar2.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < 60 * 1000){
            this.linkTime = subTime;
            this.status = true;
        }
        else{
            this.linkTime = updateTime;
            this.status = false;
        }
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getLinkTime() {
        return linkTime;
    }

    public void setLinkTime(String linkTime) {
        this.linkTime = linkTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
