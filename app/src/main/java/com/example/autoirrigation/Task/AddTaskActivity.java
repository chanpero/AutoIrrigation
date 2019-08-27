package com.example.autoirrigation.Task;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    private Spinner deviceSpinner;
    private Spinner operationSpinner;
    private Spinner circSpinner;
    private LinearLayout selectDate;
    private LinearLayout selectTime;
    private TextView timeTextView;
    private TextView dateTextView;
    private Button cancelTask;
    private Button submitTask;

    private int year, month, day, hour, minute;
    private StringBuffer date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initView();
        initDateTime();
    }

    public void initView() {
        deviceSpinner = findViewById(R.id.deviceSpinner);
        operationSpinner = findViewById(R.id.operationSpinner);
        circSpinner = findViewById(R.id.circSpinner);
        selectDate = findViewById(R.id.selectDate);
        selectTime = findViewById(R.id.selectTime);
        timeTextView = findViewById(R.id.timeTextView);
        dateTextView = findViewById(R.id.dateTextView);
        cancelTask = findViewById(R.id.cancelTask);
        submitTask = findViewById(R.id.submitTask);

        date = new StringBuffer();
        time = new StringBuffer();

        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        cancelTask.setOnClickListener(this);
        submitTask.setOnClickListener(this);

        ArrayAdapter<CharSequence> deviceAdapter = ArrayAdapter.createFromResource(this, R.array.device_array, android.R.layout.simple_spinner_item);
        deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceSpinner.setAdapter(deviceAdapter);

        ArrayAdapter<CharSequence> operationAdapter = ArrayAdapter.createFromResource(this, R.array.operation_array, android.R.layout.simple_spinner_item);
        operationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationSpinner.setAdapter(operationAdapter);

        ArrayAdapter<CharSequence> circAdapter = ArrayAdapter.createFromResource(this, R.array.circ_array, android.R.layout.simple_spinner_item);
        circAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        circSpinner.setAdapter(circAdapter);
    }

    public void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectDate:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) {
                            date.delete(0, date.length());
                        }
                        dateTextView.setText(date.append(year).append("年").append(month).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.dialog_date, null);
                final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("设置日期");
                dialog.setView(dialogView);
                dialog.show();
                datePicker.init(year, month - 1, day, this);
                break;
            case R.id.selectTime:
                AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(this);
                builder2.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        timeTextView.setText(time.append(hour).append("时").append(minute).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(this, R.layout.dialog_time, null);
                TimePicker timePicker = dialogView2.findViewById(R.id.timePicker);
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);
                dialog2.setTitle("设置时间");
                dialog2.setView(dialogView2);
                dialog2.show();
                break;
            case R.id.cancelTask:
                this.finish();
                break;
            case R.id.submitTask:
                if (dateTextView.getText().toString().equals("")) {
                    Toast.makeText(this, "请选择日期", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (timeTextView.getText().toString().equals("")) {
                    Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
                    break;
                }


                String deviceCode = deviceSpinner.getSelectedItem().toString();

                String operation = operationSpinner.getSelectedItem().toString();
                if (operation.equals("打开阀门"))
                    operation = "open";
                else
                    operation = "close";

                String time = year + "/" + (month + 1) + "/" + day + "/" + hour + "/" + minute;

                String circMode = circSpinner.getSelectedItem().toString();
                if (circMode.equals("每天"))
                    circMode = "everyDay";
                else if (circMode.equals("每周"))
                    circMode = "everyWeek";
                else if (circMode.equals("每月"))
                    circMode = "everyMonth";
                else
                    circMode = "once";

                if (new DBUtil().insertTask(deviceCode, operation, time, circMode, "njfucs123456"))
                    Toast.makeText(this, "任务添加成功", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "任务添加失败", Toast.LENGTH_SHORT).show();
                    break;
                }

                this.finish();
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }
}
