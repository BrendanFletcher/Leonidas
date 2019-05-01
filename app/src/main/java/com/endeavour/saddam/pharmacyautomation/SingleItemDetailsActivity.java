package com.endeavour.saddam.pharmacyautomation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;

import java.text.MessageFormat;

public class SingleItemDetailsActivity extends AppCompatActivity {
    private TextView name;
    private TextView type;
    private TextView mfr;
    private TextView uses;
    private TextView mfgDate;
    private TextView expDate;
    private TextView price;
    private TextView quantity;
    private TextView available;
    private TextView location;
    private String MFG_FORMAT;
    private String EXP_FORMAT;
    private String AVAILABLE_FORMAT;
    private String LOCATION_FORMAT;
    private String MFR_FORMAT;
    private String NAME_FORMAT;
    private MedicineDAO medicineDAO;

//    public SingleItemDetailsActivity(Activity activity) {
//        this.medicineDAO = AppDatabase.getInstance(activity).medicineDAO();
//        this.MFG_FORMAT = activity.getString(R.string.mfg_date_format);
//        this.EXP_FORMAT = activity.getString(R.string.exp_date_format);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_details);
        initView();
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Constants.Key.MEDICINE)) {
            MedicineDTO medicineDTO = (MedicineDTO) intentThatStartedThisActivity.getParcelableExtra(Constants.Key.MEDICINE);
            name.setText(MessageFormat.format(NAME_FORMAT, medicineDTO.getName()));
            mfr.setText(MessageFormat.format(MFR_FORMAT, medicineDTO.getManufacturer()));
            available.setText(MessageFormat.format(AVAILABLE_FORMAT, medicineDTO.getAvailable()));
            location.setText(MessageFormat.format(LOCATION_FORMAT, medicineDTO.getLocation()));
            mfgDate.setText(MessageFormat.format(MFG_FORMAT,Utils.formatDate(medicineDTO.getManufacturingDate())));
            expDate.setText(MessageFormat.format(EXP_FORMAT, Utils.formatDate(medicineDTO.getExpireDate())));
        }
    }

    private void initView() {
        name = findViewById(R.id.item_name);
        mfr = findViewById(R.id.item_mfr);
        available = findViewById(R.id.item_available);
        location = findViewById(R.id.item_location);
        mfgDate = findViewById(R.id.item_mfg_date);
        expDate = findViewById(R.id.item_exp_date);
        this.NAME_FORMAT = getString(R.string.name_format);
        this.MFR_FORMAT = getString(R.string.mfr_format);
        this.MFG_FORMAT = getString(R.string.mfg_date_format);
        this.EXP_FORMAT = getString(R.string.exp_date_format);
        this.AVAILABLE_FORMAT = getString(R.string.available_format);
        this.LOCATION_FORMAT = getString(R.string.location_format);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
