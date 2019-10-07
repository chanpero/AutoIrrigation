package com.example.autoirrigation.IrrigationStatistics;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autoirrigation.R;

import java.util.List;

public class IrrigationHistoryAdapter extends RecyclerView.Adapter<IrrigationHistoryAdapter.IrrigationHistoryViewHolder> {
    private List<IrrigationHistory> IrrigationHistoryList;

    public static class IrrigationHistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView deviceId;
        public TextView irrigationTime;
        public TextView flow;

        public IrrigationHistoryViewHolder(View view){
            super(view);
            this.deviceId = view.findViewById(R.id.irrigationhistory_id);
            this.irrigationTime = view.findViewById(R.id.irrigationhistory_time);
            this.flow = view.findViewById(R.id.irrigationhistory_flow);
        }
    }

    public IrrigationHistoryAdapter(List<IrrigationHistory> list){
        this.IrrigationHistoryList = list;
    }

    @NonNull
    @Override
    public IrrigationHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.irrigation_history, viewGroup, false);
        return new IrrigationHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IrrigationHistoryViewHolder holder, int position) {
        IrrigationHistory irrigationHistory = IrrigationHistoryList.get(position);
        holder.deviceId.setText(irrigationHistory.getDeviceId());
        holder.irrigationTime.setText(irrigationHistory.getIrrigationTime());
        holder.flow.setText(irrigationHistory.getFlow());
    }

    @Override
    public int getItemCount() {
        return IrrigationHistoryList.size();
    }
}
