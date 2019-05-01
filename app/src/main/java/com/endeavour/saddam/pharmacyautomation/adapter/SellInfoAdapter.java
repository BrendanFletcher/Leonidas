package com.endeavour.saddam.pharmacyautomation.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.adapter.holder.SellInfoViewHolder;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;

import java.text.MessageFormat;
import java.util.List;

public class SellInfoAdapter extends RecyclerView.Adapter<SellInfoViewHolder>{

    private Activity activity;
    private MedicineDAO medicineDAO;
    private Handler mHandler;
    private List<SoldDTO> medicines;
    private final String PRICE_FORMAT;
    private final String QUANTITY_FORMAT_SINGLE;
    private final String QUANTITY_FORMAT_MULTIPLE;
    private final String TOTAL_QUANTITY_FORMAT_SINGLE;
    private final String TOTAL_QUANTITY_FORMAT_MULTIPLE;
    private final String TOTAL_PRICE_FORMAT;
    Boolean FLAG = false;
    private int totalPrice = 0;
    private int totalQuantity = 0;
    private ViewGroup viewGroup;
    private TextView tvTotalPrice;
    private View splitLine;

    public SellInfoAdapter(Activity activity, Handler handler, List<SoldDTO> medicines) {
        this.activity = activity;
        this.medicines = medicines;
        this.mHandler = handler;
        this.medicineDAO = AppDatabase.getInstance(activity).medicineDAO();

        this.QUANTITY_FORMAT_MULTIPLE = activity.getString(R.string.sell_medicine_quantity_format_multiple);
        this.QUANTITY_FORMAT_SINGLE = activity.getString(R.string.sell_medicine_quantity_format_single);
        this.TOTAL_QUANTITY_FORMAT_SINGLE = activity.getString(R.string.total_quantity_format_single);
        this.TOTAL_QUANTITY_FORMAT_MULTIPLE = activity.getString(R.string.total_quantity_format_multiple);
        this.PRICE_FORMAT = activity.getString(R.string.sell_medicine_price_format);
        this.TOTAL_PRICE_FORMAT = activity.getString(R.string.total_price_format);
    }

    @NonNull
    @Override
    public SellInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.medicine_list_sell_info_row, parent, false);
//        tvTotalPrice = itemView.findViewById(R.id.sell_medicine_total);
//        tvTotalPrice.setVisibility(View.VISIBLE);
//        splitLine = itemView.findViewById(R.id.splitLine_hor1);
//        splitLine.setVisibility(View.VISIBLE);
//        this.viewGroup = parent;
        return new SellInfoViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull SellInfoViewHolder sellInfoViewHolder, int position) {

        final SoldDTO soldDTO = medicines.get(position);
        sellInfoViewHolder.medicineName.setText(soldDTO == null ? "Unknown Medicine" : soldDTO.getMedicineName());
        if(soldDTO.getQuantity() > 1) {
            sellInfoViewHolder.medicineQuantity.setText(MessageFormat.format(QUANTITY_FORMAT_MULTIPLE, soldDTO.getQuantity()));
        }
        else{
            sellInfoViewHolder.medicineQuantity.setText(MessageFormat.format(QUANTITY_FORMAT_SINGLE, soldDTO.getQuantity()));
        }
        sellInfoViewHolder.medicinePrice.setText(MessageFormat.format(PRICE_FORMAT, soldDTO.getPrice()));
        Log.d("HELL", " POS" + position + " : SIZE " + medicines.size());
        if(FLAG && position == medicines.size()-1){
            sellInfoViewHolder.totalTvQuantity.setVisibility(View.VISIBLE);
            if(totalQuantity > 1){
                sellInfoViewHolder.totalTvQuantity.setText(MessageFormat.format(TOTAL_QUANTITY_FORMAT_MULTIPLE, String.valueOf(totalQuantity)));
            }
            else{
                sellInfoViewHolder.totalTvQuantity.setText(MessageFormat.format(TOTAL_QUANTITY_FORMAT_SINGLE, String.valueOf(totalQuantity)));
            }
            sellInfoViewHolder.splitLine.setVisibility(View.VISIBLE);
            sellInfoViewHolder.totalTvPrice.setVisibility(View.VISIBLE);
            sellInfoViewHolder.totalTvPrice.setText(MessageFormat.format(TOTAL_PRICE_FORMAT, String.valueOf(totalPrice)));

            //sellInfoViewHolder.totalTv.setText(String.valueOf(Constants.TOTAL_PRICE));
        }
        else{
            if (sellInfoViewHolder.totalTvPrice != null && sellInfoViewHolder.splitLine != null && sellInfoViewHolder.totalTvQuantity != null) {
                sellInfoViewHolder.totalTvQuantity.setVisibility(View.GONE);
                sellInfoViewHolder.totalTvPrice.setVisibility(View.GONE);
                sellInfoViewHolder.splitLine.setVisibility(View.GONE);
            }
        }
//        else{
//            sellInfoViewHolder.totalTv.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setData(List<SoldDTO> soldMedicines) {
        this.medicines = soldMedicines;
        if(medicines.size() > 0){
            FLAG = true;
        }
    }

    public void setTotalPrice(int price, int quantity) {
        totalPrice = price;
        totalQuantity = quantity;
    }

    public void updateTotalPrice(int price, int quantity) {
        totalPrice += price;
        totalQuantity += quantity;
    }

    public boolean isEmpty() {
        return medicines == null || medicines.isEmpty();
    }

    public void updateTotalPriceView() {
        if(viewGroup != null) {
            TextView tv = viewGroup.findViewById(R.id.sell_medicine_total);
            if (tvTotalPrice.getVisibility() == View.VISIBLE){
                tvTotalPrice.setVisibility(View.GONE);
                splitLine.setVisibility(View.GONE);
            }

//            TextView tv = viewGroup.findViewById(R.id.sell_medicine_total);
//            if (tv.getVisibility() == View.VISIBLE){
//                tv.setVisibility(View.GONE);
//                viewGroup.findViewById(R.id.splitLine_hor1).setVisibility(View.GONE);
//            }
        }
    }
}
