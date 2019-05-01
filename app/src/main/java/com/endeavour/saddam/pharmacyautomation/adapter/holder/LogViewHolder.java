package com.endeavour.saddam.pharmacyautomation.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.R;

public class LogViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView price;
    public TextView quantity;
    public TextView time;

    public LogViewHolder(View v) {
        super(v);
        name = v.findViewById(R.id.name);
        price = v.findViewById(R.id.price);
        quantity = v.findViewById(R.id.quantity);
        time = v.findViewById(R.id.time);
    }
}