package com.example.autoirrigation;

import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OperateActivity extends AppCompatActivity {
    private Button openValve;
    private Button closeValve;
    private Button stopValve;

    private DBUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BaseTool.setStatusTransparent(this.getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        /**
         *  Register Listener for three Buttons
         */
        openValve = (Button) findViewById(R.id.openValve);
        openValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBUtil...
                Toast.makeText(OperateActivity.this, "阀门打开成功", Toast.LENGTH_SHORT).show();
            }
        });

        closeValve = (Button) findViewById(R.id.closeValve);
        closeValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBUtil...
                Toast.makeText(OperateActivity.this, "阀门关闭成功", Toast.LENGTH_SHORT).show();
            }
        });

        stopValve = (Button) findViewById(R.id.stopValve);
        stopValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DBUtil...
                Toast.makeText(OperateActivity.this, "阀门停止成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
