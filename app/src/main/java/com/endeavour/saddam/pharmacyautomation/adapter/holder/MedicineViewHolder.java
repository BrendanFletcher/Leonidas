package com.endeavour.saddam.pharmacyautomation.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;

public class MedicineViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView type;
    public TextView mfr;
    public TextView uses;
    public TextView mfgDate;
    public TextView expDate;
    public TextView price;
    public TextView quantity;
    public TextView available;
    public TextView location;
    public TextView action;
    public LinearLayout lout;

    public MedicineViewHolder(View v, int viewType) {
        super(v);
        if(viewType == Constants.WARNING_TYPE) {
            expDate = v.findViewById(R.id.exp_date);
            expDate.setVisibility(View.VISIBLE);
        }
        name = v.findViewById(R.id.name);
        //type = v.findViewById(R.id.type);
        //mfr = v.findViewById(R.id.mfr);
        uses = v.findViewById(R.id.uses);
        // mfgDate = v.findViewById(R.id.mfg_date);
        quantity = v.findViewById(R.id.item_quantity);
        type = v.findViewById(R.id.type);
        price = v.findViewById(R.id.price);
        //available = v.findViewById(R.id.available);
        //location = v.findViewById(R.id.location);
        lout = v.findViewById(R.id.show_details);
        action = v.findViewById(R.id.action);
    }
}