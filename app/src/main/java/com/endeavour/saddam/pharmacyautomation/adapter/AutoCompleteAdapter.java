package com.endeavour.saddam.pharmacyautomation.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.service.MedicineService;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutoCompleteAdapter extends ArrayAdapter<String> {

    private MedicineService medicineService;

    public AutoCompleteAdapter(Activity activity) {
        super(activity, android.R.layout.simple_dropdown_item_1line);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(activity.getString(R.string.suggest_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        medicineService = retrofit.create(MedicineService.class);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    try {
                        Response<String[]> response = medicineService.suggestName(constraint.toString()).execute();
                        if (response.isSuccessful()) {
                            String[] suggestions = response.body();
                            if (suggestions != null) {
                                results.values = suggestions;
                                results.count = suggestions.length;
                                return results;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();

                if (results.values != null) {
                    addAll((String[]) results.values);
                }

                notifyDataSetChanged();
            }
        };
    }
}
