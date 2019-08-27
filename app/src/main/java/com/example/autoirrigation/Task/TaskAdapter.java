package com.example.autoirrigation.Task;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autoirrigation.R;
import com.example.autoirrigation.Tools.DBUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TimingTask> mTaskList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskDeviceCode;
        TextView taskTime;
        TextView taskCirc;
        ImageView taskImage;
        Button deleteTask;
        SwitchCompat taskSwitch;

        public ViewHolder(View view) {
            super(view);
            taskDeviceCode = view.findViewById(R.id.taskDeviceCode);
            taskTime = view.findViewById(R.id.taskTime);
            taskCirc = view.findViewById(R.id.taskCirc);
            taskImage = view.findViewById(R.id.taskImage);
            deleteTask = view.findViewById(R.id.deleteTask);
            taskSwitch = view.findViewById(R.id.taskSwitch);
        }
    }

    public TaskAdapter(List<TimingTask> list) {
        this.mTaskList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        TimingTask task = mTaskList.get(position);
        if (task.getOperationMode().equals("open")) {
            holder.taskImage.setImageResource(R.drawable.open_valve);
        } else if (task.getOperationMode().equals("close")) {
            holder.taskImage.setImageResource(R.drawable.close_valve);
        }
        holder.taskDeviceCode.setText(task.getDeviceCode());
        holder.taskSwitch.setChecked(task.isOn());
        String circMode = task.getCirculationMode();
        String[] s = task.getTime().split("/");
        String time = "";
        if (circMode.equals("everyDay")) {
            time = s[3] + "时" + s[4] + "分";
            circMode = "每天";
        } else if (circMode.equals("everyWeek")) {
            time = s[3] + "时" + s[4] + "分";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", new Locale("ZN"));
            Date date = new Date();
            try {
                date = sdf.parse(task.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String[] week = new String[]{"日", "一", "二", "三", "四", "五", "六"};
            circMode = "每周" + week[calendar.get(7) - 1];
        } else if (circMode.equals("everyMonth")) {
            time = s[3] + "时" + s[4] + "分";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm", new Locale("ZN"));
            Date date = new Date();
            try {
                date = sdf.parse(task.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            circMode = "每月" + calendar.get(5) + "号";
        } else {
            time = s[0].substring(2, 4) + "年" + s[1] + "月" + s[2] + "日" + s[3] + "时" + s[4] + "分";
            circMode = "单次";
        }
        holder.taskCirc.setText(circMode);
        holder.taskTime.setText(time);


        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Item里的删除
                DBUtil dbUtil = new DBUtil();
                TimingTask task = mTaskList.get((position));
                String circ = task.getCirculationMode();
                dbUtil.deleteTask(task.getDeviceCode(), task.getOperationMode(), task.getTime(), task.getCirculationMode(), "njfucs123456");
                removeData(position);
            }
        });

        holder.taskSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Switch开关逻辑
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    //  删除数据
    public void removeData(int position) {
        mTaskList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTaskList.size());
    }
}
