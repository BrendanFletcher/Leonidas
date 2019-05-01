package com.endeavour.saddam.pharmacyautomation.adapter.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.R;

public class SellInfoViewHolder extends RecyclerView.ViewHolder{
    public TextView medicineName;
    public TextView medicinePrice;
    public TextView medicineQuantity;
    public TextView totalTvQuantity;
    public TextView totalTvPrice;
    public View splitLine;

    public SellInfoViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        medicineName = itemView.findViewById(R.id.sell_medicine_name);
        medicineQuantity = itemView.findViewById(R.id.sell_medicine_quantity);
        medicinePrice = itemView.findViewById(R.id.sell_medicine_price);
        totalTvQuantity = itemView.findViewById(R.id.sell_medicine_total_quantity);
        totalTvPrice = itemView.findViewById(R.id.sell_medicine_total);
        splitLine = itemView.findViewById(R.id.splitLine_hor1);
    }
}
