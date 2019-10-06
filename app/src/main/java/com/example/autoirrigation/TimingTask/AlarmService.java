package com.example.autoirrigation.TimingTask;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.autoirrigation.Tools.DBUtil;

import java.util.Objects;

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        if(Objects.equals(intent.getAction(), "open")){
            if(new DBUtil().controlValve("01 05 00 13 FF 00 7D FF", "njfucs123456"))
                Log.d("Alarm", "onStartCommand: 定时open成功");
        }
        else if(Objects.equals(intent.getAction(), "close")){
            if(new DBUtil().controlValve("01 05 00 13 00 00 3C 0F", "njfucs123456"))
                Log.d("Alarm", "onStartCommand: 定时close成功");
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
