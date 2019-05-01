package com.endeavour.saddam.pharmacyautomation.adapter.holder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.endeavour.saddam.pharmacyautomation.R;
import com.github.mikephil.charting.charts.BarChart;

public class ChartViewHolder extends RecyclerView.ViewHolder {

    public BarChart chart;
    public AppCompatTextView total;

    public ChartViewHolder(View v) {
        super(v);
        chart = v.findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        total = v.findViewById(R.id.total);
    }
}