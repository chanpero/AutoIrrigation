package com.example.autoirrigation.DeviceStatus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autoirrigation.R;

import java.util.List;

public class deviceStatusAdapter extends RecyclerView.Adapter<deviceStatusAdapter.ViewHolder> {
    private List<DeviceStatus> mDeviceStatusList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceId;
        TextView linkTime;
        ImageView deviceCurrentStatus;

        public ViewHolder(View view) {
            super(view);
            deviceId = view.findViewById(R.id.deviceId);
            linkTime = view.findViewById(R.id.linkTime);
            deviceCurrentStatus = view.findViewById(R.id.deviceCurrentStatus);
        }
    }

    public deviceStatusAdapter(List<DeviceStatus> list){ this.mDeviceStatusList = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_status_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        DeviceStatus deviceStatus = mDeviceStatusList.get(position);
        holder.deviceId.setText(deviceStatus.getDeviceId() + "");
        holder.linkTime.setText(deviceStatus.getLinkTime());
        if(deviceStatus.isStatus())
            holder.deviceCurrentStatus.setImageResource(R.drawable.deviceonline);
        else
            holder.deviceCurrentStatus.setImageResource(R.drawable.deviceoffline);
    }

    @Override
    public int getItemCount() {
        return mDeviceStatusList.size();
    }
}
