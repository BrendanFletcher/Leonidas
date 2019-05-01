package com.endeavour.saddam.pharmacyautomation.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.adapter.holder.ChartViewHolder;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;
import com.github.mikephil.charting.data.BarData;

import java.text.MessageFormat;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartViewHolder> {

    private List<BarData> barDataList;

    private final String TOTAL_REVENUE_FORMAT;

    public ChartAdapter(Activity activity, List<BarData> barDataList) {
        this.barDataList = barDataList;

        this.TOTAL_REVENUE_FORMAT = activity.getString(R.string.total_revenue_format);
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_chart, parent, false);
        return new ChartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder chartViewHolder, int position) {
        BarData data = barDataList.get(position);
        chartViewHolder.chart.setData(data);
        chartViewHolder.total.setText(MessageFormat.format(TOTAL_REVENUE_FORMAT, Utils.total(data)));
    }

    @Override
    public int getItemCount() {
        return barDataList.size();
    }

    public boolean isEmpty() {
        return barDataList.isEmpty();
    }

    public void setData(List<BarData> barDataList) {
        this.barDataList = barDataList;
    }
}
