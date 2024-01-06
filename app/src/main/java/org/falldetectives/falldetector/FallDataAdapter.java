package org.falldetectives.falldetector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FallDataAdapter extends RecyclerView.Adapter<FallDataAdapter.ViewHolder> {

    private final List<FallData> fallDataList;

    public FallDataAdapter(List<FallData> fallDataList) {
        this.fallDataList = fallDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fall_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FallData fallData = fallDataList.get(position);
        holder.textViewTimestamp.setText(fallData.getFormattedTimestamp());
        holder.textViewStatus.setText(fallData.getFalseAlarmStatus());
    }

    @Override
    public int getItemCount() {
        return fallDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTimestamp;
        public TextView textViewStatus;

        public ViewHolder(View view) {
            super(view);
            textViewTimestamp = view.findViewById(R.id.textViewTimestamp);
            textViewStatus = view.findViewById(R.id.textViewStatus);
        }
    }
}

