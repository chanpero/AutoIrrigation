package com.example.autoirrigation.DeviceStatus;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.autoirrigation.R;

import org.w3c.dom.Text;

public class DeviceInfo extends AppCompatActivity {
    private FloatingActionButton fab;
    private TextView tv;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        initView();
    }

    private void initView(){
        fab = findViewById(R.id.device_info_fab);
        tv = findViewById(R.id.device_info_tv);
        activity = this;
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.exit);
    }
}
