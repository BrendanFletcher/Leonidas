package com.endeavour.saddam.pharmacyautomation.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeavour.saddam.pharmacyautomation.HomeActivity;
import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.adapter.holder.LogViewHolder;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldMedicineDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;

import java.text.MessageFormat;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

//    private List<SoldMedicineDTO> soldMedicines;
private List<SoldDTO> soldMedicines;

    private final String QUANTITY_FORMAT;
    private final String PRICE_FORMAT;
    private final String TIME_FORMAT;
    private MedicineDTO mDTO = null;
    private static final String TAG = LogAdapter.class.getName();

    public LogAdapter(Activity activity, List<SoldDTO> soldMedicines) {
        //this.soldMedicines = soldMedicines;
        this.soldMedicines = soldMedicines;

        this.QUANTITY_FORMAT = activity.getString(R.string.quantity_format);
        this.PRICE_FORMAT = activity.getString(R.string.price_format);
        this.TIME_FORMAT = activity.getString(R.string.time_format);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_log, parent, false);
        return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder logViewHolder, int position) {
        final SoldDTO soldDTO = soldMedicines.get(position);
//        final SoldMedicineDTO soldMedicineDTO = soldMedicines.get(position);
//        final SoldDTO soldDTO = soldMedicineDTO.getSoldDTO();
//         MedicineDTO medicineDTO = soldMedicineDTO.getMedicineDTO();
//        if(mDTO != null){
//            Log.d(TAG, " " + mDTO.getName() + " 1st");
//            if(medicineDTO == null){
//                medicineDTO = mDTO;
//                Log.d(TAG, " " + medicineDTO.getName() + " 2nd");
//            }
//            if(medicineDTO != null)
//            Log.d(TAG, " " + medicineDTO.getName() + " 3rd");
////
////            if(mDTO.getId()== medicineDTO.getId()) {
////                medicineDTO = mDTO;
////            }
//            //medicineDTO.setName(mDTO.getName());
//        }

        //logViewHolder.name.setText(medicineDTO == null ? "Unknown Medicine" : medicineDTO.getName());

        logViewHolder.name.setText(soldDTO == null ? "Unknown Medicine" : soldDTO.getMedicineName());
        logViewHolder.quantity.setText(MessageFormat.format(QUANTITY_FORMAT, soldDTO.getQuantity()));
        logViewHolder.price.setText(MessageFormat.format(PRICE_FORMAT, soldDTO.getPrice()));
        logViewHolder.time.setText(MessageFormat.format(TIME_FORMAT, Utils.formatTime(soldDTO.getTime())));
        //mDTO = medicineDTO;
    }

    @Override
    public int getItemCount() {
        return soldMedicines.size();
    }

    public boolean isEmpty() {
        return soldMedicines.isEmpty();
    }

    public void setData(List<SoldDTO> soldMedicines) {
        this.soldMedicines = soldMedicines;
    }
}
