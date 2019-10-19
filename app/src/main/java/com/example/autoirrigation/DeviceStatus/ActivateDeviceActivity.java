package com.example.autoirrigation.DeviceStatus;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autoirrigation.R;

public class ActivateDeviceActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int DEVICE_ID_REQUEST = 0;
    public static final int DEVICE_NAME_REQUEST = 1;
    public static final int DEVICE_INFO_REQUEST = 2;

    private LinearLayout deviceId;
    private LinearLayout deviceName;
    private LinearLayout deviceInfo;
    private Button activateButton;
    private TextView deviceIdHint;
    private TextView deviceNameHint;
    private TextView deviceInfoHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        initView();
    }

    private void initView(){
        deviceId = findViewById(R.id.add_device_deviceId_LinearLayout);
        deviceName = findViewById(R.id.add_device_deviceName_LinearLayout);
        deviceInfo = findViewById(R.id.add_device_deviceInfo_LinearLayout);
        activateButton = findViewById(R.id.add_device_activation_button);
        deviceIdHint = findViewById(R.id.add_device_deviceId_hint);
        deviceNameHint = findViewById(R.id.add_device_deviceName_hint);
        deviceInfoHint = findViewById(R.id.add_device_deviceInfo_hint);

        deviceId.setOnClickListener(this);
        deviceName.setOnClickListener(this);
        deviceInfo.setOnClickListener(this);
        activateButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.add_device_deviceId_LinearLayout:
                intent = new Intent(this, FloatingInputActivity.class);
                startActivityForResult(intent, DEVICE_ID_REQUEST);
                break;
            case R.id.add_device_deviceName_LinearLayout:
                intent = new Intent(this, FloatingInputActivity.class);
                startActivityForResult(intent, DEVICE_NAME_REQUEST);
                break;
            case R.id.add_device_deviceInfo_LinearLayout:
                intent = new Intent(this, FloatingInputActivity.class);
                startActivityForResult(intent, DEVICE_INFO_REQUEST);
                break;
            case R.id.add_device_activation_button:
                Toast.makeText(this, "Device Not Found !", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK)
        {
            assert data != null;
            String content = data.getStringExtra("content");
            switch (requestCode)
            {
                case DEVICE_ID_REQUEST:
                    deviceIdHint.setText(content);
                    break;
                case DEVICE_NAME_REQUEST:
                    deviceNameHint.setText(content);
                    break;
                case DEVICE_INFO_REQUEST:
                    deviceInfoHint.setText(content);
                    break;
                default:
                    break;
            }
        }
    }
}
